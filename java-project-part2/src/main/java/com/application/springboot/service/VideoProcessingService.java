package com.application.springboot.service;

import com.application.springboot.dto.Resolution;
import com.application.springboot.dto.VideoPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("VideoProcessingService")
public class VideoProcessingService implements MediaProcessingService<VideoPayload> {

  private final VideoTranscoderService videoTranscoderService;

  @Autowired
  public VideoProcessingService(VideoTranscoderService videoTranscoderService) {
    this.videoTranscoderService = videoTranscoderService;
  }

  @Override
  public void process(VideoPayload payload) throws Exception {

    int videoId = payload.getVideoId();
    String sourceVideoPath = payload.getSourceVideoPath();
    int segmentDuration = payload.getSegmentDuration();
    Resolution resolutionProfile = payload.getResolutionProfile();

    // video transcoding to HLS/ABR variants
    videoTranscoderService.transcodeToHlsVariants(sourceVideoPath, resolutionProfile, segmentDuration);

    //  generateMasterManifest()
  }
}
