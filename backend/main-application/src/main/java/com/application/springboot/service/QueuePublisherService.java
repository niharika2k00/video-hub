package com.application.springboot.service;

public interface QueuePublisherService {

  void publishToQueue(int id) throws Exception;
}
