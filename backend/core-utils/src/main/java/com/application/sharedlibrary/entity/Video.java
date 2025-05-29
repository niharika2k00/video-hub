package com.application.sharedlibrary.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "video")
public class Video {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private int id;

  @Column(name = "author_id")
  private int authorId;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false, unique = true)
  private String uploadedAt;

  @Column(name = "file_path")
  private String filePath;

  private String title;
  private String description;
  private String category;
  private String duration;
  private String thumbnailUrl;

  @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  //@JoinColumn(name = "id", referencedColumnName = "video_id")
  private Set<VideoVariant> videoVariantList;

  public Video() {}

  public Video(String title, String description, String category, int authorId, String uploadedAt, String filePath, String duration, String thumbnailUrl) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.authorId = authorId;
    this.uploadedAt = uploadedAt;
    this.filePath = filePath;
    this.duration = duration;
    this.thumbnailUrl = thumbnailUrl;
  }
}

/*
Issue: The findById API returned a recursive JSON because each Video contains VideoVariant reference and then holds a reference back to Video entity.
Hence, Jackson tries to serialize its videoVariantList, which contains a Video, which again contains videoVariantList, and so on...

Fix:
@JsonManagedReference: will serialize normally (i.e., videoVariantList)
@JsonBackReference: will not serialize back (breaks the recursion)
*/
