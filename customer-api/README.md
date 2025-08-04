# Customer API - Spring Boot

A RESTful API built with Spring Boot that provides customer data and order statistics from the Think41 interview database.

## 🚀 Features

- **Paginated Customer Listing** - Get all customers with pagination support
- **Customer Details** - Fetch individual customer with order count
- **Search & Filter** - Search by name/email or filter by country
- **Error Handling** - Comprehensive error responses with proper HTTP status codes
- **CORS Support** - Ready for frontend integration
- **API Documentation** - Swagger/OpenAPI documentation
- **Unit Tests** - Comprehensive test coverage

## 📋 API Endpoints

### Customer Endpoints

#### 1. Get All Customers
```
GET /api/customers
```

**Query Parameters:**
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (1-100, default: 20)
- `search` (optional): Search term for name or email
- `country` (optional): Filter by country

**Example:**
```bash
curl "http://localhost:8080/api/customers?page=0&size=10&search=john"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "first_name": "John",
      "last_name": "Doe",
      "email": "john@example.com",
      "age": 30,
      "gender": "M",
      "state": "California",
      "street_address": "123 Main St",
      "postal_code": "90210",
      "city": "Los Angeles",
      "country": "USA",
      "latitude": 34.0522,
      "longitude": -118.2437,
      "traffic_source": "Search",
      "created_at": "2023-01-15T10:30:00Z",
      "order_count": 5
    }
  ],
  "page_number": 0,
  "page_size": 10,
  "total_elements": 100000,
  "total_pages": 10000,
  "is_first": true,
  "is_last": false
}
```

### 2. Get Customer by ID
```
GET /api/customers/{id}
```

**Example:**
```bash
curl "http://localhost:8080/api/customers/1"
```

**Response:**
```json
{
  "id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@example.com",
  "age": 30,
  "gender": "M",
  "state": "California",
  "street_address": "123 Main St",
  "postal_code": "90210",
  "city": "Los Angeles",
  "country": "USA",
  "latitude": 34.0522,
  "longitude": -118.2437,
  "traffic_source": "Search",
  "created_at": "2023-01-15T10:30:00Z",
  "order_count": 5
}
```

### 3. Get Total Customer Count
```
GET /api/customers/count
```

**Response:**
```json
100000
```

#### 4. Check Customer Exists
```
GET /api/customers/{id}/exists
```

**Response:**
```json
true
```

### Order Endpoints

#### 1. Get All Orders for a Customer
```
GET /api/customers/{customerId}/orders
```

**Query Parameters:**
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (1-100, default: 10)

**Example:**
```bash
curl "http://localhost:8080/api/customers/1/orders?page=0&size=10"
```

**Response:**
```json
{
  "content": [
    {
      "order_id": 1001,
      "user_id": 1,
      "status": "completed",
      "gender": "M",
      "created_at": "2023-01-15T10:30:00Z",
      "returned_at": null,
      "shipped_at": "2023-01-16T09:00:00Z",
      "delivered_at": "2023-01-18T14:30:00Z",
      "num_of_item": 2,
      "customer_name": "John Doe",
      "customer_email": "john@example.com"
    }
  ],
  "page_number": 0,
  "page_size": 10,
  "total_elements": 5,
  "total_pages": 1,
  "is_first": true,
  "is_last": true
}
```

#### 2. Get Specific Order for Customer
```
GET /api/customers/{customerId}/orders/{orderId}
```

**Example:**
```bash
curl "http://localhost:8080/api/customers/1/orders/1001"
```

**Response:**
```json
{
  "order_id": 1001,
  "user_id": 1,
  "status": "completed",
  "gender": "M",
  "created_at": "2023-01-15T10:30:00Z",
  "returned_at": null,
  "shipped_at": "2023-01-16T09:00:00Z",
  "delivered_at": "2023-01-18T14:30:00Z",
  "num_of_item": 2,
  "customer_name": "John Doe",
  "customer_email": "john@example.com"
}
```

#### 3. Get Order by ID (Global)
```
GET /api/orders/{orderId}
```

**Example:**
```bash
curl "http://localhost:8080/api/orders/1001"
```

#### 4. Get Order Count for Customer
```
GET /api/customers/{customerId}/orders/count
```

**Response:**
```json
5
```

## 🛠️ Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database (from Milestone 1)

### 1. Clone and Navigate
```bash
cd customer-api
```

### 2. Configure Database
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/think41_interview
    username: postgres
    password: your_password_here
```

### 3. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### 4. View API Documentation
Open your browser and go to:
```
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Test with cURL

**Get all customers:**
```bash
curl -X GET "http://localhost:8080/api/customers?page=0&size=5" \
  -H "Accept: application/json"
```

**Get specific customer:**
```bash
curl -X GET "http://localhost:8080/api/customers/1" \
  -H "Accept: application/json"
```

**Search customers:**
```bash
curl -X GET "http://localhost:8080/api/customers?search=john&page=0&size=10" \
  -H "Accept: application/json"
```

**Filter by country:**
```bash
curl -X GET "http://localhost:8080/api/customers?country=USA&page=0&size=10" \
  -H "Accept: application/json"
```

## 📊 Error Handling

The API returns structured error responses:

**404 - Customer Not Found:**
```json
{
  "error": "Customer Not Found",
  "message": "Customer not found with ID: 999",
  "status": 404,
  "path": "/api/customers/999",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**404 - Order Not Found:**
```json
{
  "error": "Order Not Found",
  "message": "Order not found with ID: 1001 for customer: 1",
  "status": 404,
  "path": "/api/customers/1/orders/1001",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**400 - Invalid Request:**
```json
{
  "error": "Invalid Parameter",
  "message": "Invalid value 'abc' for parameter 'id'. Expected type: Integer",
  "status": 400,
  "path": "/api/customers/abc",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## 🔧 Configuration

### CORS Configuration
The API is configured to accept requests from:
- `http://localhost:3000` (React default)
- `http://localhost:5173` (Vite default)
- `http://localhost:8080` (Spring Boot default)

### Database Connection
- Uses JPA/Hibernate for database operations
- Connection pooling enabled
- Optimized queries with JOIN FETCH for performance

## 📈 Performance Features

- **Pagination** - Handles large datasets efficiently
- **Indexed Queries** - Uses database indexes for fast lookups
- **Lazy Loading** - Optimized entity relationships
- **Connection Pooling** - Efficient database connections

## 🏗️ Architecture

```
├── controller/     # REST endpoints
├── service/        # Business logic
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/           # Data transfer objects
├── exception/     # Custom exceptions
└── config/        # Configuration classes
```

Your Customer API is now ready for integration with your React frontend! 🎉