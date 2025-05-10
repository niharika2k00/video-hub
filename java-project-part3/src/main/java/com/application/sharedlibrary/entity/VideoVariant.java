package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.VideoResolution;
import com.application.sharedlibrary.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "video_variant")
public class VideoVariant {
  // For this table `id` not PK, rather has Composite Key (videoId and resolution)
  @EmbeddedId
  @Column(name = "id", nullable = false)
  private VideoVariantId id; // composite key

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  private Video video;

  @Enumerated(EnumType.STRING) //specify how an enum is stored in the database
  @Column(name = "resolution", nullable = false, insertable = false, updatable = false)
  private VideoResolution resolution;

  @Column(name = "height", nullable = false, insertable = false, updatable = false)
  private int height;

  @Column(name = "width", nullable = false, insertable = false, updatable = false)
  private int width;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, insertable = false, updatable = false)
  private VideoStatus status;

  public VideoVariant() {}

  public VideoVariant(VideoVariantId id, Video video, VideoResolution resolution, int height, int width, VideoStatus status) {
    this.id = id;
    this.video = video;
    this.resolution = resolution;
    this.height = height;
    this.width = width;
    this.status = status;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", resolution='" + resolution + '\'' + ", height='" + height + '\'' + ", width='" + width + '\'' + ", status='" + status + '\'' + '}';
  }
}
