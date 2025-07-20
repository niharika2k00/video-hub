package com.application.springboot.dto;

import com.application.sharedlibrary.enums.GenderType;
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
  private String password;
  private MultipartFile profileImage;
  private String age;
  private GenderType gender;
  private String location;
  private String bio;
  private String phoneNumber;

  public UserDto(String name, String email, String password, MultipartFile profileImage, String age, GenderType gender, String location, String bio, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.profileImage = profileImage;
    this.age = age;
    this.gender = gender;
    this.location = location;
    this.bio = bio;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return "User{" +
      "name='" + (name != null ? name : "null") + '\'' +
      ", profileImage='" + (profileImage != null ? profileImage : "null") + '\'' +
      ", email='" + (email != null ? email : "null") + '\'' +
      ", age='" + (age != null ? age : "null") + '\'' +
      ", gender=" + (gender != null ? gender : "null") +
      ", location='" + (location != null ? location : "null") + '\'' +
      ", bio='" + (bio != null ? bio : "null") + '\'' +
      ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "null") + '\'' +
      '}';
  }
}
