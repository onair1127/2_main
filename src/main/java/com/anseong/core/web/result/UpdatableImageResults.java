package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;

public class UpdatableImageResults {

     public static ResponseEntity<ApiResult> build(List<ImageResponse> images) {
          List<Map<String, String>> tagLists = new ArrayList<>();
          List<Map<String, Object>> imageLists = new ArrayList<>();

          Set<String> tagTemp = new HashSet<>();

          for (ImageResponse i : images) {
               List<TagResponse> tagSet = i.tags();
               List<String> imageTags = new ArrayList<>();
               List<Map<String, Object>> multipleImages = new ArrayList<>();
               Map<String, Object> temp = new HashMap<>();

               for (TagResponse t : tagSet) {
                    if (!tagTemp.contains(t.tagName())) {
                         Map<String, String> tag = new HashMap<>();
                         tag.put("name", t.tagName());
                         tag.put("type", t.type());
                         tagLists.add(tag);
                         tagTemp.add(t.tagName());
                    }

                    imageTags.add(t.tagName());
               }

               if (i.multipleImages().size() > 0) {

                    List<MultipleImageResponse> imageSet = i.multipleImages();
                    Collections.sort(imageSet);

                    for (MultipleImageResponse m : imageSet) {
                         Map<String, Object> tmp = new HashMap<>();
                         tmp.put("thumbnail", m.detail().thumbnail());
                         tmp.put("card", m.detail().card());
                         tmp.put("loaded", false);
                         tmp.put("filter", m.detail().filter());
                         tmp.put("isSquare", m.detail().isSquare());
                         multipleImages.add(tmp);
                    }
               }

               temp.put("id", i.id().value());
               temp.put("title", i.title());
               temp.put("price", i.price());
               temp.put("card", i.detail().card());
               temp.put("thumbnail", i.detail().thumbnail());
               temp.put("description", i.description());
               temp.put("tags", imageTags);
               temp.put("filter", i.detail().filter());
               temp.put("multipleImages", multipleImages);
               temp.put("type", i.type());
               temp.put("isJob", i.job());
               temp.put("isSquare", i.detail().isSquare());

               imageLists.add(temp);
          }

          ApiResult apiResult = ApiResult.blank()
               .add("tags", tagLists)
               .add("images", imageLists);

          return Result.ok(apiResult);
	}

}
