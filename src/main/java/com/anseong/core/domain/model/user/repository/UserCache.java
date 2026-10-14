package com.anseong.core.domain.model.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anseong.core.domain.model.user.UserId;

public interface UserCache {

     /**
      * 사용자 FCM 토큰 업데이트
      * 토큰이 존재하면 토큰만 업데이트, 존재하지 않으면 추가
      * 토큰 존재 여부를 리턴하며, 그 값으로 신규 기기 구독 로직 활용
      * @param userId
      * @param key
      * @param ceo
      */
     boolean addUserFcmToken(UserId userId, String token, boolean isCeo, boolean adult);

     /**
      * <p>ID값으로 사장님 토큰 불러오기</p>
      * <p>대부분은 운영자 소식에서 가게 언급때 호출됨</p>
      * @param ids
      * @return FCM Tokens
      */
     Set<String> getCeoFcmTokenByIds(Collection<UserId> ids);

     /**
      * <p>사용자 FCM 토큰 삭제</p>
      * <p>대부분은 회원 탈퇴때 호출됨</p>
      * @param userId
      * @param isCeo
      *
      * @return 삭제된 사용자 토큰
      */
     String deleteUserFcmToken(UserId userId, boolean isCeo);

     /**
      * <p>운영자 소식 구독 설정</p>
      * @param userId
      * @param token
      * @param isCeo
      */
     void subscribeBoard(String token);

     /**
      * <p>운영자 소식 구독 취소</p>
      * @param userId
      * @param token
      * @param isCeo
      */
     void unsubscribeBoard(String token);

     /**
      * <p>운영자 소식 구독 현황 안내용 메소드</p>
      * <p>Set(subscribe:board)에 값이 있으면 true, 없으면 false를 반환</p>
      * @return
      */
     boolean getSubscribeBoardStatus(String token);

     /**
      * <p>회원 캐시 데이터 불러오기</p>
      * <p>현재는 favoriteList, boardLikeList를 return함.</p>
      * @return
      */
     Map<String, List<Integer>> getUserData(UserId userId);

     void setImageSort(UserId userId, int imageSort);

     int getImageSort(UserId userId);


     /**
      * <p>비회원에서 회원으로 전환한 유저의 데이터 적재</p>
      * <p>현재는 favoriteList, boardLikeList를 적재함.</p>
      * @return
      */
     void addUserDataFromGuestData(Collection<String> favoriteList, Collection<String> boardLikeList, Collection<String> searchHistory, int imageSort, UserId userId);
}
