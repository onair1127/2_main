package com.anseong.core.infrastructure.repository.hibernate.board_store;

import static com.anseong.core.domain.model.board.QBoard.board;
import static com.anseong.core.domain.model.board_store.QBoardStore.boardStore;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.repository.BoardStoreQueryRepository;
import com.anseong.core.domain.model.board_store.response.BoardStoreResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateBoardStoreQueryRepository implements BoardStoreQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateBoardStoreQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<BoardStoreResponse> findByBoardId(BoardId boardId) {
          return jpaQueryFactory
               .select(Projections.constructor(BoardStoreResponse.class,
                    boardStore.userId,
                    boardStore.board.id,
                    boardStore.storeName
               ))
               .from(boardStore)
               .innerJoin(boardStore.board, board)
               .where(board.id.eq(boardId.value()))
               .fetch();
     }

     @Override
     public List<BoardStoreResponse> findByBoardId(List<BoardId> boardIds) {
          return jpaQueryFactory
               .select(Projections.constructor(BoardStoreResponse.class,
                    boardStore.userId,
                    boardStore.board.id,
                    boardStore.storeName
               ))
               .from(boardStore)
               .innerJoin(boardStore.board, board)
               .where(board.id.in(boardIds.stream().map(e -> e.value()).toList()))
               .fetch();
     }
}
