# <div align="center"> 🎥 Video Hub </div>

A modern full-stack video streaming platform built with React.js frontend and Spring Boot microservices backend, featuring HLS (HTTP Live Streaming), adaptive bitrate streaming that supports multiple resolutions, external sharing links, and seamless embeddable video players for any website.

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

- **Advanced Video Streaming**: HLS (HTTP Live Streaming) with adaptive bitrate streaming for optimal playback
- **External Sharing & Embedding**: Generate shareable links and embeddable video players for seamless integration into any website
- **Multi-Resolution Processing**: Dynamic video transcoding to 240p, 360p, 480p, 720p, and 1080p for smooth streaming across devices
- **Modern Web Interface**: Responsive React.js frontend with Tailwind CSS for intuitive video management
- **Real-time Processing**: Asynchronous video processing pipeline using Apache Kafka
- **Secure Authentication**: JWT-based security with role-based access control (Admin, Developer, Editor, User)
- **Smart Notifications**: Automated email alerts for processing status and user management
- **Microservices Architecture**: Scalable, containerized services for processing and notifications
- **Production-Ready Infrastructure**: Complete Docker Compose setup with MySQL, Kafka, and Nginx

## 🛠️ Tech Stack

**Frontend**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/react/react-original.svg" alt="React" width="20" height="20"/> React.js, <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/tailwindcss/tailwindcss-original.svg" alt="Tailwind" width="20" height="20"/> Tailwind CSS, Vite, Axios

**Backend**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="Java" width="20" height="20"/> Java 17, <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg" alt="Spring" width="20" height="20"/> Spring Boot 3.4.1, Spring Security, Spring Data JPA

**Database**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original.svg" alt="MySQL" width="20" height="20"/> MySQL

**Message Broker**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/apachekafka/apachekafka-original.svg" alt="Kafka" width="20" height="20"/> Apache Kafka

**Video Processing**: 🎬 FFmpeg, HLS Streaming, Adaptive Bitrate

**Infrastructure**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/nginx/nginx-original.svg" alt="Nginx" width="20" height="20"/> Nginx, <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original.svg" alt="Docker" width="20" height="20"/> Docker Compose

**Containerization**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original.svg" alt="Docker" width="20" height="20"/> Docker

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Node.js 16+
- FFmpeg
- Maven

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/niharika2k00/video-hub.git
   cd video-hub
   ```

2. **Infrastructure Setup**

   ```bash
   # Option 1: Using Docker (Recommended)
   cd deployment/docker-mysql
   docker compose up -d

   cd deployment/docker-kafka
   docker compose up -d

   cd deployment/docker-nginx
   docker compose up -d

   # Option 2: Manual Setup
   # - Create MySQL database named `videohub`
   # - Start MySQL server on port 3306
   # - Start Kafka server on port 9093
   # - Configure bootstrap servers in `application.yml`
   ```

3. **Build & Run**

   Choose one of the following methods:

   #### Method A: Using JAR Files (Production-like)

   ```bash
   # Build backend services
   mvn clean install

   # Run backend services (in separate terminals)
   java -jar main-application/target/main-application-1.0.0.jar
   java -jar processor-service/target/processor-service-1.0.0.jar
   java -jar email-service/target/email-service-1.0.0.jar

   # Run frontend
   cd frontend
   npm install
   npm run dev
   ```

   #### Method B: Manual Development Mode (IDE/IntelliJ)

   ```bash
   # Run each service directly from IDE or command line
   # Terminal 1: Main Application
   cd backend/main-application
   mvn spring-boot:run

   # Terminal 2: Processor Service
   cd backend/processor-service
   mvn spring-boot:run

   # Terminal 3: Email Service
   cd backend/email-service
   mvn spring-boot:run

   # Terminal 4: Frontend
   cd frontend
   npm install
   npm run dev
   ```

### Service URLs

The application will be available at:

- **Backend API**: http://localhost:4040
- **Frontend**: http://localhost:5173
- **MySQL**: localhost:3306
- **Kafka**: localhost:9093
- **Nginx**: localhost:80

## 🐳 Docker Deployment

The project includes comprehensive Docker Compose configurations in the `deployment/` directory. Follow these steps for proper infrastructure setup:

### Method A: One-Command Deployment (Recommended)

```bash
# Build, create Docker images, and push to Docker Hub
./script1.sh

# Deploy all services using Docker Compose
cd deployment/docker-deploy
docker compose up -d
```

### Method B: Individual Docker Steps

```bash
# 1. Build frontend and copy to static resources
cd frontend
npm run build
cd ..
cp -r ./frontend/dist/* ./backend/main-application/src/main/resources/static/

# 2. Build and create Docker images for each service
cd backend/main-application
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t main-application:v0.0.1 .

cd ../processor-service
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t processor-service:v0.0.1 .

cd ../email-service
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t email-service:v0.0.1 .

# 3. Run individual containers
docker run -p 4040:4040 main-application:v0.0.1
docker run -p 4041:4041 processor-service:v0.0.1
docker run -p 4042:4042 email-service:v0.0.1

# 4. Or use Docker Compose for orchestrated deployment
cd ../../deployment/docker-deploy
docker compose up -d
```

### What `script1.sh` Does

The `script1.sh` script automates the complete build and deployment process:

1. **Builds frontend** and copies dist files to static resources
2. **Builds JAR files** for all microservices using Maven
3. **Creates Docker images** for each service
4. **Pushes images** to Docker Hub (niharikadutta/\*)
5. **Supports multi-platform** builds (linux/amd64, linux/arm64)

## 📧 Email Configuration

Configure SMTP settings in `EmailSenderConfig.java` for email notifications.

## 🌐 Live Demo

- **Website**: [https://videohub.raspberryip.com/](https://videohub.raspberryip.com/)
- **Embeddable Player**: Upload videos and get instant external sharing links for seamless embedding

## 👨‍💻 Author

**Niharika Dutta** - [GitHub](https://github.com/niharika2k00)

⭐ If you found this project helpful, please give it a star!

<img width="381" height="514" alt="image" src="https://github.com/user-attachments/assets/321adfa2-cc64-46ab-93c7-f71cbb598fab" />

