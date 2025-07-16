package com.application.springboot.service;

import com.application.sharedlibrary.entity.User;
import com.application.springboot.dto.UserUpdateRequestDto;

public interface UserUpdateService {
  User saveOrUpdate(User user);

  void updateUser(int id, UserUpdateRequestDto requestBody) throws Exception;
}
