package com.application.sharedlibrary.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Configuration
public class AwsClientBuilder implements DisposableBean {
  private final Map<Class<? extends SdkClient>, Supplier<? extends SdkClient>> factories; // key:class value:lambda function
  private final Map<Class<? extends SdkClient>, SdkClient> cache = new ConcurrentHashMap<>();

  public AwsClientBuilder(Environment env) {
    // credentials from application.yml
    Region region = Region.of(env.getProperty("aws.credentials.region", "us-east-1"));
    String profile = env.getProperty("aws.credentials.profile", "default");
    // String accessKey = env.getProperty("aws.credentials.access-key");
    // String secretKey = env.getProperty("aws.credentials.secret-key");

    // Use profile-based credentials from ~/.aws/credentials
    final AwsCredentialsProvider awsCreds = createCredentialsProvider(profile);

    // initialising factory object. NO CLIENT is created yet.
    factories = Map.of(
      S3Client.class, () -> S3Client.builder().region(region).credentialsProvider(awsCreds).build()
    );

    // SdkClient s3 = factories.get(S3Client.class).get();
    // System.out.println("s3" + s3);
  }

  private AwsCredentialsProvider createCredentialsProvider(String profile) {
    if (StringUtils.hasText(profile)) {
      try {
        return ProfileCredentialsProvider.create(profile);
      } catch (Exception e) {
        System.err.println("Failed to load profile '" + profile + "', falling back to default: " + e.getMessage());
        return DefaultCredentialsProvider.create();
      }
    } else
      return DefaultCredentialsProvider.create();
  }

  // Check if a client already exists ?? if NO then insert it
  // On passing S3Client.class, T becomes S3Client
  @SuppressWarnings("unchecked")
  public <T extends SdkClient> T get(Class<T> type) {
    Supplier<? extends SdkClient> factory = factories.get(type); // factory lookup. And create if not exist

    if (factory == null) {
      throw new IllegalArgumentException("No factory for " + type.getSimpleName());
    }
    return (T) cache.computeIfAbsent(type, k -> factory.get());
  }

  // Iterates over all cached AWS SDK clients (like S3Client, DynamoDbClient,
  // etc)and calls close() on each client to release underlying resources
  @Override
  public void destroy() {
    cache.values().forEach(c -> {
      try {
        c.close();
      } catch (Exception e) {
        System.err.println("Failed to close client: " + c.getClass().getSimpleName());
      }
    });
  }
}

/*
 * 1) Supplier<T> function :
 * - takes NO arguments and return a value
 * - mainly used for LAZY INITIALIZATION (build client only when needed)
 * - clients are created when .get() is called
 *
 * 2) Credential can also be created like below -
 * AwsCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
 * but this won't work as we need to pass awsCreds in credentialsProvider, hence
 * anyway need to typecast to StaticCredentialsProvider
 *
 */
