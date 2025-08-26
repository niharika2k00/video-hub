package com.application.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
  @NotNull
  private String name;

  @NotNull
  private String email;

  @NotNull
  @Size(min = 5, max = 300, message = "Subject must be between 5 and 200 characters")
  private String subject;

  @NotBlank(message = "Message is required")
  @Size(min = 5, max = 6000, message = "Message must be between 2 and 2000 characters")
  private String message;

  // default constructor
  public ContactDto() {
  }
}
