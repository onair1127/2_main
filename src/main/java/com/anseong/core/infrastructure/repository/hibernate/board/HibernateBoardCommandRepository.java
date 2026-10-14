package com.anseong.core.infrastructure.repository.hibernate.board;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;

import jakarta.persistence.EntityManager;

@Repository
class HibernateBoardCommandRepository extends HibernateSupport<Board> implements BoardCommandRepository {

     HibernateBoardCommandRepository(EntityManager entityManager) {
          super(entityManager);
     }

     @Override
     public Board getReferenceById(BoardId boardId) {
          return getReference(boardId.value());
     }

     @Override
     public Board findById(BoardId boardId) {
          return find(boardId.value());
     }

     @Override
     public void save(Board newBoard) {
          save(newBoard);
     }
}
