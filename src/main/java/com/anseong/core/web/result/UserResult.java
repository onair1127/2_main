package com.anseong.core.web.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.user.SimpleUser;

public class UserResult {

     static Logger log = LoggerFactory.getLogger(UserResult.class);

     public static ResponseEntity<ApiResult> build(SimpleUser user, boolean subscribeBoard, List<Integer> favoriteList, List<Integer> boardLikeList) {
          Map<String, Object> result = new HashMap<>();

          result.put("id", user.getUserId().value());
          result.put("username", user.getUsername());
          result.put("nickname", user.getNickname());
          result.put("role", user.getCeoId().value() != null ? true : false);
          result.put("profile", user.getProfile());
          result.put("logo", user.getLogo());
          result.put("imageSort", user.getImageSort());
          result.put("subscribeBoard", subscribeBoard);
          result.put("favoriteList", favoriteList);
          result.put("boardLikeList", boardLikeList);

          ApiResult apiResult = ApiResult.blank().add("user", result);

          return Result.ok(apiResult);
	}

}
