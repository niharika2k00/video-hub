package com.application.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.application.springboot.config.DotEnvConfig;

@SpringBootApplication
@ComponentScan("com.application")
@EnableJpaRepositories("com.application")
@EntityScan("com.application")
public class SpringbootApplication {

  public static void main(String[] args) throws NullPointerException {
    System.out.println("🚀 Processing Service running (consumer-only mode)");
    // SpringApplication.run(SpringbootApplication.class, args);

    // Create SpringApplication instance
    // Initialize DotEnvConfig BEFORE running the application (app)
    SpringApplication app = new SpringApplication(SpringbootApplication.class);
    app.addInitializers(new DotEnvConfig());
    app.run(args);
  }
}
