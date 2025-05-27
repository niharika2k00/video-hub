package com.application.springboot.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SubtitleGenerator {

  // not working now
  public void generate(String sourceVideoPath) throws IOException, InterruptedException {
    Path videoFolderPath = Paths.get(sourceVideoPath).getParent();
    String outputPath = videoFolderPath.resolve("").toString();

    ProcessBuilder pb = new ProcessBuilder(
      "/Users/niharika/whisper-env/bin/whisper",
      sourceVideoPath,
      "--model", "base",
      "--output_format", "srt"
    );

    pb.redirectErrorStream(true);
    Process process = pb.start();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println("[WHISPER] " + line);
      }
    }

    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new RuntimeException("Whisper transcription failed with exit code " + exitCode);
    }
  }
}
