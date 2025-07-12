package com.application.springboot.service;

import com.application.springboot.dto.Resolution;
import com.application.springboot.dto.ResolutionFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MasterManifestGeneratorService {

  public MasterManifestGeneratorService() {}

  public void generate(String videoDirPath) throws Exception {
    List<Resolution> allAvailableResolutions = extractResolutionProfilesFromSegments(videoDirPath);
    StringBuilder masterContent = new StringBuilder("#EXTM3U\n");

    for (Resolution res : allAvailableResolutions) {
      masterContent.append("#EXT-X-STREAM-INF:BANDWIDTH=").append(res.getBitrate())
        .append(",RESOLUTION=")
        .append(res.getWidth()).append("x").append(res.getHeight())
        .append("\n")
        .append("manifests/rendition_").append(res.getHeight()).append("p.m3u8\n");
    }

    // Files.write(path, byte[] bytes)
    Files.write(Paths.get(videoDirPath + "/master.m3u8"), masterContent.toString().getBytes());
    System.out.println("✅ Master manifest generated");
  }

  public List<Resolution> extractResolutionProfilesFromSegments(String videoDirPath) {
    List<Resolution> allResolutions = new ArrayList<>();
    Path segmentsPath = Paths.get(videoDirPath, "segments");

    if (!Files.isDirectory(segmentsPath))
      return List.of();

    try (DirectoryStream<Path> folders = Files.newDirectoryStream(segmentsPath)) {
      for (Path path : folders) {
        if (Files.isDirectory(path)) {
          String dirName = path.getFileName().toString();
          Resolution resolution = ResolutionFactory.createResolutionProfile(dirName);

          if (resolution != null)
            allResolutions.add(resolution);
        }
      }
    } catch (IOException e) {
      System.out.println("Failed to read segments: " + e.getMessage());
    }

    return allResolutions;
  }
}
