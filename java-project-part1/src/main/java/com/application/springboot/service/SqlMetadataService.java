package com.application.springboot.service;

import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.entity.Video;
import com.application.sharedlibrary.service.UserService;
import com.application.springboot.dto.VideoUploadRequestDto;
import com.application.springboot.utility.AuthenticatedUserLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service("SqlMetadataService") // necessary for qualifier in controller
public class SqlMetadataService implements MetadataService<VideoUploadRequestDto, Integer> {

  private final AuthenticatedUserLogger authenticatedUserLogger;
  private final UserService userService;
  private final VideoService videoService;

  @Autowired
  public SqlMetadataService(AuthenticatedUserLogger authenticatedUserLogger, UserService userService, VideoService videoService) {
    this.authenticatedUserLogger = authenticatedUserLogger;
    this.userService = userService;
    this.videoService = videoService;
  }

  @Override
  public Optional<Integer> saveMetadata(VideoUploadRequestDto reqBodyParams) throws Exception {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy hh:mm:ss a");
    String formattedDateTime = now.format(formatter);

    String email = authenticatedUserLogger.getLoggedInUsername();
    User userDetails = userService.findByEmail(email);

    Video video = new Video();
    video.setAuthorId(userDetails.getId());
    video.setUploadedAt(formattedDateTime);

    BeanUtils.copyProperties(reqBodyParams, video); // (source, target)
    System.out.println("storing video metadata" + video);
    videoService.saveOrUpdate(video); // store in SQL database

    return Optional.of(video.getId());
  }
}
