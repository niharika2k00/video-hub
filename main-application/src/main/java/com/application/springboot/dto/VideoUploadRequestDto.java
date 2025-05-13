package com.application.springboot.dto;

import org.springframework.web.multipart.MultipartFile;

public class VideoUploadRequestDto {
  String title;
  String description;
  String category;
  MultipartFile videoFile;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public MultipartFile getVideoFile() {
    return videoFile;
  }

  public void setVideoFile(MultipartFile videoFile) {
    this.videoFile = videoFile;
  }
}
