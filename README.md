# Ojas Backend

Spring Boot backend for Ojas by Tejas / Naadi project.

## Java version

Java 21 LTS

```bash
java -version
```

Expected example:

```text
java version "21.0.6" 2025-01-21 LTS
```

## Run in IntelliJ

1. Open this folder in IntelliJ:

```text
D:\React\Naadi\ojas-backend
```

2. Wait for Maven dependencies to load.
3. Run `OjasBackendApplication.java`.

Backend URL:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

H2 JDBC URL:

```text
jdbc:h2:mem:ojasdb
```

Username:

```text
sa
```

Password is empty.

## Demo Booking API

### Create booking

```http
POST http://localhost:8080/api/demo-bookings
Content-Type: application/json
```

```json
{
  "parentName": "Krishna Vinay",
  "childName": "Aarav",
  "childAge": 8,
  "phone": "9876543210",
  "email": "krishna@example.com",
  "preferredClass": "Drawing Basics",
  "message": "Need weekend demo class"
}
```

### Get all bookings

```http
GET http://localhost:8080/api/demo-bookings
```

### Get booking by id

```http
GET http://localhost:8080/api/demo-bookings/1
```
