package com.application.sharedlibrary.enums;

public enum VideoResolution {
  P144("144p"),
  P240("240p"),
  P360("360p"),
  P480("480p"),
  P720("720p"),
  P1080("1080p");

  private final String label;

  VideoResolution(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static VideoResolution fromLabel(String label) {
    for (VideoResolution r : values()) {
      if (r.label.equalsIgnoreCase(label)) {
        return r;
      }
    }
    throw new IllegalArgumentException("Unknown resolution: " + label);
  }
}

// values() -> array of enum constants e.g [P144, P240, P360]
