# Retailer Rewards Application V3

## Overview

Retailer Rewards Application is a Spring Boot-based RESTful service that manages customer transactions and calculates reward points based on transaction amounts. It supports saving transactions, retrieving monthly reward summaries, and querying points earned in the last 3 months.

---

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database (for testing)
- JUnit & Mockito (for testing)
- Maven

---

## Features

- Save customer transactions and calculate reward points.
- Retrieve monthly reward summaries.
- Fetch reward points earned by a customer in the last 3 months.
- Exception handling for validation, data integrity, and missing customers.

---

## Architecture

### Layers:
- **Controller**: Handles HTTP requests.
- **Service**: Business logic for transactions and rewards.
- **Repository**: Data access using Spring Data JPA.
- **Entity**: JPA models for `Customer` and `Transaction`.
- **DTO**: Data Transfer Object for transaction data.
- **Exception Handling**: Custom and global exception management.

---

## Package Structure

Here is the **project structure** for the **Retailer Rewards Application V3**, based on the image you provided:

```
RetailerOffersRewardsProgram/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/retaileroffers/
│   │   │       ├── controller/
│   │   │       │   └── TransactionController.java
│   │   │       ├── dao/
│   │   │       │   └── TransactionsDTO.java
│   │   │       ├── entity/
│   │   │       │   ├── Customer.java
│   │   │       │   └── Transaction.java
│   │   │       ├── globalexception/
│   │   │       │   ├── CustomerNotFoundException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── repo/
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   └── TransactionRepository.java
│   │   │       └── service/
│   │   │           ├── TransactionService.java
│   │   │           └── TransactionServiceImpl.java
│   │   └── resources/
│   │       └── application.properties
│
├── test/
│   └── java/
│       └── com/retaileroffers/
│           ├── RewardsPointsApplicationTests.java
│           ├── controller/
│           │   └── TransactionControllerTest.java
│           ├── integration/
│           │   └── TestTransactionalControllerIntegration.java
│           └── service/
│               └── TransactionServiceImplTest.java
```


---

## Reward Calculation Logic

- **Amount ≤ 50**           : 0 points
- **Amount > 50 and ≤ 100** : 1 point per dollar over 50
- **Amount > 100**          : 2 points per dollar over 100 + 50 points

---

## API Endpoints

### 1. Save Transaction
```
PUT /transaction/save
```
**Request Body**:
```json
{
  "amount": 120,
  "customerId": 1,
  "month": "2025-07"
}
```

**Response**: Updated `Customer` object with new points and amount.

---

### 2. Get Last 3 Months Points
```
GET /transaction/points/last3months?customerId=1
```

**Response**: List of `TransactionsDTO` with month-wise points.

Here’s an expanded and more detailed section for the **API Endpoints** in the README:

---

### 3. **Save Transaction**

**Endpoint**:  
`PUT /transaction/save`

**Description**:  
Saves a new transaction for a customer and calculates reward points based on the transaction amount.


**Request Body**:
```
json
{
  "amount": 120,
  "customerId": 1,
  "month": "2025-07"
}
```

**Validations**:
- `amount`: Required, must be > 0
- `customerId`: Required
- `month`: Required, non-blank

**Response**:
- `200 OK`: Returns the updated `Customer` object with new points and amount.
- `404 NOT FOUND`: If the customer ID does not exist.
- `500 INTERNAL SERVER ERROR`: If transaction saving fails.

---

### 4. **Get Reward Points for Last 3 Months**

**Endpoint**:  
`GET /transaction/points/last3months?customerId={id}`

**Description**:  
Fetches the reward points earned by a customer in the last 3 calendar months.

**Query Parameter**:
- `customerId` (Long): ID of the customer

**Response**:
```
json
[
  {
    "customerId": 1,
    "month": "2025-05",
    "totalPoints": 120
  },
  {
    "customerId": 1,
    "month": "2025-06",
    "totalPoints": 90
  },
  {
    "customerId": 1,
    "month": "2025-07",
    "totalPoints": 150
  }
]
```

**Status Codes**:
- `200 OK`: Returns a list of `TransactionsDTO` with month-wise points.
- `404 NOT FOUND`: If no data is found for the customer in the last 3 months.

---

## Reward Points Calculation Logic

| Transaction Amount | Reward Points Calculation                    |
|--------------------|----------------------------------------------|
| ≤ $50              | 0 points                                     |
| $51 - \$100        | 1 point per dollar over \$50                 |
| > $100             | 2 points per dollar over \$100 + 50 points   |

**Example**:
- \$120 transaction → (120 - 100) * 2 + 50 = **90 points**

---

## Validation

- `amount`: Required, must be > 0
- `customerId`: Required
- `month`: Required, non-blank

---

## Exception Handling

- `CutomerNotFoundException`        : 404 NOT FOUND
- `MethodArgumentNotValidException` : 400 BAD REQUEST
- `DataIntegrityViolationException` : 409 CONFLICT

---

## Database Configuration

**PostgreSQL** is used as the database.

```properties
spring.application.name=RewardsPoints
spring.datasource.url=jdbc:postgresql://localhost:5432/rewardsdb
spring.datasource.username=postgres
spring.datasource.password=******
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## Testing

You can test the application using tool like **SoupUI** by hitting the endpoints mentioned above.

---

