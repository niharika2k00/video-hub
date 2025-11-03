package com.application.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.application.springboot.config.DotEnvConfig;

@SpringBootApplication
public class SpringbootApplication {
  public static void main(String[] args) throws NullPointerException {
    System.out.println("✉️ Email Service is running (consumer-only mode)");
    // SpringApplication.run(SpringbootApplication.class, args);

    // Create SpringApplication instance
    // Initialize DotEnvConfig BEFORE running the application (app)
    SpringApplication app = new SpringApplication(SpringbootApplication.class);
    app.addInitializers(new DotEnvConfig());
    app.run(args);
  }
}
