package com.application.springboot.utility;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class FileUtils {

  private static final Set<String> ALLOWED_FORMATS = Set.of("image/jpeg", "image/png", "image/webp", "image/heic");

  public static void validateImgae(MultipartFile file) {
    String fileContentType = file.getContentType();
    long fileSize = file.getSize();

    System.out.println("size of display image: " + fileSize);
    if (!ALLOWED_FORMATS.contains(fileContentType)) {
      throw new IllegalArgumentException(
        "Only JPG, JPEG, PNG, WEBP or HEIC images are allowed (got " + fileContentType + ')');
    }

    // size check
    if (fileSize > 8 * 1024 * 1024) {      // 8 MB
      throw new IllegalArgumentException("Image must be ≤ 8 MB");
    }
  }

  public static String getFileExtention(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex).toLowerCase();
  }
}
