package com.anseong.core.infrastructure.repository.hibernate.board_store;

import static com.anseong.core.domain.model.board_store.QBoardStore.boardStore;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.board_store.repository.BoardStoreCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateBoardStoreCommandRepository extends HibernateSupport<BoardStore> implements  BoardStoreCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateBoardStoreCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public void removeByBoardId(BoardId id) {
          jpaQueryFactory
               .delete(boardStore)
               .where(boardStore.board.id.eq(id.value()));
     }

     @Override
     public void removeByBoardIdAndUserId(BoardId boardId, UserId userId) {
          jpaQueryFactory
               .delete(boardStore)
               .where(boardStore.board.id.eq(boardId.value())
                    .and(boardStore.userId.eq(userId.value()))
               );
     }
}
