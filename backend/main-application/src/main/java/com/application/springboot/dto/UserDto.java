package com.application.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

@Getter
@Setter
public class UserDto {
  @NotNull
  private String name;
  @NotNull
  private String email;
  @NotNull
  private String password;
  private MultipartFile profileImage;
  private String age;
  private String location;
  private String bio;
  private String phoneNumber;

  public UserDto(String name, String email, String password, MultipartFile profileImage, String age, String location, String bio, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.profileImage = profileImage;
    this.age = age;
    this.location = location;
    this.bio = bio;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return "User{" + ", name='" + name + '\'' + ", email='" + email + '\'' + ", age='" + age + ", location='" + location + '\'' + ", bio='" + bio + '\'' + ", phone number='" + phoneNumber + '\'' + '}';
  }
}
