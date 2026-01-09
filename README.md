# Event Manager — Backend Java Service

## Описание
Event Manager — это микросервис для создания и управления мероприятиями.
Сервис предоставляет REST API для:
- создания мероприятий
- получения списка событий
- обновления/удаления мероприятия

## Технологии
- Java 17+
- Spring Boot (Web, Data JPA)
- PostgreSQL
- Kafka (для событий уведомления)

## Архитектура
- REST API
- Event-Driven коммуникация через Kafka
- Docker-Compose для запуска окружения

## Запуск
1. `git clone ...`
2. `docker-compose up`
3. перейти на http://localhost:8080/api/events
