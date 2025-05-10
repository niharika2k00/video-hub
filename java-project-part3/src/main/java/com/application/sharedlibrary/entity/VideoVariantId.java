package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.VideoResolution;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable // creating composite key class for video_variant table
public class VideoVariantId implements Serializable {
  private VideoResolution resolution;
  @Column(name = "video_id")
  private int videoId;

  public VideoVariantId() {}

  public VideoVariantId(int videoId, VideoResolution resolution) {
    this.videoId = videoId;
    this.resolution = resolution;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    VideoVariantId videoVariantId = (VideoVariantId) obj;
    return Objects.equals(videoId, videoVariantId.videoId) && Objects.equals(resolution, videoVariantId.resolution);
  }

  @Override
  public int hashCode() {
    // Generate hash code from fields
    return Objects.hash(videoId, resolution);
  }
}
