package com.application.springboot.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class VideoPathUtil {

  // get the entire path of the source/raw video file
  // "/Users/niharika/Workspace/Personal/IntellijProjects/project-videohub/videos/1/40/40.mp4"
  public static String getSourceVideoAbsolutePath(String videoDirPath) throws IOException {
    Path directoryPath = Paths.get(videoDirPath);
    String videoId = directoryPath.getFileName().toString();

    // validate the path exists and is a directory
    if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
      throw new IllegalArgumentException("Provided path is not a valid directory: " + videoDirPath);
    }

    // list all files(ls) and find one starts with videoId
    try (Stream<Path> files = Files.list(directoryPath)) {
      Optional<Path> matchedVideoFile = files
        .filter(Files::isRegularFile)
        .filter(path -> path.getFileName().toString().startsWith((videoId + ".")))
        .findFirst();

      if (matchedVideoFile.isPresent())
        return matchedVideoFile.get().toString();

      System.out.println("⚠️ No video file starting with '" + videoId + "' found in path " + videoDirPath);
    }

    return null;
  }
}
