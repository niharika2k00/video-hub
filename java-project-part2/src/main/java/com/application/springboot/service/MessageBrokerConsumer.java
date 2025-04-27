package com.application.springboot.service;

public interface MessageBrokerConsumer {

  void listenToTopic(String payload) throws Exception;
}
