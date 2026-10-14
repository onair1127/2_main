package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.image.Image;

public class ArchivedImageResults {

     public static ResponseEntity<ApiResult> build(List<Image> images) {
          ApiResult apiResult = ApiResult.blank();
          List<Map<String, Object>> results = new ArrayList<>();

          for (Image i : images) {
               Map<String, Object> temp = new HashMap<>();
               temp.put("id", i.getId().value());
               temp.put("thumbnail", i.getDetail().getThumbnail());
               temp.put("card", i.getDetail().getCard());

               results.add(temp);
          }

          apiResult.add("images", results);
          return Result.ok(apiResult);
     }
}
