package com.anseong.core.infrastructure.client.convert.media;

import java.util.HashSet;
import java.util.Set;

import com.anseong.core.domain.client.convert.media.exception.ExtensionNotSupportedException;

public abstract class ConvertMediaSupport {
     private static final Set<String> SUPPORTED_EXTENSIONS = new HashSet<>();
     private static final Set<String> IMAGE = new HashSet<>();
     private static final Set<String> VIDEO = new HashSet<>();

     static {
          IMAGE.add("JPG");
          IMAGE.add("JPEG");
          IMAGE.add("PNG");
          IMAGE.add("WEBP");
          IMAGE.add("HEIC");

          VIDEO.add("MPEG");
          VIDEO.add("WEBM");
          VIDEO.add("MPG");
          VIDEO.add("MP4");
          VIDEO.add("MOV");
          VIDEO.add("WMV");
          VIDEO.add("AVI");
          VIDEO.add("GIF");

          SUPPORTED_EXTENSIONS.addAll(IMAGE);
          SUPPORTED_EXTENSIONS.addAll(VIDEO);
     }

     public String mediaType(String ext) {
          ext = ext.toUpperCase();

          if (!SUPPORTED_EXTENSIONS.contains(ext))
               throw new ExtensionNotSupportedException("지원하지 않는 확장자 입니다.");

          if (VIDEO.contains(ext))
               return "video";
          else
               return "image";
     }
}
