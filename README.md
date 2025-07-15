# <div align="center"> 🎥 Video Hub </div>

A robust microservices-based video processing and management platform built with Spring Boot, featuring video upload, processing and streaming with email notification capabilities.

## 🧱 Project Structure

```
├── core-utils            # Shared utilities and configurations
├── deployment            # Deployment scripts and configurations
├── email-service         # Handles email notifications
├── main-application      # Entry point and API gateway
├── processor-service     # Manages video processing tasks
├── README.md
├── src
└── videos
```

## 🌟 Features

- **Video Upload & Processing**

  - Support for large video file uploads (up to 50MB)
  - Asynchronous video processing using Kafka
  - Multiple resolution processing (360p, 720p, 1024p)
  - FFmpeg integration for video manipulation

- **Security**

  - JWT-based authentication
  - Role-based access control (RBAC)
  - Multiple user roles (Admin, Developer, Editor, User, etc.)
  - Secure file handling

- **Email Notifications**

  - Handles email notifications via SMTP configuration
  - Welcome emails for new users
  - Processing status notifications
  - Customizable email templates

- **Microservices Architecture**
  - Main Application Service
  - Processor Service
  - Email Service
  - Core Utilities

## 🛠️ Technology Stack

- **Backend**

  - <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="Java" width="20" height="20"/> Java 17
  - <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg" alt="Spring" width="20" height="20"/> Spring Boot 3.4.1
  - 🛡️ Spring Security
  - 📊 Spring Data JPA
  - 📨 Spring Kafka

- **Database**

  - <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original.svg" alt="MySQL" width="20" height="20"/> MySQL

- **Message Broker**

  - <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/apachekafka/apachekafka-original.svg" alt="Kafka" width="20" height="20"/> Apache Kafka

- **Video Processing**

  - 🎬 FFmpeg

- **Email Service**

  - 📧 JavaMailSender
  - 📨 SMTP (Gmail)

- **Containerization**
  - <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original.svg" alt="Docker" width="20" height="20"/> Docker

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0
- Apache Kafka
- FFmpeg
- Maven

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/niharika2k00/video-hub.git
   cd video-hub
   ```

2. **Database Setup**

   - Create a MySQL database named `videohub`
   - Update database credentials in `application.yml`

3. **Kafka Setup**

   - Start Kafka server on port 9093
   - Configure Kafka bootstrap servers in `application.yml`

4. **Build the Project**

   ```bash
   mvn clean install
   ```

5. **Run Services**

   ```bash
   # Main Application
   java -jar main-application/target/main-application-1.0.0.jar

   # Processor Service
   java -jar processor-service/target/processor-service-1.0.0.jar

   # Email Service
   java -jar email-service/target/email-service-1.0.0.jar
   ```

## 🔒 Security

The application implements a comprehensive security system with:

- JWT-based authentication
- Role-based access control
- Secure file handling
- Protected endpoints

## 📧 Email Configuration

To configure email notifications:

<!-- TODO -->

<!-- 1. Enable 2-Step Verification in your Google Account
2. Generate an App Password
3. Update the email configuration in `EmailSenderConfig.java` -->

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 👥 Author

- **Niharika Dutta** - [GitHub Profile](https://github.com/niharika2k00)

References :

- https://vite.dev/config/
- https://ffmpeg.org/ffmpeg-all.html#Video-Options
- https://tailwindcss.com/docs/installation/using-vite
