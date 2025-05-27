package com.application.springboot.service;

public interface StorageService <T> {

  void storeMediaFile(int id, T reqBodyParams) throws Exception;
}
