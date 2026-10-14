package com.anseong.core.application.commands.sns;

import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.user.UserId;

public record AdminAddSnsCommand(
     UserId storeId,
     List<SnsCommand> snsCommands
) {
     public AdminAddSnsCommand(
          long storeId
          , List<Map<String, String>> snsData
     ) {
          this(
               new UserId(storeId),
               snsData.stream()
                    .map(e -> new SnsCommand(e.get("type"), e.get("url")))
                    .toList()
          );
     }
}
