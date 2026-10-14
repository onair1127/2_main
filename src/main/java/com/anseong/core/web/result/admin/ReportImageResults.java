package com.anseong.core.web.result.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;

import org.springframework.http.ResponseEntity;

public class ReportImageResults {

     public static ResponseEntity<ApiResult> build(List<ReportImageResponse> images) {

          List<Map<String, Object>> results = new ArrayList<>();
          List<MultipleImageResponse> multipleImageSet;
          List<TagResponse> tagSet;
          List<Map<String, Object>> multipleImages;
          List<String> tagNames;

          for (ReportImageResponse i : images) {
               Map<String, Object> temp = new HashMap<>();
               ImageResponse img = i.image();

               multipleImageSet = img.multipleImages();
               multipleImages = new ArrayList<>();
               tagNames = new ArrayList<>();
               tagSet = img.tags();

               Collections.sort(multipleImageSet);

               for (TagResponse t : tagSet) {
                    tagNames.add(t.tagName());
               }

               if (multipleImageSet.size() != 0) {
                    for (MultipleImageResponse s : multipleImageSet) {
                         Map<String, Object> tmp = new LinkedHashMap<>();
                         tmp.put("card", s.detail().card());
                         multipleImages.add(tmp);
                    }
               }

               temp.put("reportId", i.id().value());
               temp.put("userId", i.userId().value());
               temp.put("content", i.content());
               temp.put("createdDate", i.createdDate());
               temp.put("count", i.reporters().split(", ").length);

               temp.put("id", img.id().value());
               temp.put("title", img.title());
               temp.put("description", img.description());
               temp.put("card", img.detail().card());
               temp.put("type", img.type());

               temp.put("storeName", img.store().storeName());

               temp.put("tags", tagNames);
               temp.put("multipleImages", multipleImages);

               results.add(temp);
          }

          ApiResult apiResult = ApiResult.blank()
                    .add("images", results);

          return Result.ok(apiResult);
     }
}
