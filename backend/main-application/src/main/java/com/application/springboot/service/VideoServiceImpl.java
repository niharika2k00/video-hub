package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.exception.InvalidRequestException;
import com.application.springboot.dao.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImpl implements VideoService {

  private final VideoRepository videoRepository;

  @Autowired
  public VideoServiceImpl(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

  @Override
  public Video findById(int id) throws InvalidRequestException, IllegalArgumentException {
    Video videoData;
    Optional<Video> optionalVideo = videoRepository.findById(id);

    if (optionalVideo.isPresent()) {
      videoData = optionalVideo.get();
    } else {
      throw new InvalidRequestException("Video with the specified ID (" + id + ") was not found. Please verify the ID and try again.");
    }

    return videoData;
  }

  @Override
  public List<Video> findByAuthorId(Integer id) {
    return videoRepository.findByAuthorId(id);
  }

  @Override
  public List<Video> findAll() {
    System.out.println("video count: " + videoRepository.count());
    return videoRepository.findAll();
  }

  @Override
  @Transactional
  public Video saveOrUpdate(Video video) {
    return videoRepository.save(video);
  }

  @Override
  @Transactional
  public void deleteById(int id) {
    videoRepository.deleteById(id);
  }
}
