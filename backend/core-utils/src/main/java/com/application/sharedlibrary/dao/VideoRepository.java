package com.application.sharedlibrary.dao;

import com.application.sharedlibrary.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

  // get all videos created by an user
  // SELECT * FROM video WHERE author_id = ?;
  List<Video> findByAuthorId(Integer authorId);
}
