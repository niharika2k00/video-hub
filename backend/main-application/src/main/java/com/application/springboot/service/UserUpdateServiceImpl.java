package com.application.springboot.service;

import com.application.sharedlibrary.dao.UserRepository;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.service.UserServiceImpl;
import com.application.springboot.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateServiceImpl implements UserUpdateService {

  private final UserRepository userRepository;
  private final UserServiceImpl userServiceImpl;

  @Autowired
  public UserUpdateServiceImpl(UserRepository user_repository, UserServiceImpl user_service_impl) {
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
    User existingUser = userServiceImpl.findById(id);
    String oldPassword = existingUser.getPassword(); // hashed
    String newPassword = requestBody.getPassword(); // plaintext
    String message;

    BeanUtils.copyProperties(requestBody, existingUser);

    if (BCrypt.checkpw(newPassword, oldPassword)) { // check same hash
      message = "No change in password";
      existingUser.setPassword(oldPassword); // setting old hash again as it was overridden by copyProperties
    } else {
      message = "Password updated successfully";
      String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
      existingUser.setPassword(hashedPassword);
      saveOrUpdate(existingUser);
    }

    System.out.println(message);
    saveOrUpdate(existingUser);
  }
}
