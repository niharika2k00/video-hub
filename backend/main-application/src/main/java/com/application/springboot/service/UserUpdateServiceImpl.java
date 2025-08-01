package com.application.springboot.service;

import com.application.sharedlibrary.dao.UserRepository;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.service.UserServiceImpl;
import com.application.springboot.dto.UserDto;
import com.application.springboot.utility.FileUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserUpdateServiceImpl implements UserUpdateService {

  private final FileUtils fileUtils;
  private final UserRepository userRepository;
  private final UserServiceImpl userServiceImpl;

  @Autowired
  public UserUpdateServiceImpl(FileUtils fileUtils, UserRepository user_repository, UserServiceImpl user_service_impl) {
    this.fileUtils = fileUtils;
    this.userRepository = user_repository;
    this.userServiceImpl = user_service_impl;
  }

  @Override
  @Transactional
  public User saveOrUpdate(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUser(int id, UserDto requestBody) throws Exception {
    System.out.println("requestBody" + requestBody);
    User existingUser = userServiceImpl.findById(id);
    String oldPassword = existingUser.getPassword(); // hashed
    String newPassword = requestBody.getPassword(); // plaintext

    BeanUtils.copyProperties(requestBody, existingUser, "password"); // copy non-null properties from source to target except password

    // handle password update
    if (newPassword != null && !newPassword.isBlank()) {
      if (BCrypt.checkpw(newPassword, oldPassword)) { // check same hash
        System.out.println("No change in password");
        existingUser.setPassword(oldPassword); // setting old hash again as it was overridden by copyProperties
      } else {
        System.out.println("Password updated successfully");
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        existingUser.setPassword(hashedPassword);
      }
    } else {
      existingUser.setPassword(oldPassword);
    }

    // handle multipart profile image and upload it to cloud storage (S3)
    MultipartFile profileImage = requestBody.getProfileImage();
    if (profileImage != null && !profileImage.isEmpty()) {
      fileUtils.deleteFileByUrl(existingUser.getProfileImage()); // delete previous image
      String updatedProfileImageUrl = fileUtils.handleImageUpload(id, profileImage);
      existingUser.setProfileImage(updatedProfileImageUrl);
    }

    System.out.println("existingUser" + existingUser);
    saveOrUpdate(existingUser);
  }
}

// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/BeanUtils.html
