package com.anseong.core.infrastructure.repository.hibernate.image;

import static com.anseong.core.domain.model.image.QImage.image;
import static com.anseong.core.domain.model.store.QImageStore.imageStore;
import static com.anseong.core.domain.model.user.QImageUser.imageUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageQueryRepository;
import com.anseong.core.domain.model.image.response.ImageDetailResponse;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.store.response.ImageStoreResponse;
import com.anseong.core.domain.model.tag.QTag;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.response.ImageUserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateImageQueryRepository implements ImageQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;
     private final EntityManager entityManager;

     HibernateImageQueryRepository(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
          this.jpaQueryFactory = jpaQueryFactory;
          this.entityManager = entityManager;
     }

     @Override
     public Map<String, List<ImageId>> findIdsGroupByTagName(List<String> tagNames, String current, boolean isAdult, int amount) {
          record QueryResult( String tagName, long id ) {}
          final Session session = entityManager.unwrap(Session.class);
          int time = 0;

          List<String> query = new ArrayList<>();

          /**
           * 추천 태그
           */
          for (int i = 0; i < tagNames.size(); i++) {
               query.add("""
                    (
                         SELECT :tagName%1$d as tagName, i.id
                         FROM image i
                         WHERE i.id IN(
                              SELECT t.image_id
                              FROM tag t
                              WHERE t.tag_name LIKE :tagNameWildCard%1$d
                         )
                         LIMIT :amount
                    )
               """.formatted(i));
          }

          /**
           * 현재 영업중인 가게
           */
          if (current != null && !current.isBlank()) {
               String[] timeAndDate = current.split(":");
               time = Integer.parseInt(timeAndDate[0]);
               int date = Integer.parseInt(timeAndDate[1]);
               int beforeDate = date == 6 ? 0 : (date-1);

               query.add("""
                    (
                         SELECT '영업중' as key, i.id
                         FROM image i
                         WHERE i.id IN(
                              SELECT s.user_id
                              FROM store s
                              WHERE (open%1$d < :time AND :time < close%1$d)
                                   OR (open%1$d < :nightTime AND :nightTime < close%1$d AND open%2$d != 0)
                                   OR (open%1$d = 9999)
                         )
                         LIMIT :amount
                    )
               """.formatted(date, beforeDate));
          }

          NativeQuery<Object[]> unionQuery = session.createNativeQuery(String.join("UNION ALL", query), Object[].class);

          unionQuery.setParameter("amount", amount);

          for (int i = 0; i < tagNames.size(); i++) {
               String parameterTagName = tagNames.get(i);
               unionQuery.setParameter("tagName" + i, parameterTagName);
               unionQuery.setParameter("tagNameWildCard" + i, "%" + parameterTagName + "%");
          }

          if (current != null && !current.isEmpty()) {
               unionQuery.setParameter("time", time);
               unionQuery.setParameter("nightTime", time+2400);
          }

          Map<String, List<ImageId>> results = unionQuery.list().stream()
               .map(e -> new QueryResult(
                    String.valueOf(e[0]),
                    (long) e[1]
               ))
               .collect(Collectors.groupingBy(
                    QueryResult::tagName,
                    Collectors.mapping(e -> new ImageId(e.id()), Collectors.toList())
               ));

          return results;
     }

     @Override
     public List<ImageResponse> findRandom(List<String> tagNames, List<ImageId> imageIds, String current, Boolean job,
               List<UserId> storeIds, boolean adult, int amount) {

          List<Long> allIds = jpaQueryFactory
               .selectDistinct(image.id)
               .from(image)
               .innerJoin(image.store, imageStore)
               .where(
                    image.detail.archived.isFalse(),
                    // image.job.eq(job),
                    (imageIds == null || imageIds.isEmpty()) ? null : image.id.notIn(imageIds.stream().map(e -> e.value()).toList()),
                    (storeIds == null || storeIds.isEmpty()) ? null : image.user.id.in(storeIds.stream().map(e -> e.value()).toList()),
                    (tagNames == null || tagNames.isEmpty()) ? null : hasTags(tagNames),
                    adult ? null : isAdultAndStoreOpen(adult, current)
               )
               .fetch();

          Collections.shuffle(allIds);
          amount = Math.min(allIds.size(), amount);

          return findByRawIds(allIds.subList(0, amount), false);
     }

     @Override
     public List<ImageResponse> findByType(ImageId lastImageId, boolean adult, String type, int amount) {
          List<Long> ids = jpaQueryFactory
               .selectDistinct(image.id)
               .from(image)
               .innerJoin(image.store, imageStore)
               .where(
                    lastImageId == null || lastImageId.value() == 0 ? null : image.id.lt(lastImageId.value()),
                    image.detail.archived.isFalse(),
                    image.type.eq(type),
                    adult ? null : imageStore.adult.isFalse()
               )
               .orderBy(image.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(ids, false);
     }

     @Override
     public List<ImageResponse> findByUserId(UserId userId, ImageId lastImageId, boolean isAdult, int amount) {
          List<Long> ids = jpaQueryFactory
               .select(image.id)
               .from(image)
               .innerJoin(image.store, imageStore)
               .where(
                    image.user.id.eq(userId.value()),
                    lastImageId == null ? null : image.id.lt(lastImageId.value()),
                    isAdult ? null : imageStore.adult.isFalse()
               )
               .orderBy(image.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(ids, isAdult);
     }

     @Override
     public List<ImageResponse> findStoreImages(UserId storeId, ImageId lastImageId, String type, boolean adult, int amount) {
          List<Long> query = jpaQueryFactory
               .selectDistinct(image.id)
               .from(image)
               .innerJoin(image.store, imageStore)
               .where(
                    lastImageId == null ? null : image.id.lt(lastImageId.value()),
                    image.detail.archived.isFalse(),
                    imageStore.id.eq(storeId.value()),
                    image.type.eq(type),
                    adult ? null : imageStore.adult.isFalse()
               )
               .orderBy(image.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(query, false);
     }

     @Override
     public List<ImageResponse> findUpdatableImages(UserId userId, ImageId lastImageId, int amount) {
          List<Long> query = jpaQueryFactory
               .selectDistinct(image.id)
               .from(image)
               .where(
                    lastImageId == null ? null : image.id.lt(lastImageId.value()),
                    image.detail.archived.isFalse(),
                    image.store.id.eq(userId.value())
               )
               .orderBy(image.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(query, false);
     }

     @Override
     public ImageResponse findRandomOne(boolean isAdult) {
          Long maxId = jpaQueryFactory
               .select(image.id.max())
               .from(image)
               .innerJoin(image.store, imageStore)
               .where(
                    image.detail.archived.isFalse(),
                    isAdult ? null : imageStore.adult.isFalse()
               )
               .fetchOne();

          return selectFromImage(null)
               .join(image.user, imageUser)
               .join(image.store, imageStore)
               .where(
                    image.id.goe(ThreadLocalRandom.current().nextLong(maxId)),
                    image.detail.archived.isFalse()
               )
               .orderBy(image.id.asc())
               .fetchFirst();
     }

     @Override
     public List<ImageResponse> findByIds(List<ImageId> imageIds, boolean shuffle) {
          return findByIds(imageIds, "", shuffle);
     }

     @Override
     public List<ImageResponse> findByIds(List<ImageId> imageIds, String referenceTagName, boolean shuffle) {
          if (referenceTagName == null) referenceTagName = "";

          return selectFromImage(referenceTagName)
               .join(image.user, imageUser)
               .join(image.store, imageStore)
               .where(
                    image.detail.archived.isFalse(),
                    image.id.in(imageIds.stream().map(e -> e.value()).toList())
               )
               .fetch();
     }

     @Override
     public List<ImageId> findIdsByUserId(UserId userId) {
          return jpaQueryFactory
               .select(image.id)
               .from(image)
               .where(
                    image.detail.archived.isFalse(),
                    image.user.id.eq(userId.value())
               )
               .fetch()
               .stream()
               .map(e -> new ImageId(e))
               .toList();
     }

     private List<ImageResponse> findByRawIds(List<Long> imageIds, boolean shuffle) {
          return selectFromImage(null)
               .join(image.user, imageUser)
               .join(image.store, imageStore)
               .where(image.id.in(imageIds))
               .fetch();
     }

     private BooleanExpression hasTags(List<String> tagList) {
          if (tagList.isEmpty())
               return null;

          QTag subTag = new QTag("subTag");

          return tagList.stream()
               .map(tagName -> image.id.in(
                    JPAExpressions
                         .select(subTag.image.id)
                         .from(subTag)
                         .where(subTag.tagName.contains(tagName))
               ))
               .reduce(BooleanExpression::and)
               .orElse(null);
     }

     private BooleanExpression isAdultAndStoreOpen(boolean isAdult, String current) {
          if (current == null)
               return isAdult ? null : imageStore.adult.isFalse();

          PathBuilder<ImageStore> pathBuilder = new PathBuilder<>(ImageStore.class, "imageStore");

          String[] timeAndDate = current.split(":");
          int time = Integer.parseInt(timeAndDate[0]);
          int date = Integer.parseInt(timeAndDate[1]);
          int beforeDate = date == 0 ? 6 : (date-1);

          NumberPath<Integer> openToday = pathBuilder.getNumber("openingHours.open" + date, Integer.class);
          NumberPath<Integer> closeToday = pathBuilder.getNumber("openingHours.close" + date, Integer.class);
          NumberPath<Integer> openBefore = pathBuilder.getNumber("openingHours.open" + beforeDate, Integer.class);

          // open < time < close
          BooleanExpression isStandardOpen = openToday.lt(time)
               .and(closeToday.gt(time));

          // open < time+2400 < close && openBefore.open != 0
          BooleanExpression isExtendedOpen = openToday.lt(time+2400)
               .and(closeToday.gt(time+2400))
               .and(openBefore.ne(0));

          // open == 9999
          BooleanExpression isAlwaysOpen = openToday.eq(9999);

          return isStandardOpen.or(isExtendedOpen).or(isAlwaysOpen);
     }

     private JPAQuery<ImageResponse> selectFromImage(String referenceTagName) {
          if (referenceTagName == null) referenceTagName = "";

          return jpaQueryFactory
               .select(Projections.constructor(ImageResponse.class,
                    image.id
                    , image.user.id
                    , image.store.id
                    , image.title
                    , image.price
                    , image.description
                    , image.type
                    , image.job
                    , Projections.constructor(ImageDetailResponse.class,
                         image.detail.filename
                         , image.detail.thumbnail
                         , image.detail.card
                         , image.detail.filter
                         , image.detail.isSquare
                         , image.detail.archived
                         , image.detail.createdDate
                    )
                    , Projections.constructor(ImageUserResponse.class,
                         imageUser.nickname
                         , imageUser.profile
                    )
                    , Projections.constructor(ImageStoreResponse.class,
                         imageStore.id
                         , imageStore.storeName
                         , imageStore.address
                         , imageStore.description
                         , imageStore.thumbnail
                         , imageStore.adult
                         , imageStore.poster
                         , imageStore.filter
                         , imageStore.openingHours
                    )
                    , Expressions.as(Expressions.constant(referenceTagName), "referenceTagName")
               ))
               .from(image);
     }
}
