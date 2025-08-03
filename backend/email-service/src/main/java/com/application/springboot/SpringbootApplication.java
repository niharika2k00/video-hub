package com.application.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication {
  public static void main(String[] args) throws NullPointerException {
    System.out.println("✉️ Email Service is running (consumer-only mode)");
    SpringApplication.run(SpringbootApplication.class, args);
  }
}
