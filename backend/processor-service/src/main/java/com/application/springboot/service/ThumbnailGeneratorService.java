package com.application.springboot.service;

import jakarta.annotation.PostConstruct;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ThumbnailGeneratorService {

  public ThumbnailGeneratorService() {
  }

  @Value("${custom.ffmpeg.path}")
  private String ffmpegPath;

  @Value("${custom.ffmpeg.probe}")
  private String ffprobePath;

  private FFmpeg ffmpeg;
  private FFprobe ffprobe;

  @PostConstruct
  public void init() throws IOException {
    this.ffmpeg = new FFmpeg(ffmpegPath);
    this.ffprobe = new FFprobe(ffprobePath);
  }

  public String generate(String sourceVideoPath) throws IOException {
    Path videoFolderPath = Paths.get(sourceVideoPath).getParent();
    String outputPath = videoFolderPath.resolve("thumbnail.jpg").toString();

    FFmpegBuilder builder = new FFmpegBuilder()
        .setInput(sourceVideoPath)
        .overrideOutputFiles(true)
        .addOutput(outputPath)
        .setFormat("image2")
        .addExtraArgs("-ss", String.valueOf(10)) // seek time in secs
        .addExtraArgs("-vframes", "1") // Capture 1 frame only
        .done();

    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
    FFmpegJob job = executor.createJob(builder);
    job.run();

    System.out.println("✅ Thumbnail created at: " + outputPath);
    return outputPath;
  }
}
