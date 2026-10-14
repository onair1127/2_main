package com.anseong.core.domain.model.store.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anseong.core.domain.model.user.UserId;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface StoreCache {

     void addSearchHistory(UserId userId, String keyword);

     List<String> getSearchHistory(UserId userId);

     void deleteSearchHistory(UserId userId, @NotNull @NotEmpty String history);

     void createStore(UserId userId, String storeName, List<String> addSns, double lat, double lng);

     Set<String> getArchivedSnsIds();

     List<String> getArchivedSns(UserId storeId);

     void completeSnsJob(UserId id);

     void deleteStore(UserId userId);

     /**
      * <h2>좌표를 통한 내 주변의 가게 ID값 가져오기.</h2>
      * <p>주변에 가게가 없을 경우 null을 리턴함.</p>
      * @param lat
      * @param lng
      * @return
      */
     List<UserId> getGeoStoreIds(Double lat, Double lng);

     void updateGeoLocation(UserId userId, double lat, double lng);

     /**
      * <h2>가게 노출수 업카운트<h2>
      * <p>앱에서 (추후에) 가게 방문수 통계에 반영할 가게 클릭당 업카운트 로직</p>
      * @param store
      */
     void incrementClickStoreCount(UserId storeId);

     /**
      * <h2>가게 주변 위치 재설정(어드민 전용)</h2>
      *
      * @param dataSet
      */
     void restoreGeoStore(List<Map<String, Object>> dataSet);

     void incrementClickSnsCount(UserId storeId, String type);
}
