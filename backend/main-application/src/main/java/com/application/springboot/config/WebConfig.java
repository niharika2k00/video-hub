package com.application.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  // Handle all other static files
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
      .addResourceLocations("classpath:/static/") // refers to the contents of dist folder inside src/main/resources/static/
      .setCachePeriod(3600) // sets browser cache for 3600 seconds (1 hour)
      .resourceChain(false); // change to false for direct file access
  }

  // Forward all non-API requests to index.html for React Router
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("forward:/index.html");
    registry.addViewController("/{path:[^.]*}").setViewName("forward:/index.html");
  }
}
