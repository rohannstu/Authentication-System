
<img width="839" height="391" alt="Screenshot from 2025-09-18 01-15-34" src="https://github.com/user-attachments/assets/40934d32-23e1-4f74-9470-5cfef733d625" />

# Authentication System

A comprehensive authentication system built with Spring Boot that handles user registration, email verification, and secure login using JWT tokens and cookies. This system provides a robust foundation for implementing secure user authentication in Spring Boot applications.

## Key Features

- **Secure User Registration**
  - Email-based registration system
  - Automatic verification code generation
  - Email notifications for verification
  
- **Email Verification**
  - Time-limited verification codes
  - Secure email verification process
  - Automated email delivery system
  
- **JWT Authentication**
  - Secure JWT token generation
  - Cookie-based token storage
  - Protected endpoint access
  
- **Security Implementation**
  - Spring Security integration
  - Secure cookie handling

## Technology Stack

- **Backend Framework**
  - Java 21
  - Spring Boot 3.x
  - Spring Security 6.x
  
- **Security**
  - JSON Web Tokens (JWT)
  - HTTP-Only Cookies
  - BCrypt Password Encoding
  
- **Database**
  - Spring Data JPA
  - PostgreSQL/MySQL
  
- **Email Service**
  - Spring Mail
  - SMTP Integration
  
- **Build Tool**
  - Maven

## Prerequisites

- JDK 21
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

## API Documentation

### Authentication Endpoints

#### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123"
}
```
- Creates new user account
- Sends verification email
- Returns: Registration confirmation

#### 2. Verify Email
```http
POST /api/auth/verify
Content-Type: application/json

{
    "email": "john.doe@example.com",
    "code": "123456"
}
```
- Verifies user email with code
- Activates user account
- Returns: Verification status

#### 3. Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "john.doe@example.com",
    "password": "securePassword123"
}
```
- Authenticates user credentials
- Sets JWT in HTTP-only cookie
- Returns: Authentication status and user info

### Response Format
```json
{
    "status": "SUCCESS",
    "message": "Operation successful",
    "data": {
        "userId": "uuid",
        "name": "John Doe",
        "email": "john.doe@example.com",
        "verified": true
    }
}
```

## Security Implementation

### Authentication Flow
1. User registers with email and password
2. System sends verification code via email
3. User verifies email with the received code
4. Upon login, JWT token is generated
5. Token is stored in HTTP-only cookie
6. Subsequent requests are authenticated via cookie

### Security Measures
  
- **JWT Implementation**
  - Short-lived JWT tokens
  - Secure cookie storage
  - Token refresh mechanism
  
- **Email Security**
  - Time-limited verification codes
  - One-time use verification tokens
  
- **Application Security**
  
  - HTTP-only cookies
  - Secure headers
  - Protected endpoints

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

