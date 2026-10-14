package com.anseong.core.web.result;

import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.user.UserId;

public class BindTimeResult {

	public static ResponseEntity<ApiResult> build(List<LocalTime> timeList, UserId userId) {
          ApiResult apiResult = ApiResult.blank()
                    .add("time", timeList)
                    .add("userId", userId.value());

          return Result.ok(apiResult);
	}

}
