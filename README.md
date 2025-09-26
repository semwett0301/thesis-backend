# RouteX - Thesis Backend

RouteX is a comprehensive travel route planning backend system that builds intelligent routes for trips using AI-powered recommendations and real-time data from various travel services.

## 🏗️ Architecture

RouteX follows a **microservices architecture** with the following components:

### Service Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Frontend      │    │   Frontend      │
│   (Port 3000)   │    │   (Port 3000)   │    │   (Port 3000)   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │    Spring Gateway        │
                    │    (Port 7000, HTTPS)    │
                    └─────────────┬─────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │    Eureka Server         │
                    │    (Port 8081)           │
                    └─────────────┬─────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                       │                        │
┌───────▼────────┐    ┌─────────▼─────────┐    ┌────────▼────────┐
│ Route Service  │    │ Accommodation     │    │ Tickets Service │
│ (Dynamic Port) │    │ Service           │    │ (Dynamic Port)  │
│                │    │ (Dynamic Port)    │    │                 │
└───────┬────────┘    └───────────────────┘    └─────────────────┘
        │
        │
┌───────▼────────┐    ┌───────────────────┐    ┌─────────────────┐
│ PostgreSQL     │    │ Redis Cache&Queue │    │ External APIs   │
│ (Port 5432)    │    │ (Port 6379)       │    │ - Yandex GPT    │
│ + Replica      │    │                   │    │ - Yandex Travel │
│ (Port 5433)    │    │                   │    │ - Aviasales     │
└────────────────┘    └───────────────────┘    └─────────────────┘
```

### Service Interactions

1. **API Gateway** routes all requests to appropriate microservices
2. **Eureka Server** provides service discovery and registration
3. **Route Service** orchestrates route generation using AI and external services
4. **Accommodation Service** fetches hotel/accommodation data
5. **Tickets Service** retrieves flight and train ticket information
6. **Shared Database** (PostgreSQL) with read replica for scalability
7. **Redis Cache** for session management and token storage

## 🛠️ Tech Stack

### Core Technologies
- **Java 17** - Primary programming language
- **Spring Boot 3.1.0** - Application framework
- **Spring Cloud Gateway** - API Gateway
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **Spring WebFlux** - Reactive web framework

### Microservices & Service Discovery
- **Netflix Eureka** - Service discovery and registration
- **Spring Cloud Netflix** - Microservices integration

### Database & Caching
- **PostgreSQL 14** - Primary database with replication
- **Redis 6.2** - Session storage and caching
- **Hibernate** - JPA implementation

### External Integrations
- **Yandex GPT API** - AI-powered route generation
- **Yandex Travel API** - Accommodation and train tickets
- **Aviasales API** - Flight ticket data

### Development Tools
- **Maven** - Build and dependency management
- **Lombok** - Code generation
- **MapStruct** - Object mapping
- **JWT (Auth0)** - Token-based authentication
- **Jacoco** - Code coverage
- **Docker Compose** - Container orchestration

### Security
- **JWT Tokens** - Stateless authentication
- **HTTPS/SSL** - Secure communication
- **Fingerprint-based** - Device identification

## 🚀 API Endpoints

### Authentication Service (`/api/v1/auth`)
- `GET /me` - Get current user information
- `POST /login` - User login (requires X-Finger-Print header)
- `POST /logout` - User logout (requires X-Finger-Print header)
- `POST /register` - User registration (requires X-Finger-Print header)
- `POST /refresh` - Refresh access token (requires X-Finger-Print header)

### Route Service (`/api/v1/routes`)
- `POST /` - Create new route (AI-generated)
- `GET /{id}` - Get route by ID
- `POST /saved` - Get user's saved routes (authenticated)
- `POST /{id}/save` - Save route to user's collection

### Cities Service (`/api/v1/cities`)
- `GET /` - Search cities with pagination
  - Query params: `search`, `page`, `page_size`

### Accommodation Service (`/api/v1/accommodation-service/accommodations`)
- `GET /` - Get accommodations for a city
  - Query params: `start_date`, `end_date`, `max_price`, `city_iata`

### Tickets Service (`/api/v1/ticket-service/tickets`)
- `GET /` - Get tickets (flights or trains)
  - Query params: `type` (AIRPLANE/TRAIN), `start_date`, `end_date`, `max_price`, `start_city`, `start_city_iata`, `end_city`, `end_city_iata`

## 🔧 Setup & Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 14+
- Redis 6.2+

### Environment Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd thesis-backend
   ```

2. **Start infrastructure services**
   ```bash
   docker-compose up -d
   ```
   This starts:
   - PostgreSQL database (port 5432)
   - PostgreSQL replica (port 5433)
   - Redis cache (port 6379)

3. **Build common types module**
   ```bash
   cd common-types
   mvn clean install
   cd ..
   ```

4. **Start services in order**
   ```bash
   # Start Eureka Server
   cd eureka-server
   mvn spring-boot:run
   
   # Start Route Service
   cd ../route-service
   mvn spring-boot:run
   
   # Start Accommodation Service
   cd ../accommodation-service
   mvn spring-boot:run
   
   # Start Tickets Service
   cd ../tickets-service
   mvn spring-boot:run
   
   # Start API Gateway
   cd ../spring-gateway
   mvn spring-boot:run
   ```

### Configuration

#### Environment Variables
- `DATABASE_HOST` - PostgreSQL host (default: localhost)
- `DATABASE_PORT` - PostgreSQL port (default: 5432)
- `DATABASE_NAME` - Database name (default: postgres)
- `DATABASE_USERNAME` - Database username (default: admin)
- `DATABASE_PASSWORD` - Database password (default: password)
- `SECRET_KEY` - JWT secret key for token signing

#### External API Keys
Configure the following in `application.yml` files:
- **Yandex GPT API**: IAM token and model URI
- **Yandex Travel API**: API token for accommodations and trains
- **Aviasales API**: API token for flight tickets

## 🔄 Service Flow

### Route Generation Process
1. **User Request** → API Gateway → Route Service
2. **Route Service** calls Yandex GPT API for intelligent route planning
3. **Parallel Service Calls**:
   - Accommodation Service → Yandex Travel API (hotels)
   - Tickets Service → Yandex Travel API (trains) + Aviasales API (flights)
4. **Data Aggregation** → Route Service combines all data
5. **Response** → User receives complete route with accommodations and tickets

### Authentication Flow
1. **Registration/Login** → JWT tokens generated
2. **Token Storage** → Redis cache with fingerprint binding
3. **Request Authentication** → JWT validation via Spring Security
4. **Token Refresh** → Automatic token renewal mechanism

## 📊 Database Schema

### Core Entities
- **Users** - User accounts and authentication
- **Routes** - Generated travel routes
- **RoutePoints** - Individual points in routes
- **Cities** - City information and IATA codes

### Redis Storage
- **Access Tokens** - Short-lived authentication tokens
- **Refresh Tokens** - Long-lived token renewal tokens
- **Session Data** - User session information

## 🔒 Security Features

- **JWT Authentication** with access/refresh token pattern
- **Device Fingerprinting** for enhanced security
- **HTTPS/SSL** encryption for all communications
- **Role-based Access Control** (RBAC)
- **Token Blacklisting** for secure logout
- **CORS Configuration** for frontend integration

## 🧪 Testing

Each service includes comprehensive test suites:
- **Unit Tests** - Service logic testing
- **Integration Tests** - API endpoint testing
- **Code Coverage** - Jacoco reporting

Run tests:
```bash
mvn test
```

## 📈 Monitoring & Observability

- **Eureka Dashboard** - Service discovery monitoring
- **Spring Boot Actuator** - Health checks and metrics
- **Logging** - Structured logging with SLF4J
- **Database Replication** - Read/write separation for performance

## 📝 API Documentation

The API Gateway includes Swagger/OpenAPI documentation available at:
- **Swagger UI**: `https://localhost:7000/swagger-ui.html`
- **OpenAPI Spec**: `https://localhost:7000/v3/api-docs`