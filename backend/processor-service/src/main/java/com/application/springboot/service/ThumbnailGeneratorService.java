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
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

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

  public void generate(String videoDirPath, String sourceVideoFilePath) throws Exception {
    String outputPath = Paths.get(videoDirPath).resolve("thumbnail.jpg").toString();
    int randomSeconds = ThreadLocalRandom.current().nextInt(1, 11); // [1, 10] inclusive as each video segment is of 10 secs hardcoded in KafkaConsumerService.java

    FFmpegBuilder builder = new FFmpegBuilder()
      .setInput(sourceVideoFilePath)
      .overrideOutputFiles(true)
      .addOutput(outputPath)
      .setFormat("image2")
      .addExtraArgs("-ss", String.valueOf(randomSeconds)) // seek time in secs
      .addExtraArgs("-vframes", "1") // Capture 1 frame only
      .done();

    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
    FFmpegJob job = executor.createJob(builder);
    job.run();

    System.out.println("✅ Thumbnail created at: " + outputPath); // local path
  }
}
