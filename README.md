# Smart Campus REST API (JAX-RS)
Client Server Architecture Coursework

## Overview

This project implements a RESTful API for a Smart Campus system that manages Rooms, Sensors, and Sensor Readings. The API is designed using RESTful principles and built using JAX-RS (Jersey) with an embedded Grizzly HTTP server.

The system allows campus administrators to:

* Manage rooms and their capacities
* Register sensors linked to specific rooms
* Record and retrieve sensor readings
* Monitor real-time sensor values

The API follows best practices including:

* Resource-based URIs
* Proper HTTP methods and status codes
* JSON communication
* Nested resource design

---

## Technologies Used

* Java 17
* JAX-RS (Jersey)
* Grizzly HTTP Server (embedded)
* Maven
* JSON (Jackson via Jersey)
* In-memory data structures (ConcurrentHashMap, ArrayList)

> No database or frameworks like Spring Boot were used, as required by the coursework.

---

## How to Build and Run

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/smart-campus-api.git
cd smart-campus-api
```

### 2. Build the project

```bash
mvn clean compile
```

### 3. Run the server

```bash
mvn exec:java
```

### 4. Access the API

```
http://localhost:8080/api/v1
```

---

## API Structure

### Base URL

```
http://localhost:8080/api/v1
```

### Endpoints

| Resource | Endpoint | Description |
|----------|----------|-------------|
| Discovery | `/` | API information |
| Rooms | `/rooms` | Manage rooms |
| Sensors | `/sensors` | Manage sensors |
| Readings | `/sensors/{id}/readings` | Manage sensor readings |

---

## Sample curl Commands

### Get API info

```bash
curl -X GET http://localhost:8080/api/v1
```

### Create a room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library Quiet Study","capacity":120}'
```

### Create a sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":21.5,"roomId":"LIB-301"}'
```

### Filter sensors

```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

### Add sensor reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"READ-001","timestamp":1710000000000,"value":22.8}'
```

### Get readings

```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

---

## Key Design Features

* Rooms contain multiple Sensors
* Sensors contain multiple SensorReadings
* Nested resource pattern used for readings
* Query filtering implemented using `?type=`
* Sub-resource locator used for readings
* Sensor `currentValue` updates automatically after new reading
* Data stored using in-memory collections

---

## Error Handling

| Scenario | HTTP Status |
|----------|-------------|
| Room has sensors | 409 Conflict |
| Invalid room reference | 422 Unprocessable Entity |
| Sensor in maintenance | 403 Forbidden |
| Unexpected error | 500 Internal Server Error |

### Example Error Response

```json
{
  "status": 409,
  "error": "RoomNotEmpty",
  "message": "Room LIB-301 cannot be deleted because sensors are still assigned."
}
```

---

## Logging

A global logging filter is implemented using JAX-RS filters:

* Logs incoming request method and URI
* Logs outgoing response status code

This improves debugging and system monitoring.

---

## Part 1 — Service Architecture & Setup

**Q1: JAX-RS Resource Lifecycle**

By default, JAX-RS creates a new instance of a resource class for each request. This ensures thread safety. In this project, shared data is managed using ConcurrentHashMap to avoid race conditions when multiple requests access the same data.

**Q2: HATEOAS**

HATEOAS allows clients to discover API endpoints dynamically through links in responses. This reduces dependency on external documentation and improves flexibility for client applications.

---

## Part 2 — Room Management

**Q1: Returning IDs vs Full Objects**

Returning only IDs reduces bandwidth but requires additional API calls. Returning full objects increases response size but improves usability. In this project, full objects are returned to simplify client interaction.

**Q2: DELETE Idempotency**

DELETE is idempotent because repeated calls produce the same result. If a room is already deleted, further DELETE requests do not change the system state.

---

## Part 3 — Sensor Operations

**Q1: @Consumes JSON Behaviour**

If a client sends a request with an unsupported format, JAX-RS returns a 415 Unsupported Media Type. This ensures only valid JSON requests are processed.

**Q2: Query vs Path Parameters**

Query parameters are better for filtering because they are optional and flexible. Path parameters should be used for identifying specific resources.

---

## Part 4 — Sub-Resources

**Q1: Sub-resource Locator Benefits**

The sub-resource locator pattern separates logic into smaller components, making the API more modular and maintainable. This avoids having one large controller class handling all endpoints.

**Q2: Data Consistency**

When a new reading is added, the `currentValue` of the parent sensor is updated. This ensures consistency between historical data and real-time values.

---

## Part 5 — Error Handling & Logging

**Q1: HTTP 422 vs 404**

HTTP 422 is more appropriate because the request is valid but contains incorrect data (invalid room ID). A 404 would imply the endpoint itself does not exist.

**Q2: Stack Trace Security Risk**

Exposing stack traces reveals internal details such as class names and file paths, which can be exploited by attackers. This project returns a generic 500 error to prevent information leakage.

**Q3: Logging Filters**

Using filters centralises logging logic and avoids duplication. It ensures consistent logging across all endpoints.

---

## Video Demonstration

A video demonstration showing all API functionality, including success and error scenarios, is included in the submission.

---

## Notes

* This API uses in-memory storage, so all data resets when the server restarts
* No database is used, as required by coursework
* Designed strictly using JAX-RS (no Spring Boot)
