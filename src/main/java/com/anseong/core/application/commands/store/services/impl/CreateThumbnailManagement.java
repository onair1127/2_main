package com.anseong.core.application.commands.store.services.impl;

import java.net.URI;

import org.apache.commons.io.FilenameUtils;

import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest.ConvertMode;

class CreateThumbnailManagement {

     static ConvertMediaRequest createThumbnailRequest(String thumbnailUrl, String nickname) {
          String filename = FilenameUtils.getName(URI.create(thumbnailUrl).getPath());
          String ext = FilenameUtils.getExtension(filename);

          return ConvertMediaRequest.builder()
               .convertMode(ConvertMode.PROFILE)
               .url(thumbnailUrl)
               .ext(ext)
               .filename(filename)
               .username(nickname)
               .build();
     }
}
