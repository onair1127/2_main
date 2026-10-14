package com.anseong.core.domain.model.tag.repository;

import java.util.List;
import java.util.Map;

public interface TagCache {

     Map<String, String> getRandomRecommendTags();

     void determineTag(List<String> tagLists);

     void addRecommendTag(String tag, String description);

     void deleteRecommendTag(String tag);

}
