package com.anseong.core.domain.model.report.repository;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.report.ReportImage;
import com.anseong.core.domain.model.report.ReportImageId;

public interface ReportImageCommandRepository {

     ReportImage findByImageId(ImageId imageId);

	void save(ReportImage report);

     void solve(ReportImageId id);
}
