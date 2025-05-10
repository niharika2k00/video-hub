package com.application.sharedlibrary.service;

import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.entity.VideoVariantId;
import com.application.sharedlibrary.exception.InvalidRequestException;

public interface VideoVariantService {

  VideoVariant saveOrUpdate(VideoVariant video);

  VideoVariant findById(VideoVariantId id) throws InvalidRequestException;

  int getCountByVideoId(int id);
}
