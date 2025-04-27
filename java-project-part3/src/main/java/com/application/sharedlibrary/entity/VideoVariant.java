package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.VideoResolution;
import com.application.sharedlibrary.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "video_variant")
public class VideoVariant {
  // For this table `id` not as PK, rather has Composite Key (videoId and resolution)
  @EmbeddedId
  @Column(name = "id", nullable = false)
  private VideoVariantId id; // composite key

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  private Video video;

  @Column(name = "height", nullable = false, insertable = false, updatable = false)
  private VideoResolution resolution;

  @Column(name = "width", nullable = false, insertable = false, updatable = false)
  private VideoStatus status;

  public VideoVariant() {}

  public VideoVariant(VideoVariantId id, Video video, VideoResolution resolution, VideoStatus status) {
    this.id = id;
    this.video = video;
    this.resolution = resolution;
    this.status = status;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", resolution='" + resolution + '\'' + ", status='" + status + '\'' + '}';
  }
}
