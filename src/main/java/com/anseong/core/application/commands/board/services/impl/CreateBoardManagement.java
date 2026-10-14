package com.anseong.core.application.commands.board.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.util.URLEncoder;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.domain.client.convert.media.ConvertMediaPlatform;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.exception.GlobalAnseongappException;

final class CreateBoardManagement {

     private static final ConvertMediaPlatform CONVERT_MEDIA_PLATFORM = ConvertMediaPlatform.LAMBDA;
     private static final URLEncoder URL_ENCODER = URLEncoder.DEFAULT;
     private static final Charset UTF_8 = StandardCharsets.UTF_8;
     private static final String BOARD_PATH = "anseongapp-board-images-all/";


     private CreateBoardManagement() {}

     static List<ConvertMediaRequest> createBoardImageRequests(UploadBoardCommand command) {
          List<ConvertMediaRequest> requests = new ArrayList<>();
          String username = BOARD_PATH + command.createdDate();

          for (String src : command.imageSrc()) {
               String path = URI.create(src).getPath();
               String filename = FilenameUtils.getBaseName(path);
               String ext = FilenameUtils.getExtension(filename);
               String decodedFilename = URLDecoder.decode(filename, UTF_8);

               ConvertMediaRequest request = ConvertMediaRequest.builder()
                    .platform(CONVERT_MEDIA_PLATFORM)
                    .url(src)
                    .ext(ext)
                    .filename(decodedFilename)
                    .username(username)
                    .build();

               requests.add(request);
          }

          return requests;
     }

     static String replaceMediaOriginalSrcToConvertedSrcFromContents(List<String> originalSrc, List<ConvertMediaResponse> convertedSrc, String contents) {
          if (originalSrc.size() != convertedSrc.size())
               throw new GlobalAnseongappException(String.format("원본 미디어 소스와 컨버팅한 미디어 소스 갯수 불일치 [original : %s개, converted: %s개]",
                    originalSrc.size(), convertedSrc.size()
               ));

          for (int i = 0; i < originalSrc.size(); i++) {
               String a = URL_ENCODER.encode(originalSrc.get(i), UTF_8);
               String b = URL_ENCODER.encode(convertedSrc.get(i).card(), UTF_8);

               contents = contents.replace(a, b);
          }

          return contents;
     }

     static String replaceThumbnail(String selectedThumbnail, List<ConvertMediaResponse> convertedSrc, String contents) {
          if (selectedThumbnail == null) {
               Document document;
               try {
                    document = Jsoup.parse(URLDecoder.decode(contents, "UTF-8"), "UTF-8");
               } catch (UnsupportedEncodingException e) {
                    throw new GlobalAnseongappException("게시판 컨텐츠 Parsing 실패 : " + e.getMessage(), e.getCause());
               }

               return document.select("img").first().attributes().get("src");

          } else {
               String selectedThumbnailFilename = FilenameUtils.getBaseName(URI.create(selectedThumbnail).getPath());
               String replaceThumbnail = selectedThumbnail;

               for (ConvertMediaResponse src : convertedSrc) {
                    String srcFilename = FilenameUtils.getBaseName(URI.create(src.thumbnail()).getPath()).replace(".thumb", "");

                    if (selectedThumbnailFilename.equals(srcFilename)) {
                         replaceThumbnail = src.thumbnail();
                    }
               }

               if (replaceThumbnail.equals(selectedThumbnail))
                    throw new GlobalAnseongappException("게시판 섬네일의 원본 소스를 컨버팅된 소스로 교체 실패.");

               return replaceThumbnail;
          }
     }
}
