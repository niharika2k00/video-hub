package com.application.springboot.controller;

import com.application.springboot.dto.ContactDto;
import com.application.springboot.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContactController {

  private ContactService contactService;

  @Autowired
  public ContactController(ContactService contactService) {
    this.contactService = contactService;
  }

  @PostMapping("/contact")
  public ResponseEntity<Map<String, String>> submitContactForm(@RequestBody ContactDto reqBody) throws Exception {
    String message = contactService.processContactForm(reqBody);
    System.out.println(message);

    return ResponseEntity.ok(Map.of(
        "message", message,
        "status", "success"));
  }
}
