package com.anseong.core.web.result;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.board.response.BoardResponse;
import com.anseong.core.domain.model.board_store.response.BoardStoreResponse;

public class BoardResults {

     public static ResponseEntity<ApiResult> build(List<BoardResponse> boardResponses) {
          List<Map<String, Object>> results = new ArrayList<>();

          for (BoardResponse board : boardResponses) {
               Map<String, Object> temp = new HashMap<>();
               List<Map<String, Object>> stores = new ArrayList<>();
               Map<String, Object> storeData = new HashMap<>();

               for (BoardStoreResponse bs : board.boardStores()) {
                    storeData = new HashMap<>();
                    storeData.put("id", bs.userId().value());
                    storeData.put("name", bs.storeName());
                    stores.add(storeData);
               }

               temp.put("id", board.id().value());
               temp.put("userId", board.userId().value());
               temp.put("thumbnail", board.thumbnail());
               temp.put("title", board.title());
               temp.put("category", board.category());
               temp.put("contents", board.contents());
               // temp.put("createdDate", b.getCreatedDate().toString());
               temp.put("createdDate", board.createdDate().format(DateTimeFormatter.ofPattern("yy.MM.dd | HH:mm:ss")));
               temp.put("views", board.views());
               temp.put("likes", board.likes());
               temp.put("adult", board.adult());
               temp.put("stores", stores);

               results.add(temp);
          }

          ApiResult apiResult = ApiResult
                                   .blank()
                                   .add("boards", results);

          return Result.ok(apiResult);
     }
}
