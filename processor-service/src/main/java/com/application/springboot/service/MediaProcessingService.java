package com.application.springboot.service;

import com.application.springboot.dto.Payload;

public interface MediaProcessingService <T extends Payload> {

  void process(T payload) throws Exception;
}
