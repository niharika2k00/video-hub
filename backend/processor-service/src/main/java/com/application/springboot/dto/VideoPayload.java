package com.application.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// Note: if using superclass method/field then don't use @Builder annotation as its gonna override @SuperBuilder due to precedence
//@Builder
@SuperBuilder // for accessing superclass field
@Getter
@Setter
public class VideoPayload extends Payload {
  private int videoId;
  private String videoDirectoryPath;
  private int segmentDuration; // in seconds
  private Resolution resolutionProfile;
}
