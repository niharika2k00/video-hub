package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.VideoResolution;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable // creating composite key class for video_variant table
public class VideoVariantId implements Serializable {
  @Column(name = "video_id")
  private int videoId;

  @Enumerated(EnumType.STRING)
  @Column(name = "resolution", nullable = false)
  private VideoResolution resolution;

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
