package com.application.springboot.service;

import com.application.sharedlibrary.service.ResourceLoaderService;
import com.application.sharedlibrary.util.EmailTemplateProcessor;
import com.application.springboot.dto.ContactDto;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ContactServiceImpl implements ContactService {

  private EmailTemplateProcessor emailTemplateProcessor;
  private ResourceLoaderService resourceLoaderService;
  private KafkaTemplate kafkaTemplate;

  @Autowired
  public ContactServiceImpl(EmailTemplateProcessor emailTemplateProcessor, ResourceLoaderService resourceLoaderService,
                            KafkaTemplate kafkaTemplate) {
    this.emailTemplateProcessor = emailTemplateProcessor;
    this.resourceLoaderService = resourceLoaderService;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public String processContactForm(ContactDto contactDto) throws Exception {
    // send an email to the website's email
    // send thank you note to the user
    publishContactSubmissionNotifyEvent(contactDto);
    publishContactAcknowledgementEvent(contactDto);

    return "Contact form submitted successfully!";
  }
  
  private void publishContactSubmissionNotifyEvent(ContactDto contactDto) throws Exception {
    String timestamp = LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    System.out.println("=== NEW CONTACT FORM SUBMISSION DETAILS ===");
    System.out.println("Timestamp: " + timestamp);
    System.out.println("Name: " + contactDto.getName());
    System.out.println("Email: " + contactDto.getEmail());
    System.out.println("Subject: " + contactDto.getSubject());
    System.out.println("Message: " + contactDto.getMessage());
    System.out.println("================================");

    // Mapping placeholders for replacement
    Map<String, String> replacements = Map.of(
        "{{submitted_on}}", timestamp,
        "{{name}}", contactDto.getName(),
        "{{email}}", contactDto.getEmail(),
        "{{subject}}", contactDto.getSubject(),
        "{{message}}", contactDto.getMessage());

    String mailBodyMd = resourceLoaderService.readFileFromResources("new_contact_form_submission_email.md");
    String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements); // convert markdown content
    // to html

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("subject",
        "\uD83D\uDCE9 [VideoHub] New Contact Form Submission Received - " + contactDto.getSubject());
    jsonPayload.put("body", mailBodyHtml);
    jsonPayload.put("receiverEmail", "dniharika16@gmail.com");

    System.out.println("New Contact form submitted - notification sent to team");
    kafkaTemplate.send("email-notification", jsonPayload.toJSONString());
  }

  private void publishContactAcknowledgementEvent(ContactDto contactDto) throws Exception {
    String timestamp = LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

    // Mapping placeholders for replacement
    Map<String, String> replacements = Map.of(
        "{{name}}", contactDto.getName(),
        "{{submitted_on}}", timestamp,
        "{{subject}}", contactDto.getSubject(),
        "{{message}}", contactDto.getMessage());

    String mailBodyMd = resourceLoaderService.readFileFromResources("contact_form_acknowledgement_email.md");
    String mailBodyHtml = emailTemplateProcessor.processContent(mailBodyMd, replacements); // convert markdown content to html

    JSONObject jsonPayload = new JSONObject();
    jsonPayload.put("subject", "\uD83C\uDFA5 Thank you for reaching out to VideoHub");
    jsonPayload.put("body", mailBodyHtml);
    jsonPayload.put("receiverEmail", "dniharika16@gmail.com");

    System.out.println("Acknowledgement mail send successfully to the user");
    kafkaTemplate.send("email-notification", jsonPayload.toJSONString());
  }
}
