# Real-Time Micro-Investment Engine

A Spring Boot application that automatically rounds up user transactions and invests the spare change, helping users save money unknowingly.

## 🚀 Features

- **Round-up Savings**: Automatically rounds up transactions to the nearest whole number
- **User Authentication**: Secure JWT-based authentication system
- **Transaction Management**: Track all user transactions and savings
- **Automatic Investment**: Quartz scheduler processes investments at regular intervals
- **Goal Tracking**: Set and monitor savings goals
- **Mock Investment Platform**: Simulated investment APIs for testing
- **PostgreSQL Database**: Robust data persistence with proper relationships

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Security**: Spring Security with JWT tokens
- **Database**: PostgreSQL with JPA/Hibernate
- **Scheduling**: Quartz Scheduler for automated tasks
- **Build Tool**: Maven
- **Validation**: Bean Validation (Jakarta)

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd micro-investment-engine
```

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE micro_investment;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE micro_investment TO postgres;
```

### 3. Configuration
Update `src/main/resources/application.yml` with your database credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/micro_investment
    username: your_username
    password: your_password
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securepassword",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword"
}
```

### Transaction Endpoints

#### Create Transaction
```http
POST /api/transactions
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "amount": 93.50,
  "description": "Coffee purchase",
  "category": "Food & Beverages"
}
```

#### Get User Transactions
```http
GET /api/transactions
Authorization: Bearer <jwt_token>
```

#### Get Transactions by Category
```http
GET /api/transactions/category/Food%20%26%20Beverages
Authorization: Bearer <jwt_token>
```

## 💡 How It Works

### 1. Transaction Processing
- User makes a purchase (e.g., ₹93.50)
- System rounds up to ₹94.00
- ₹0.50 is automatically transferred to savings wallet

### 2. Investment Processing
- Quartz scheduler runs every hour
- When savings reach ₹10+, 80% is automatically invested
- Investment balance is updated accordingly

### 3. Security
- JWT tokens for stateless authentication
- Password encryption with BCrypt
- Protected endpoints require valid tokens

## 🗄️ Database Schema

### Users Table
- `id`: Primary key
- `username`: Unique username
- `email`: Unique email
- `password`: Encrypted password
- `savings_balance`: Current savings amount
- `investment_balance`: Current investment amount

### Transactions Table
- `id`: Primary key
- `user_id`: Foreign key to users
- `original_amount`: Actual transaction amount
- `charged_amount`: Rounded up amount
- `round_up_amount`: Difference (savings)
- `category`: Transaction category

### Savings Goals Table
- `id`: Primary key
- `user_id`: Foreign key to users
- `goal_name`: Name of the savings goal
- `target_amount`: Target savings amount
- `current_amount`: Current progress

## 🔧 Configuration

### JWT Settings
```yaml
spring:
  security:
    jwt:
      secret: your-secret-key-here
      expiration: 86400000  # 24 hours
```

### Scheduler Settings
- Daily investments: 2:00 AM
- Hourly investments: Every hour (for demo)

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Test with Postman/curl

1. Register a user
2. Login to get JWT token
3. Use token in Authorization header for protected endpoints

## 🚀 Deployment

### Build JAR
```bash
mvn clean package
```

### Run JAR
```bash
java -jar target/micro-investment-engine-1.0.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/micro-investment-engine-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔒 Security Considerations

- Change default JWT secret in production
- Use HTTPS in production
- Implement rate limiting
- Add input validation and sanitization
- Regular security audits

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

## 🎯 Future Enhancements

- Real-time notifications
- Multiple investment options
- Portfolio analytics
- Social features
- Mobile app integration
- Advanced goal tracking
- Tax optimization features
