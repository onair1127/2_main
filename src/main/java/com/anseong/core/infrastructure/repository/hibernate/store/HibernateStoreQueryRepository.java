package com.anseong.core.infrastructure.repository.hibernate.store;

import static com.anseong.core.domain.model.store.QSearchBoardStore.searchBoardStore;
import static com.anseong.core.domain.model.store.QSearchStore.searchStore;
import static com.anseong.core.domain.model.store.QStore.store;
import static com.anseong.core.domain.model.store.QStoreGeo.storeGeo;
import static com.anseong.core.domain.model.tag.QStoreTag.storeTag;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.store.OpeningHours;
import com.anseong.core.domain.model.store.repository.StoreQueryRepository;
import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.tag.QTag;
import com.anseong.core.domain.model.user.UserId;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateStoreQueryRepository implements StoreQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateStoreQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public StoreResponse findByUserId(UserId userId) {
          return jpaQueryFactory
               .select(Projections.constructor(StoreResponse.class,
                    store.userId
                    , store.storeName
                    , store.bizNo
                    , store.phone
                    , store.address
                    , store.description
                    , store.announcement
                    , store.lat
                    , store.lng
                    , store.thumbnail
                    , store.createdDate
                    , store.breakDates
                    , store.breakStart
                    , store.breakEnd
                    , store.adult
                    , store.filter
                    , store.poster
                    , store.menuLists
                    , store.openingHours
               ))
               .from(store)
               .where(store.userId.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public List<StoreGeoResponse> findStoreGeosByUserIds(List<UserId> userIds, List<String> tagNames, String current, boolean isAdult) {
          List<Long> ids = jpaQueryFactory
               .select(storeGeo.userId)
               .from(storeGeo)
               .where(
                    storeGeo.userId.in(userIds.stream().map(e -> e.value()).toList()),
                    hasTagsInStoreGeo(tagNames),
                    isAdultAndStoreOpenInStoreGeo(isAdult, current)
               )
               .fetch();

          return jpaQueryFactory
               .select(Projections.constructor(StoreGeoResponse.class,
                    storeGeo.userId
                    , storeGeo.storeName
                    , storeGeo.thumbnail
                    , storeGeo.filter
                    , storeGeo.poster
                    , storeGeo.lat
                    , storeGeo.lng
               ))
               .from(storeGeo)
               .where(storeGeo.userId.in(ids))
               .fetch();
     }

     @Override
     public List<SearchStoreResponse> findByKeyword(String keyword, UserId lastUserId, boolean isAdult, int amount) {
          StringExpression replaceStoreName = Expressions.stringTemplate(
               "REPLACE({0}, ' ', '')",
               searchStore.storeName
          );

          List<Long> ids = jpaQueryFactory
               .selectDistinct(searchStore.userId)
               .from(searchStore)
               .where(
                    lastUserId == null ? null : searchStore.userId.lt(lastUserId.value()),
                    searchStore.userId.in(
                         JPAExpressions
                              .select(storeTag.userId)
                              .from(storeTag)
                              .where(storeTag.tagName.contains(keyword))
                    )
                    .or(replaceStoreName.contains(keyword).and(searchStore.storeName.ne("안성상회")))
               )
               .orderBy(searchStore.userId.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(ids, false);
     }

     @Override
     public List<SearchStoreResponse> findRandom(boolean isAdult, int amount) {
          List<Long> allIds = jpaQueryFactory
               .selectDistinct(searchStore.userId)
               .from(searchStore)
               .where(isAdult ? null : searchStore.adult.isFalse())
               .fetch();

          Collections.shuffle(allIds);
          amount = Math.min(allIds.size(), amount);

          return findByRawIds(allIds.subList(0, amount), true);
     }

     @Override
     public List<String> findStoreNames(String keyword, boolean adult, int amount) {
          StringExpression replaceStoreName = Expressions.stringTemplate(
               "REPLACE({0}, ' ', '')",
               searchStore.storeName
          );

          return jpaQueryFactory
               .select(searchStore.storeName)
               .from(searchStore)
               .where(
                    replaceStoreName.contains(keyword),
                    searchStore.storeName.ne("안성상회")
               )
               .limit(amount)
               .fetch();
     }

     @Override
     public List<SearchBoardStoreResponse> findBoardStoresByName(String storeName) {
          StringExpression replaceStoreName = Expressions.stringTemplate(
               "REPLACE({0}, ' ', '')",
               searchBoardStore.storeName
          );

          return jpaQueryFactory
               .select(Projections.constructor(SearchBoardStoreResponse.class,
                    searchBoardStore.userId
                    , searchBoardStore.storeName
               ))
               .from(searchBoardStore)
               .where(replaceStoreName.contains(storeName))
               .fetch();
     }

     @Override
     public StoreForCertificateCardResponse findStoreForCertificateCardByUserId(UserId userId) {
          return jpaQueryFactory
               .select(Projections.constructor(StoreForCertificateCardResponse.class,
                    store.userId,
                    store.storeName,
                    store.phone,
                    store.address
               ))
               .from(store)
               .where(store.userId.eq(userId.value()))
               .fetchFirst();
     }

     private BooleanExpression hasTagsInStoreGeo(List<String> tagNames) {
          if (tagNames == null || tagNames.isEmpty())
               return null;

          QTag subTag = new QTag("subTag");

          return tagNames.stream()
               .map(tagName -> storeGeo.userId.in(
                    JPAExpressions
                         .select(subTag.userId)
                         .from(subTag)
                         .where(subTag.tagName.contains(tagName))
               ))
               .reduce(BooleanExpression::and)
               .orElse(null);
     }

     private BooleanExpression isAdultAndStoreOpenInStoreGeo(boolean isAdult, String current) {
          if (current == null || current.isBlank())
               return isAdult ? null : storeGeo.adult.isFalse();

          PathBuilder<OpeningHours> pathBuilder = new PathBuilder<>(OpeningHours.class, "openingHours");

          String[] timeAndDate = current.split(":");
          int time = Integer.parseInt(timeAndDate[0]);
          int date = Integer.parseInt(timeAndDate[1]);
          int beforeDate = date == 6 ? 0 : (date-1);

          NumberPath<Integer> openToday = pathBuilder.getNumber("open" + date, Integer.class);
          NumberPath<Integer> closeToday = pathBuilder.getNumber("close" + date, Integer.class);
          NumberPath<Integer> openBeforeDay = pathBuilder.getNumber("open" + beforeDate, Integer.class);

          // open < time < close
          BooleanExpression isStandardOpen = openToday.lt(time).and(closeToday.gt(time));

          // open < time+2400 < close && openBefore != 0
          BooleanExpression isExtendedOpen = openToday.lt(time+2400)
               .and(closeToday.gt(time+2400))
               .and(openBeforeDay.ne(0));

          // open == 9999;
          BooleanExpression isAlwaysOpen = openToday.eq(9999);

          return isStandardOpen.or(isExtendedOpen).or(isAlwaysOpen);
     }

     private List<SearchStoreResponse> findByRawIds(List<Long> ids, boolean shuffle) {
          List<SearchStoreResponse> responses = jpaQueryFactory
               .select(Projections.constructor(SearchStoreResponse.class,
                    searchStore.userId
                    , searchStore.storeName
                    , searchStore.address
                    , searchStore.thumbnail
                    , searchStore.poster
                    , searchStore.breakDates
                    , searchStore.breakStart
                    , searchStore.breakEnd
                    , searchStore.adult
                    , searchStore.openingHours
               ))
               .from(searchStore)
               .where(searchStore.userId.in(ids))
               .fetch();

          if (shuffle)
               Collections.shuffle(responses);

          return responses;
     }
}
