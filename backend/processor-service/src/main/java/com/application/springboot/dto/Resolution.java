package com.application.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resolution {
  private final String name;
  private final int width;
  private final int height;
  private final int bitrate;

  public Resolution(String name, int width, int height, int bitrate) {
    this.name = name;
    this.width = width;
    this.height = height;
    this.bitrate = bitrate;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getBitrate() {
    return bitrate;
  }

  @Override
  public String toString() {
    return "Resolution{" + "name=" + name + ", width='" + width + '\'' + ", height='" + height + '\'' + ", bitrate='" + bitrate + '\'' + '}';
  }
}
