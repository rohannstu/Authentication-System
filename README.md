<img width="839" height="391" alt="Screenshot from 2025-09-18 01-15-34" src="https://github.com/user-attachments/assets/40934d32-23e1-4f74-9470-5cfef733d625" />

# Authentication System

A robust authentication system built with Spring Boot that provides secure user authentication with JWT tokens and email verification functionality.

## Features

- User Registration with Email Verification
- JWT-based Authentication
- Secure Password Handling
- Email Notifications
- RESTful API Endpoints
- Spring Security Integration

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JSON Web Tokens (JWT)
- Spring Data JPA
- PostgreSQL/MySQL
- Maven

## Prerequisites

- JDK 17 or later
- Maven 3.6+
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)
- PostgreSQL/MySQL database

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/rohannstu/Spring-boot-Authentication-System.git
   cd Spring-boot-Authentication-System
   ```

2. Configure the database connection in `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_database
       username: your_username
       password: your_password
   ```

3. Configure email settings in `application.yml`:
   ```yaml
   spring:
     mail:
       host: smtp.gmail.com
       port: 587
       username: your-email@gmail.com
       password: your-app-specific-password
       properties:
         mail.smtp.auth: true
         mail.smtp.starttls.enable: true
   ```

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication

- **Register User**
  - POST `/api/auth/register`
  - Request Body:
    ```json
    {
      "name" : "your full name"
      "email": "john.doe@example.com",
      "password": "securePassword123"
    }
    ```

- **Verify Email**
  - POST `/api/auth/verify`
  - Request Body:
    ```json
    {
      "email": "john.doe@example.com",
      "code": "123456"
    }
    ```

- **Login**
  - POST `/api/auth/login`
  - Request Body:
    ```json
    {
      "email": "john.doe@example.com",
      "password": "securePassword123"
    }
    ```

## Security Features

- Password Encryption using BCrypt
- JWT Token Authentication
- Email Verification
- Spring Security Configuration
- Protected API Endpoints

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/example/authenticationsystem/
│   │       ├── auth/
│   │       │   ├── config/       # Security configurations
│   │       │   ├── controllers/  # API endpoints
│   │       │   ├── dtos/        # Data transfer objects
│   │       │   ├── entities/    # Database entities
│   │       │   ├── repos/       # Database repositories
│   │       │   ├── services/    # Business logic
│   │       │   └── utils/       # Utility classes
│   └── resources/
│       └── application.yml      # Application configuration
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Spring Boot and Spring Security documentation
- JWT implementation guidelines
- Email service integration best practices
