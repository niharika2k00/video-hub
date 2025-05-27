package com.application.sharedlibrary.service;

import com.application.sharedlibrary.dao.VideoVariantRepository;
import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.entity.VideoVariantId;
import com.application.sharedlibrary.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoVariantServiceImpl implements VideoVariantService {

  private final VideoVariantRepository videoVariantRepository;

  @Autowired
  public VideoVariantServiceImpl(VideoVariantRepository videoVariantRepository) {
    this.videoVariantRepository = videoVariantRepository;
  }

  @Override
  public VideoVariant saveOrUpdate(VideoVariant variant) {
    return videoVariantRepository.save(variant);
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
  public List<VideoVariant> findByVideoId(int videoId) throws InvalidRequestException {
    List<VideoVariant> variants = videoVariantRepository.findByVideoId(videoId);

    if (variants == null || variants.isEmpty())
      throw new InvalidRequestException("Video variant not found. Please verify the ID and try again.");

    return variants;
  }

  @Override
  public int getCountByVideoId(int videoId) {
    return videoVariantRepository.getCountByVideoId(videoId);
  }
}
