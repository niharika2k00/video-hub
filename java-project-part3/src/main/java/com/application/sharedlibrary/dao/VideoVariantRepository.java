package com.application.sharedlibrary.dao;

import com.application.sharedlibrary.entity.VideoVariant;
import com.application.sharedlibrary.entity.VideoVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoVariantRepository extends JpaRepository<VideoVariant, VideoVariantId> {
  // Both valid | done in 2 ways

  //@Query(value = "SELECT COUNT(*) FROM video_variant WHERE video_id = :id", nativeQuery = true)
  //int getCountByVideoId(@Param("id") int videoId);

  @Query("SELECT COUNT(*) FROM VideoVariant v WHERE v.video.id = ?1")
  int getCountByVideoId(int videoId);

  @Query(value = "SELECT * FROM video_variant WHERE video_id = :videoId", nativeQuery = true)
  List<VideoVariant> findByVideoId(@Param("videoId") int videoId);
}
