package com.application.springboot.service;

public interface CloudStorageSyncService {

  void createBucket(String bucketName);

  void syncDirectoryFromLocal(String localPath, String remotePath) throws Exception;
}
