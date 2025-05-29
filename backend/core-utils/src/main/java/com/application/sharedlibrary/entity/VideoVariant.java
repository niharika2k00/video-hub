package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
  // resolution is already part of the composite key (VideoVariantId), it was redundant
  @EmbeddedId
  private VideoVariantId id; // composite key

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @JsonBackReference
  private Video video;

  @Column(name = "height", nullable = false)
  private int height;

  @Column(name = "width", nullable = false)
  private int width;

  @Enumerated(EnumType.STRING) // specify how an enum is stored in the database
  @Column(name = "status", nullable = false)
  private VideoStatus status;

  public VideoVariant() {}

  public VideoVariant(VideoVariantId id, Video video, int height, int width, VideoStatus status) {
    this.id = id;
    this.video = video;
    this.height = height;
    this.width = width;
    this.status = status;
  }

  @Override
  public String toString() {
    return "Image{" + "id=" + id + ", height='" + height + '\'' + ", width='" + width + '\'' + ", status='" + status + '\'' + '}';
  }
}
