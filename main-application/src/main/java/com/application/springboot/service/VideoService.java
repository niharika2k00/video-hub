package com.application.springboot.service;

import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.exception.InvalidRequestException;

import java.util.List;

public interface VideoService {
  Video findById(int id) throws InvalidRequestException, IllegalArgumentException;

  List<Video> findAll();

  Video saveOrUpdate(Video video);
}
