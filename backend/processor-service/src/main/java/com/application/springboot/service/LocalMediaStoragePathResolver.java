package com.application.springboot.service;

import com.application.springboot.dto.Resolution;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("LocalMediaStoragePathResolver")
public class LocalMediaStoragePathResolver implements MediaStoragePathResolver {

  @Override
  public void prepareDirectoryStructure(String path, Resolution resolutionProfile) {
    try {
      // create directories
      Files.createDirectories(getManifestsPath(path));
      Files.createDirectories(getSegmentsPath(path, resolutionProfile));
    } catch (Exception ex) {
      throw new RuntimeException("Failed to create local directories" + ex);
    }
  }

  @Override
  public Path getBasePath(String sourceVideoPath) {
    return Paths.get(sourceVideoPath).getParent(); // .../videos/userId/videoId
  }

  // path -> "/Users/niharika/Workspace/Personal/IntellijProjects/project-videohub/videos/1/40"
  @Override
  public Path getManifestsPath(String path) { // absolute path
    return Paths.get(path).resolve("manifests");
  }

  @Override
  public Path getSegmentsPath(String path, Resolution resolutionProfile) {
    return Paths.get(path).resolve("segments").resolve(resolutionProfile.getName()); // for 720p... resolution folder
  }
}
