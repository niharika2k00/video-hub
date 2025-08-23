package com.application.springboot.service;

import com.application.springboot.dto.ContactDto;

public interface ContactService {

  String processContactForm(ContactDto contactDto) throws Exception;
}
