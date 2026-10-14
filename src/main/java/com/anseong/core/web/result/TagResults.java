package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.tag.Tag;

public class TagResults {

     public static ResponseEntity<ApiResult> build(List<Tag> tags) {
          List<String> tagName = new ArrayList<>();

          for (Tag t : tags) {
               if (tagName.contains(t.getTagName())) {
                    continue;
               }
               tagName.add(t.getTagName());
          }

          ApiResult apiResult = ApiResult.blank()
               .add("tags", tagName);

          return Result.ok(apiResult);
     }
}
