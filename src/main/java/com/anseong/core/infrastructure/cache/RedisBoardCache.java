package com.anseong.core.infrastructure.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardCache;
import com.anseong.core.domain.model.board.response.BoardResponse;
import com.anseong.core.domain.model.user.UserId;

@Repository
public class RedisBoardCache implements BoardCache {

     private final StringRedisTemplate redisTemplate;

     public RedisBoardCache(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     /**
      * 운영자 소식의 좋아요, 조회수 가져오기
      * @param boards
      * @return
      */
     @Override
     public List<BoardResponse> setBoardViewsAndLikes(List<BoardResponse> boards) {
          final ValueOperations<String, String> valueOerations = redisTemplate.opsForValue();
          final List<String> mGetCommands = new ArrayList<>();

          for (BoardResponse board : boards) {
               mGetCommands.add("view:board:" + board.id().value());
               mGetCommands.add("like:board:" + board.id().value());
          }

          List<String> mGetResults = valueOerations.multiGet(mGetCommands);

          if (mGetResults == null || mGetResults.isEmpty())
               return boards;

          else {
               List<BoardResponse> boardResults = new ArrayList<>();

               int index = 0;
               for (BoardResponse board : boards) {
                    int viewIndex = index*2;
                    int likeIndex = viewIndex+1;

                    index++;
                    boardResults.add(board.withViewsAndLikes(viewIndex, likeIndex));
               }

               return boardResults;
          }
     }

     /**
      * 운영자 소식 조회수 추가
      * @param boardId
      */
     @Override
     @Async
     public void addBoardView(BoardId boardId) {
          final ValueOperations<String, String> operations = redisTemplate.opsForValue();

          operations.increment("view:board:" + boardId.value());
     }

     /**
      * 운영자 소식 좋아요 추가
      * @param boardId
      * @param userId
      */
     @Async
     public void addBoardLike(BoardId boardId, UserId userId) {
          final ValueOperations<String, String> operations = redisTemplate.opsForValue();

          operations.increment("like:board:" + boardId.value());

          if (userId.value() != 0) {
               SetOperations<String, String> setOperations = redisTemplate.opsForSet();
               setOperations.add("user:likeBoard:" + userId.value(), boardId.value().toString());
          }
     }

     /**
      * 운영자 소식 좋아요 취소
      * @param boardId
      * @param userId
      */
     @Async
     public void removeBoardLike(BoardId boardId, UserId userId) {
          final ValueOperations<String, String> operations = redisTemplate.opsForValue();

          operations.decrement("like:board:" + boardId.value());

          if (userId.value() != 0) {
               SetOperations<String, String> setOperations = redisTemplate.opsForSet();
               setOperations.add("user:likeBoard:" + userId.value(), boardId.value().toString());
          }
     }

     /**
      * 운영자 소식 좋아요, 조회수 삭제
      * @param boardId
      */
     @Override
     public void deleteBoard(BoardId boardId) {
          redisTemplate.delete("like:board:" + boardId.value());
          redisTemplate.delete("view:board:" + boardId.value());
     }
}
