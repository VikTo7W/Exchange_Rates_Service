# Rates Service

Currency exchange rates microservice: collection, storage, analysis, anomaly/markup detection, providing internal APIs, and optionally publishing events to RabbitMQ.

## Responsibilities

- Periodically fetches exchange rates from 1..N sources (providers).
- Normalizes and stores exchange rate time series (`base/quote + rate + ts + source`).
- Detects anomalies:
  - deviation from the median/MA/reference source
  - markup based on predefined rules
- Provides internal services with:
  - the latest rate for a currency pair
  - rate history for a specified period
  - a list of detected anomalies
- Optionally publishes `rates.updated` and `rates.anomaly_detected` events to RabbitMQ.

## Tech stack

- Java 21, Spring Boot 3
- PostgreSQL 16
- Flyway migrations
- RabbitMQ (optional)
- OpenAPI/Swagger
- Actuator + Prometheus metrics
- Internal token authentication (basic protection for internal APIs)

## Quick start (local)

Requirements: Docker + Docker Compose.

```bash
docker-compose up --build
