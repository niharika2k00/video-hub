package com.application.springboot.service;

import com.application.sharedlibrary.entity.User;
import com.application.springboot.dto.UserDto;

public interface UserUpdateService {
  User saveOrUpdate(User user);

  void updateUser(int id, UserDto requestBody) throws Exception;
}
