package com.application.springboot.service;

import com.application.springboot.dto.Resolution;

import java.nio.file.Path;

public interface MediaStoragePathResolver {

  void prepareDirectoryStructure(String sourceVideoPath, Resolution resolutionProfile);

  Path getBasePath(String sourceVideoPath);

  Path getManifestsPath(String sourceVideoPath);

  Path getSegmentsPath(String sourceVideoPath, Resolution resolutionProfile);
}
