package com.anseong.core.application.commands.store;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.URLEncoder;

public record MakeOwnStoreCommand(
     String name,
     long bizNo,
     String phone,
     String address,
     double lat,
     double lng,
     String description,
     String announcement,
     String thumbnail,
     List<Integer> breakDates,
     int breakStart,
     int breakEnd,
     boolean adult,
     List<String> addSns,
     String filter,
     String menuLists,
     Map<String, String> openingHours
) {
     public MakeOwnStoreCommand {
          description = URLEncoder.DEFAULT.encode(description, StandardCharsets.UTF_8);
          announcement = URLEncoder.DEFAULT.encode(announcement, StandardCharsets.UTF_8);
     }
}
