package com.application.springboot.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

// @PostConstruct -> will also work but it will be too late to load the .env file so datasource won't find the url, username, password.
public class DotEnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    // load .env file BEFORE Spring reads application.yml
    Dotenv dotenv = Dotenv.configure()
        .directory("/Users/niharika/Workspace/Personal/projects/video-hub/backend")
        .filename(".env")
        .ignoreIfMissing()
        .load();

    // Set all .env variables as System properties
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
  }
}
