package com.anseong.core.infrastructure.client.location.naver;

import java.util.List;

import com.anseong.core.domain.client.location.response.GeoCodeResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

final class ResponseBody {

     @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
     record Geocoding(
          String status,
          Meta meta,
          List<Address> addresses,
          String errorMessage
     ) implements NaverResponseBody<GeoCodeResponse> {
          @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
          record Meta(
               int totalCount,
               int page,
               int count
          ) {}

          @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
          record Address(
               String roadAddress,
               String jibunAddress,
               String englishAddress,
               List<AddressElement> addressElements,
               String x,
               String y,
               double distance
          ) {
               @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
               record AddressElement(
                    List<String> types,
                    String longName,
                    String shortName,
                    String code
               ) {}
          }

          @Override
          public GeoCodeResponse toDomainResponse() {
               Address address = addresses.get(0);
               return new GeoCodeResponse(
                    Long.parseLong(address.y()),
                    Long.parseLong(address.x())
               );
          }
     }

     @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
     @JsonIgnoreProperties(ignoreUnknown = true)
     record ReverseGeocoding(
          Status status,
          List<Result> results
     ) implements NaverResponseBody<String> {
          @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
          record Status(
               int code,
               String name,
               String message
          ) {}

          @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
          record Result(
               String name,
               Region region
          ) {
               @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
               record Region(
                   Area3 area3
               ) {
                    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
                    record Area3(
                         String name
                    ) {}
               }
          }

          @Override
          public String toDomainResponse() {
               String road = results.get(0)
                    .region()
                    .area3()
                    .name();

               String lot = "(" + results.get(1)
                    .region()
                    .area3()
                    .name() + ")";

               return road + lot;
          }
     }
}
