package com.application.springboot.dto;

public class ResolutionFactory {

  public static Resolution createResolutionProfile(String name) {

    //Since most modern video resolutions follow a 16:9 aspect ratio, we can calculate the width from the height using: width = (height * 16)/9
    switch (name.toLowerCase()) {
      case "1080p":
        return new Resolution("1080p", 1920, 1080, 5000000);
      case "720p":
        return new Resolution("720p", 1280, 720, 3000000); // 3mbps
      case "480p":
        return new Resolution("480p", 854, 480, 1500000);
      case "360p":
        return new Resolution("360p", 640, 360, 800000);
      case "240p":
        return new Resolution("240p", 426, 240, 400000);
      default:
        throw new IllegalArgumentException("Unsupported resolution: " + name);
    }
  }
}
