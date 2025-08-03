package com.application.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.application")
@EnableJpaRepositories("com.application")
@EntityScan("com.application")
public class SpringbootApplication {

  @Autowired
  private static Environment env;

  public SpringbootApplication(Environment env) {
    SpringbootApplication.env = env;
  }

  public static void main(String[] args) {

    System.out.println("Project started 👀...");
    ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);

    // values from env assigned at runtime once the springboot application starts.
    // Need to use @PostConstruct annotation.
    if (context instanceof WebServerApplicationContext webContext) {
      int port = webContext.getWebServer().getPort();
      System.out.println("🚀 Application is running on port: " + port);
    }

    // the value is assigned at runtime once the springboot application starts
    // String port = env.getProperty("local.server.port");
    // System.out.println("Running in port:" + port);

    // 1. Initialise default roles from RoleInitializer Class annotated with
    // @EventListener()
  }
}
