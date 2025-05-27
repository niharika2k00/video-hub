package com.application.springboot.service;

import java.util.Optional;

public interface MetadataService <T, R> {

  Optional<R> saveMetadata(T reqBodyParams) throws Exception;
}
