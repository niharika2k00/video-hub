package com.application.springboot.service;

import com.application.springboot.dto.Resolution;
import com.application.springboot.dto.VideoPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("VideoProcessingService")
public class VideoProcessingService implements MediaProcessingService<VideoPayload> {

  private final VideoTranscoderService videoTranscoderService;
  private final MasterManifestGeneratorService masterManifestGeneratorService;
  private final ThumbnailGeneratorService thumbnailGeneratorService;

  @Autowired
  public VideoProcessingService(VideoTranscoderService videoTranscoderService, MasterManifestGeneratorService masterManifestGeneratorService, ThumbnailGeneratorService thumbnailGeneratorService) {
    this.videoTranscoderService = videoTranscoderService;
    this.masterManifestGeneratorService = masterManifestGeneratorService;
    this.thumbnailGeneratorService = thumbnailGeneratorService;
  }

  @Override
  public void process(VideoPayload payload) throws Exception {

    int videoId = payload.getVideoId();
    String sourceVideoPath = payload.getSourceVideoPath();
    int segmentDuration = payload.getSegmentDuration();
    Resolution resolutionProfile = payload.getResolutionProfile();

    // video transcoding to HLS/ABR variants
    videoTranscoderService.transcodeToHlsVariants(videoId, sourceVideoPath, resolutionProfile, segmentDuration);

    // generate master manifest file
    masterManifestGeneratorService.generateMasterManifest(sourceVideoPath);
    thumbnailGeneratorService.generateThumbnail(sourceVideoPath);
  }
}
