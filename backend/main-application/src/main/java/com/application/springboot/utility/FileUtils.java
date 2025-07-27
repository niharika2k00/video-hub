package com.application.springboot.utility;

import com.application.sharedlibrary.config.AwsClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Component
public class FileUtils {

  @Value("${aws.s3.bucket-name}")
  String bucketName;

  private final Set<String> ALLOWED_FORMATS = Set.of("image/jpeg", "image/png", "image/webp", "image/heic");
  private final AwsClientBuilder connection;

  @Autowired
  public FileUtils(AwsClientBuilder connection) {
    this.connection = connection;
  }

  public void validateImgae(MultipartFile file) {
    String fileContentType = file.getContentType();
    long fileSize = file.getSize();

    System.out.println("size of display image: " + fileSize);
    if (!ALLOWED_FORMATS.contains(fileContentType)) {
      throw new IllegalArgumentException(
        "Only JPG, JPEG, PNG, WEBP or HEIC images are allowed (got " + fileContentType + ')');
    }

    // size check
    if (fileSize > 20 * 1024 * 1024) { // 20 MB
      throw new IllegalArgumentException("Image must be ≤ 20 MB");
    }
  }

  public String getFileExtention(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex).toLowerCase();
  }

  // handle profile image upload to storage (here S3)
  public String handleImageUpload(int userId, MultipartFile file) throws Exception {
    validateImgae(file);

    String extension = getFileExtention(file.getOriginalFilename());
    String objectKey = String.format("profile_images/%d%s", userId, extension); // build object key

    // upload to S3
    S3Client s3Client = connection.get(S3Client.class);
    PutObjectRequest putReq = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(objectKey)
      .contentType(file.getContentType())
      .build();

    try (InputStream in = file.getInputStream()) {
      s3Client.putObject(putReq, RequestBody.fromInputStream(in, file.getSize())); // fromInputStream as handling with MultipartFile
    }

    // Get public storage URL
    S3Utilities s3Utilities = s3Client.utilities();
    GetUrlRequest getUrlRequest = GetUrlRequest.builder()
      .bucket(bucketName)
      .key(objectKey)
      .build();

    // toExternalForm() function converts URL object to a String works similar as toString(). Below is the equivalent lambda function.
    String url = s3Utilities.getUrl(getUrlRequest).toExternalForm();

    //String url = s3Client.utilities()
    //  .getUrl(b -> b.bucket(bucketName).key(objectKey))
    //  .toExternalForm();

    return url;
  }

  // for profile image delete (while updating)
  public void deleteFileByUrl(String url) {
    // example: "https://demobucket-890291224.s3.amazonaws.com/profile_images/1.png"
    String objectKey = url.substring(url.indexOf('/', 8) + 1);

    S3Client s3Client = connection.get(S3Client.class);
    s3Client.deleteObject(b -> b.bucket(bucketName).key(objectKey));
  }

  public void deleteVideoFolderHierarchy(int userId, int videoId) {
    S3Client s3Client = connection.get(S3Client.class);
    String prefix = String.format("videos/%d/%d/", userId, videoId);
    String continuationToken = null;

    do {
      // List up to 1000 objects per call
      ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
        .bucket(bucketName)
        .prefix(prefix)
        .continuationToken(continuationToken)
        .build();

      ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

      // returns S3 object so need to handle like this
      List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
        .map(s3Obj -> ObjectIdentifier.builder().key(s3Obj.key()).build())
        .toList();

      if (!objectsToDelete.isEmpty()) {
        DeleteObjectsRequest deleteReq = DeleteObjectsRequest.builder()
          .bucket(bucketName)
          .delete(Delete.builder().objects(objectsToDelete).build())
          .build();

        s3Client.deleteObjects(deleteReq);
        System.out.println("✅ Deleted " + objectsToDelete.size() + " objects.");
      }

      continuationToken = listResponse.isTruncated() ? listResponse.nextContinuationToken() : null;
    } while (continuationToken != null);
  }

  /*
listResponse (result from S3)
    {
      "contents": [
        { "key": "videos/1/2/master.m3u8", "size": 2048 },
        { "key": "videos/1/2/thumbnail.jpg", "size": 10240 },
        { "key": "videos/1/2/manifests/rendition_360p.m3u8", "size": 4096 },
        { "key": "videos/1/2/manifests/rendition_720p.m3u8", "size": 4096 },
        ...
      ],
      "isTruncated": false,
      "keyCount": 4,
      "nextContinuationToken": null
    }

objectsToDelete can't be obj.contents as listResponse.contents() returns List<S3Object> not List<ObjectIdentifier>
class S3Object {
    String key;
    long size;
    ...
}
   */
}
