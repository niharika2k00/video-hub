package com.application.sharedlibrary.service;

import com.application.sharedlibrary.dao.VideoVariantRepository;
import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.entity.VideoVariantId;
import com.application.sharedlibrary.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoVariantServiceImpl implements VideoVariantService {

  private final VideoVariantRepository videoVariantRepository;

  @Autowired
  public VideoVariantServiceImpl(VideoVariantRepository videoVariantRepository) {
    this.videoVariantRepository = videoVariantRepository;
  }

  @Override
  public VideoVariant saveOrUpdate(VideoVariant video) {
    return videoVariantRepository.save(video);
  }

  @Override
  public VideoVariant findById(VideoVariantId id) throws InvalidRequestException {
    VideoVariant item;
    Optional<VideoVariant> optionalVariant = videoVariantRepository.findById(id);

    if (optionalVariant.isPresent()) {
      item = optionalVariant.get();
    } else {
      throw new InvalidRequestException("Video variant not found. Please verify the ID and try again.");
    }

    return item;
  }

  @Override
  public int getCountByVideoId(int id) {
    return videoVariantRepository.getCountByVideoId(id);
  }
}
