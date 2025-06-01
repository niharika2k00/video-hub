package com.application.sharedlibrary.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.exception.InvalidRequestException;

import java.util.List;

public interface VideoService {

  Video findById(int id) throws InvalidRequestException, IllegalArgumentException;

  List<Video> findByAuthorId(Integer id);

  List<Video> findAll();

  Video saveOrUpdate(Video video);

  void deleteById(int id);
}
