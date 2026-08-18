# Rates Service

Микросервис курсов валют: сбор, хранение, анализ, детект аномалий/наценок, выдача внутренних API и (опционально) публикация событий в RabbitMQ.

## Responsibilities (что делает сервис)
- Периодически получает курсы из 1..N источников (провайдеров).
- Нормализует и сохраняет тайм-серию курсов (base/quote + rate + ts + source).
- Детектирует аномалии:
  - отклонение от медианы/MA/эталонного источника
  - “наценку”/markup по заданным правилам
- Выдаёт внутренним сервисам:
  - latest rate по паре
  - историю за период
  - список аномалий
- (Опционально) публикует события `rates.updated` и `rates.anomaly_detected` в RabbitMQ.

## Tech stack
- Java 21, Spring Boot 3
- PostgreSQL 16
- Flyway migrations
- RabbitMQ (optional)
- OpenAPI/Swagger
- Actuator + Prometheus metrics
- Internal token auth (простая защита внутренних API)

## Quick start (local)
Требования: Docker + docker-compose.

```bash
docker-compose up --build
```
