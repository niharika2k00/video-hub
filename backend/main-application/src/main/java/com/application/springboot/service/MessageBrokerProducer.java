package com.application.springboot.service;

public interface MessageBrokerProducer {

  void sendToTopic(String payload) throws Exception;
}
