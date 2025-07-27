package com.application.sharedlibrary.entity;

import com.application.sharedlibrary.enums.StorageType;
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

  @Column(name = "video_directory_path") // relative path
  private String videoDirectoryPath;

  @Enumerated(EnumType.STRING) // specify how an enum is stored in the database
  @Column(name = "storage_type", nullable = true)
  private StorageType storageType;

  private String category;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;
  private String duration;
  private String thumbnailUrl;
  private String title;

  @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  //@JoinColumn(name = "id", referencedColumnName = "video_id")
  private Set<VideoVariant> videoVariantList;

  public Video() {}

  public Video(int authorId, String uploadedAt, String videoDirectoryPath, StorageType storageType, String category, String description, String duration, String thumbnailUrl, String title) {
    this.authorId = authorId;
    this.uploadedAt = uploadedAt;
    this.videoDirectoryPath = videoDirectoryPath;
    this.storageType = storageType;
    this.category = category;
    this.description = description;
    this.duration = duration;
    this.thumbnailUrl = thumbnailUrl;
    this.title = title;
  }
}

/*
Issue: The findById API returned a recursive JSON because each Video contains VideoVariant reference and then holds a reference back to Video entity.
Hence, Jackson tries to serialize its videoVariantList, which contains a Video, which again contains videoVariantList, and so on...

Fix:
@JsonManagedReference: will serialize normally (i.e., videoVariantList)
@JsonBackReference: will not serialize back (breaks the recursion)
*/
