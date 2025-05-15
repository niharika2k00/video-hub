package com.application.springboot.controller;

import com.application.sharedlibrary.entity.Video;
import com.application.springboot.dto.VideoUploadRequestDto;
import com.application.springboot.exception.MissingFileException;
import com.application.springboot.service.VideoProcessingService;
import com.application.springboot.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoController {

  private final VideoService videoService;
  private final VideoProcessingService videoProcessingService;

  @Autowired
  public VideoController(VideoProcessingService videoProcessingService, VideoService videoService) {
    this.videoProcessingService = videoProcessingService;
    this.videoService = videoService;
  }

  // GET all /videos
  @GetMapping("/videos")
  public List<Video> findAll() {
    return videoService.findAll();
  }

  // GET /videos/{id}
  @GetMapping("/videos/{id}")
  public Video findById(@PathVariable int id) throws Exception {
    Video userDetails = videoService.findById(id);
    return userDetails;
  }

  // POST /video/upload
  @PostMapping("/video/upload")
  // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html
  public String uploadMedia(@ModelAttribute VideoUploadRequestDto reqBodyParams) throws Exception { // @RequestParam("file") MultipartFile file
    MultipartFile videoFile = reqBodyParams.getVideoFile();
    System.out.println("file name: " + videoFile.getOriginalFilename());

    if (videoFile == null || videoFile.isEmpty()) {
      throw new MissingFileException("File is required");
    }

    videoProcessingService.uploadVideo(reqBodyParams);

    return "Video uploaded and processing started successfully!";
  }

  // DELETE /videoId/id
  @DeleteMapping("/video/{id}")
  public String deleteVideo(@PathVariable int id) {
    videoService.deleteById(id);
    System.out.println("Successfully deleted video with id " + id);
    return "Successfully deleted video with id " + id;
  }
}
