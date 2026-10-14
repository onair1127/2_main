package com.anseong.core.infrastructure.client.certification.iamport;

interface IamportResponseBody {

}

interface BaseIamportResponseBody<T> extends IamportResponseBody {
     /**
      * 포트원 거래고유번호
      * 결제건의 포트원 거래고유번호
      */
     String impUid();

     /**
      * 고객사 주문번호
      * 결제건의 고객사 주문번호
      */
     String merchantUid();

     /**
      * 인증된 사용자의 성명
      */
     String name();

     /**
      * 생년월일
      * ISO8601 형식의 문자열. YYYY-MM-DD 10자리 문자열
      */
     String birthday();


     /**
      * 도메인 객체로 반환합니다.
      * @return
      */
     T toDomainResponse();
}
