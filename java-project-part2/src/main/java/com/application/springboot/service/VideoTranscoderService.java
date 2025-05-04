package com.application.springboot.service;

import com.application.springboot.dto.Resolution;
import jakarta.annotation.PostConstruct;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class VideoTranscoderService {

  @Value("${custom.ffmpeg.path}")
  private String ffmpegPath;

  @Value("${custom.ffmpeg.probe}")
  private String ffprobePath;

  private FFmpegExecutor executor;
  private FFmpeg ffmpeg;
  private FFprobe ffprobe;

  @PostConstruct
  // After calling the constructor and injecting all @Value or @Autowired fields
  public void init() throws IOException {
    this.ffmpeg = new FFmpeg(ffmpegPath);
    this.ffprobe = new FFprobe(ffprobePath);
    this.executor = new FFmpegExecutor(ffmpeg, ffprobe);
  }

  public void transcodeToHlsVariants(String sourceVideoPath, Resolution resolutionProfile, int segmentDuration) throws IOException {
    System.out.println("source video path: " + sourceVideoPath);

    transcodeResolution(sourceVideoPath, resolutionProfile, segmentDuration);
    //generateMasterManifest(baseOutputPath, resolutionProfile);

    System.out.println("✅ HLS conversion completed.");
  }

  public void createDirectoriesIfNotExists(Path path) throws IOException {
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
  }

  public void transcodeResolution(String sourceVideoPath, Resolution resolutionProfile, int segmentDuration) throws IOException {

    // Create output directory structure
    Path videoFolderPath = Paths.get(sourceVideoPath).getParent(); // or sourceVideoPath.substring(0, sourceVideoPath.lastIndexOf("/"))  "videos/userid/videoid"
    Path manifestsFolderPath = videoFolderPath.resolve("manifests");
    Path segmentsFolderPath = videoFolderPath.resolve("segments");
    Path resolutionFolderPath = videoFolderPath.resolve("segments").resolve(resolutionProfile.getName()); // for 720p... resolution folder

    createDirectoriesIfNotExists(manifestsFolderPath);
    createDirectoriesIfNotExists(segmentsFolderPath);
    createDirectoriesIfNotExists(resolutionFolderPath);

    String segmentPattern = resolutionFolderPath.resolve("%05d.ts").toString();
    String outputManifestFilePath = videoFolderPath.resolve("manifests").resolve("rendition_" + resolutionProfile.getName() + ".m3u8").toString();

    // https://github.com/bramp/ffmpeg-cli-wrapper
    FFmpegBuilder builder = new FFmpegBuilder()
      .setInput(sourceVideoPath)
      .overrideOutputFiles(true)
      .addOutput(outputManifestFilePath)
      .setFormat("hls") // hence .ts is generated otherwise .mp4
      .setAudioBitRate(128_000) // in bps
      .setVideoBitRate(resolutionProfile.getBitrate())
      .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
      .setVideoResolution(resolutionProfile.getWidth(), resolutionProfile.getHeight())
      .setVideoCodec("libx264")
      .setAudioCodec("aac") // compression algorithm (codec) to use while video encoding
      .addExtraArgs(
        "-profile:v", "main",
        "-level", "3.1",
        "-hls_time", String.valueOf(segmentDuration),
        "-hls_playlist_type", "vod",
        "-hls_segment_filename", segmentPattern,
        "-start_number", "1",
        "-hls_list_size", "0"
      )
      .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // allow FFmpeg to use experimental specs
      .done();

    //executor.createJob(builder).run();
    System.out.println("⏳ Starting conversion: " + resolutionProfile.getName());

    try {
      //Probe to get video duration (needed for percentage calculation)
      double durationNs = ffprobe.probe(sourceVideoPath).getFormat().duration * TimeUnit.SECONDS.toNanos(1);

      //Create the job with a ProgressListener
      FFmpegJob job = executor.createJob(builder, progress -> {

        if (progress.out_time_ns <= 0 || durationNs <= 0) // Avoid N/A invalid time issue
          return;

        double percentage = (double) progress.out_time_ns / durationNs;

        System.out.printf(
          "[%s | %.0f%%] status:%s frame:%d time:%s fps:%.0f speed:%.2fx%n",
          resolutionProfile.getName(),
          percentage * 100,
          progress.status,
          progress.frame,
          FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
          progress.fps.doubleValue(),
          progress.speed
        );
      });

      job.run();
    } catch (Exception e) {
      System.err.println("❌ FFmpeg failed with error: " + e.getMessage());
      throw e;
    }

    System.out.println("✅ Conversion completed for resolution " + resolutionProfile.getName());
  }
}
