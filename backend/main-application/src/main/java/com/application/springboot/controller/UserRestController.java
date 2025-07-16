package com.application.springboot.controller;

import com.application.sharedlibrary.config.AwsClientBuilder;
import com.application.sharedlibrary.dao.UserRepository;
import com.application.sharedlibrary.entity.Role;
import com.application.sharedlibrary.entity.User;
import com.application.sharedlibrary.exception.CustomResourceNotFoundException;
import com.application.sharedlibrary.service.ResourceLoaderService;
import com.application.sharedlibrary.service.UserService;
import com.application.sharedlibrary.util.EmailTemplateProcessor;
import com.application.springboot.dto.LoginRequestDto;
import com.application.springboot.dto.UserDto;
import com.application.springboot.dto.UserLoginResponseDto;
import com.application.springboot.exception.ResourceAlreadyExistsException;
import com.application.springboot.service.JwtService;
import com.application.springboot.service.RoleService;
import com.application.springboot.service.UserUpdateServiceImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.application.springboot.utility.FileUtils.getFileExtention;
import static com.application.springboot.utility.FileUtils.validateImgae;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api")
public class UserRestController {

  @Value("${aws.s3.bucket-name}")
  String bucketName;

  // Autowired to inject service bean into controller, serve as an intermediary between C and DAO layer
  private final AwsClientBuilder connection;
  private final EmailTemplateProcessor emailTemplateProcessor;
  private final JwtService jwtService;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ResourceLoaderService resourceLoaderService;
  private final RoleService roleService;
  private final UserRepository userRepository;
  private final UserService userService;
  private final UserUpdateServiceImpl userUpdateServiceImpl;

  @Autowired
  public UserRestController(AwsClientBuilder connection, EmailTemplateProcessor emailTemplateProcessor, JwtService jwtService, KafkaTemplate<String, String> kafkaTemplate, ResourceLoaderService resourceLoaderService, RoleService roleService, UserRepository userRepository, UserService userService, UserUpdateServiceImpl userUpdateServiceImpl) {
    this.connection = connection;
    this.emailTemplateProcessor = emailTemplateProcessor;
    this.jwtService = jwtService;
    this.kafkaTemplate = kafkaTemplate;
    this.resourceLoaderService = resourceLoaderService;
    this.roleService = roleService;
    this.userRepository = userRepository;
    this.userService = userService;
    this.userUpdateServiceImpl = userUpdateServiceImpl;
  }

  // test http://localhost:4040/api/test
  @GetMapping("/test")
  public String test() {
    return "Welcome to backend APIs";
  }

  // GET all /users
  @GetMapping("/users")
  public List<User> findAll() {
    return userService.findAll();
  }

  // GET /users/{id}
  @GetMapping("/users/{id}")
  // can throw parent(Exception) class instead of comma separated multiple exceptions
  public User findById(@PathVariable int id) throws Exception {
    User userDetails = userService.findById(id);
    return userDetails;
  }

  // POST /users/register - SignUp | add new user
  @PostMapping(
    value = "/auth/register",
    consumes = MULTIPART_FORM_DATA_VALUE
  )
  public User addNewUser(@ModelAttribute UserDto reqUserDto) throws Exception {
    // Check: email already exist
    Optional<User> isUserExist = userRepository.findByEmail(reqUserDto.getEmail());
    if (isUserExist.isPresent()) {
      throw new ResourceAlreadyExistsException("Email already exists.");
    }

    User user = new User();
    user.setId(0);
    user.setName(reqUserDto.getName());
    user.setEmail(reqUserDto.getEmail());
    user.setAge(reqUserDto.getAge());
    user.setBio(reqUserDto.getBio());
    user.setLocation(reqUserDto.getLocation());
    user.setPhoneNumber(reqUserDto.getPhoneNumber());

    // Password encryption
    String plainTextPassword = reqUserDto.getPassword();
    String salt = BCrypt.gensalt(12);
    String hashedPassword = BCrypt.hashpw(plainTextPassword, salt);
    user.setPassword(hashedPassword);
    System.out.println("hashed password: " + hashedPassword);

    // Grant basic user role
    Role basicAuthority = roleService.findByRoleName("ROLE_USER");
    user.setRoles(new HashSet<>(List.of(basicAuthority)));

    // Save user object to DB
    User newUserObject = userUpdateServiceImpl.saveOrUpdate(user);
    System.out.println("Success! New user registered. " + newUserObject);

    // Handle profile image if present
    MultipartFile profileImage = reqUserDto.getProfileImage();
    if (profileImage != null && !profileImage.isEmpty()) {
      validateImgae(profileImage);
      String extention = getFileExtention(profileImage.getOriginalFilename());
      String objectKey = String.format("profile_images/%d%s", newUserObject.getId(), extention); // build object key

      // upload to S3
      S3Client s3Client = connection.get(S3Client.class);
      PutObjectRequest putReq = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .contentType(profileImage.getContentType())
        .build();

      try (InputStream in = profileImage.getInputStream()) {
        s3Client.putObject(putReq,
          software.amazon.awssdk.core.sync.RequestBody.fromInputStream(in, profileImage.getSize())); // fromInputStream as handling with MultipartFile
      }

      // Get public storage URL
      S3Utilities s3Utilities = s3Client.utilities();
      GetUrlRequest getUrlRequest = GetUrlRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

      // toExternalForm() function converts URL object to a String works similar as toString(). Below is the equivalent lambda function.
      String profileImageUrl = s3Utilities.getUrl(getUrlRequest).toExternalForm();

      //String profileImageUrl = s3Client.utilities()
      //  .getUrl(b -> b.bucket(bucketName).key(objectKey))
      //  .toExternalForm();
      newUserObject.setProfileImage(profileImageUrl);
      newUserObject = userUpdateServiceImpl.saveOrUpdate(newUserObject);
    }

    // Mapping placeholders for replacement
    Map<String, String> replacements = Map.of(
      "{{username}}", newUserObject.getName().toUpperCase()
    );

    // Sending email - picking email template from resources folder
    String mailBodyMd = resourceLoaderService.readFileFromResources("user_welcome_email.md");
    //String mailBodyMd = Files.readString(Paths.get("./file_path"));
    String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements); // convert markdown content to html

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("subject", "Welcome to Video Hub! Your Account Has Been Successfully Created");
    jsonPayload.put("body", mailBodyHtml);
    jsonPayload.put("receiverEmail", newUserObject.getEmail());

    kafkaTemplate.send("email-notification", jsonPayload.toJSONString());
    return newUserObject;
  }

  // POST /users/login - Login existing user
  @PostMapping("/auth/login")
  public UserLoginResponseDto loginUser(@RequestBody LoginRequestDto reqBody) throws CustomResourceNotFoundException { // use {email, password} for now, later replace with {username, password}
    System.out.println(reqBody);
    UserLoginResponseDto userLoginResponse = null;

    User userInfo = userService.findByEmail(reqBody.getEmail());
    String inputPassword = reqBody.getPassword(); // text
    String originalPassword = userInfo.getPassword(); // hashed

    // Salt is already stored as a prefix in the hashed password in database
    if (BCrypt.checkpw(inputPassword, originalPassword)) {
      System.out.println("Successfully logged in");
      String jwtToken = jwtService.buildToken(userInfo.getId()); // embedding userid(not email) as subject in the JWT token as best practice
      userLoginResponse = new UserLoginResponseDto(Optional.of(jwtToken), Optional.of(jwtService.getExpirationDate()), "Token generated successfully!");
    } else {
      System.out.println("Incorrect email or password");
      userLoginResponse = new UserLoginResponseDto(Optional.empty(), Optional.empty(), "Incorrect email or password");
    }

    return userLoginResponse;
  }

  // POST /users/logout
  @PostMapping("/auth/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.badRequest().body("Invalid token.");
    }

    String token = authHeader.substring(7);
    long expirationTime = jwtService.getExpirationDate().getTime();
    jwtService.addTokenToBlacklist(token, expirationTime);

    System.out.println("Successfully logged out.");
    return ResponseEntity.ok("Successfully logged out.");
  }

  // PUT /user/ - update existing user details
  @PutMapping(
    value = "/user/{id}",
    consumes = MULTIPART_FORM_DATA_VALUE
  )
  public String updateUser(@PathVariable int id, @ModelAttribute UserDto reqUserDto) throws Exception {
    userUpdateServiceImpl.updateUser(id, reqUserDto);
    return "User updated successfully";
  }

  // DELETE /users/{id}
  @DeleteMapping("/users/{id}")
  public String deleteUser(@PathVariable int id) throws Exception {
    userService.deleteById(id);
    System.out.println("Successfully deleted user with id " + id);
    return "Successfully deleted user with id " + id;
  }
}
