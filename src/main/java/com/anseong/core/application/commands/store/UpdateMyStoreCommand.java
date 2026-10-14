package com.anseong.core.application.commands.store;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.URLEncoder;

import com.anseong.core.application.commands.sns.SnsCommand;

public record UpdateMyStoreCommand(
     String name,
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
     boolean thumbnailCreated,
     String filter,
     String menuLists,
     List<SnsCommand> snsCommands,
     Map<String, String> openingHours
) {
     public static UpdateMyStoreCommand from(String name, String phone, String address, double lat, double lng, String description, String announcement, String thumbnail, List<Integer> breakDates, int breakStart, int breakEnd, boolean adult, boolean thumbnailCreated, String filter, String menuLists, List<Map<String, String>> addSns, Map<String, String> openingHours) {
          return new UpdateMyStoreCommand(
               name
               , phone
               , address
               , lat
               , lng
               , URLEncoder.DEFAULT.encode(description, Charset.forName("utf-8"))
               , URLEncoder.DEFAULT.encode(announcement, Charset.forName("utf-8"))
               , thumbnail
               , breakDates
               , breakStart
               , breakEnd
               , adult
               , thumbnailCreated
               , filter
               , menuLists
               , addSns.stream()
                    .map(e -> new SnsCommand(e.get("type"), e.get("url")))
                    .toList()
               , openingHours
          );
     }
}
