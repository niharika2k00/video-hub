package com.application.sharedlibrary.service;

import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.entity.VideoVariantId;
import com.application.sharedlibrary.exception.InvalidRequestException;

import java.util.List;

public interface VideoVariantService {

  VideoVariant saveOrUpdate(VideoVariant video);

  VideoVariant findById(VideoVariantId id) throws InvalidRequestException;

  List<VideoVariant> findByVideoId(int videoId) throws InvalidRequestException;

  int getCountByVideoId(int id);
}
