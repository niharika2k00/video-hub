package com.application.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public abstract class Payload {
  // add common fields
  private int authenticatedUserId;
  private String message;
}
