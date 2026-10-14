package com.anseong.core.infrastructure.repository.hibernate.board;

import static com.anseong.core.domain.model.board.QBoard.board;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardQueryRepository;
import com.anseong.core.domain.model.board.response.BoardResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateBoardQueryRepository implements BoardQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateBoardQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<BoardResponse> find(BoardId lastBoardId, boolean isCeo, boolean isAdult, int amount) {
          List<Long> ids = jpaQueryFactory
               .select(board.id)
               .from(board)
               .where(
                    lastBoardId == null ? null : board.id.lt(lastBoardId.value()),
                    isCeo ? null : board.category.ne("사장님"),
                    isAdult ? null : board.adult.isFalse()
               )
               .orderBy(board.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(ids);
     }

     @Override
     public BoardResponse findById(BoardId boardId) {
          return jpaQueryFactory
               .select(Projections.constructor(BoardResponse.class,
                    board.id,
                    board.userId,
                    board.thumbnail,
                    board.title,
                    board.category,
                    board.contents,
                    board.adult,
                    board.createdDate
               ))
               .from(board)
               .where(board.id.eq(boardId.value()))
               .fetchOne();
     }

     private List<BoardResponse> findByRawIds(Collection<Long> ids) {
          return jpaQueryFactory
               .select(Projections.constructor(BoardResponse.class,
                    board.id,
                    board.userId,
                    board.thumbnail,
                    board.title,
                    board.category,
                    board.contents,
                    board.adult,
                    board.createdDate
               ))
               .from(board)
               .where(board.id.in(ids))
               .fetch();
     }
}
