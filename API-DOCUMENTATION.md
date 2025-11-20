# API Documentation Implementation

## Обзор

В проекте Travel Agency реализована полноценная API документация с использованием OpenAPI/Swagger для всех микросервисов. Документация включает интерактивные интерфейсы для тестирования API endpoints.

## Реализованные компоненты

### 1. Swagger/OpenAPI зависимости

Добавлены следующие зависимости во все сервисы:

**Для обычных сервисов (Trip, Review, Match, User):**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

**Для API Gateway (WebFlux):**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

### 2. Конфигурация OpenAPI

Созданы конфигурационные классы `OpenApiConfig` для каждого сервиса:

- **Trip Service**: `/src/main/java/com/travelagency/tripservice/config/OpenApiConfig.java`
- **Review Service**: `/src/main/java/com/travelagency/reviewservice/config/OpenApiConfig.java`
- **Match Service**: `/src/main/java/com/travelagency/matchservice/config/OpenApiConfig.java`
- **User Service**: `/src/main/java/com/travelagency/userservice/config/OpenApiConfig.java`
- **API Gateway**: `/src/main/java/com/travelagency/apigateway/config/OpenApiConfig.java`

Каждая конфигурация включает:
- Название и описание API
- Контактную информацию
- Лицензию
- Серверы (development и production)

### 3. Аннотации OpenAPI

Добавлены подробные аннотации во все контроллеры:

- `@Tag` - для группировки endpoints
- `@Operation` - для описания операций
- `@ApiResponses` - для описания возможных ответов
- `@Parameter` - для описания параметров
- `@Schema` - для описания моделей данных

### 4. Конфигурация Swagger UI

Настроена конфигурация в `application.properties` для всех сервисов:

```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.try-it-out-enabled=true
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.filter=true
```

## Доступ к документации

### URL для каждого сервиса:

- **Trip Service**: http://localhost:8081/swagger-ui/index.html
- **Review Service**: http://localhost:8082/swagger-ui/index.html
- **Match Service**: http://localhost:8083/swagger-ui/index.html
- **User Service**: http://localhost:8084/swagger-ui/index.html
- **API Gateway**: http://localhost:8080/swagger-ui/index.html

### Скрипт для быстрого доступа:

Создан скрипт `api-documentation.bat` для удобного открытия документации всех сервисов.

## Документированные API endpoints

### Trip Service API

**Основные операции:**
- `POST /api/trips` - Создать новую поездку
- `GET /api/trips/{id}` - Получить поездку по ID
- `GET /api/trips` - Получить все поездки
- `GET /api/trips/user/{userId}` - Получить поездки пользователя
- `GET /api/trips/status/{status}` - Получить поездки по статусу
- `GET /api/trips/search?destination=X` - Поиск поездок по направлению
- `PUT /api/trips/{id}/status` - Обновить статус поездки
- `DELETE /api/trips/{id}` - Удалить поездку

**Circuit Breaker endpoints:**
- `GET /api/trips/{id}/reviews/summary` - Получить сводку отзывов
- `GET /api/trips/{id}/reviews/summary/async` - Получить сводку отзывов асинхронно

### Review Service API

- `POST /api/reviews` - Создать отзыв
- `GET /api/reviews/{id}` - Получить отзыв по ID
- `GET /api/reviews` - Получить все отзывы
- `GET /api/reviews/trip/{tripId}` - Получить отзывы по поездке
- `GET /api/reviews/user/{userId}` - Получить отзывы пользователя
- `GET /api/reviews/trip/{tripId}/summary` - Получить сводку отзывов поездки

### Match Service API

- `POST /api/matches` - Создать совпадение
- `GET /api/matches/{id}` - Получить совпадение по ID
- `GET /api/matches` - Получить все совпадения
- `GET /api/matches/user/{userId}` - Получить совпадения пользователя
- `GET /api/matches/trip/{tripId}` - Получить совпадения по поездке

### User Service API

- `POST /api/users/register` - Регистрация пользователя
- `POST /api/users/login` - Вход пользователя
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users` - Получить всех пользователей
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### API Gateway

Все вышеперечисленные endpoints доступны через Gateway по адресу:
`http://localhost:8080/api/{service}/*`

## Особенности реализации

### 1. Детальная документация

Каждый endpoint включает:
- Подробное описание функциональности
- Описание всех параметров
- Возможные коды ответов
- Примеры использования
- Схемы данных

### 2. Circuit Breaker документация

Специально документированы endpoints с Circuit Breaker:
- Описаны сценарии fallback
- Указаны возможные ошибки сервисов
- Документированы асинхронные операции

### 3. Интерактивное тестирование

Swagger UI позволяет:
- Тестировать endpoints напрямую из браузера
- Просматривать примеры запросов и ответов
- Валидировать схемы данных
- Экспортировать документацию в различные форматы

### 4. Многосерверная конфигурация

Каждый сервис настроен для работы с:
- Development сервером (прямой доступ)
- Production сервером (через Gateway)

## Мониторинг документации

### Health Check endpoints

Каждый сервис предоставляет endpoints для мониторинга:
- `/actuator/health` - Статус сервиса
- `/actuator/info` - Информация о сервисе
- `/actuator/metrics` - Метрики сервиса

### Custom мониторинг endpoints

Дополнительные endpoints для мониторинга:
- `/api/{service}/monitoring/status` - Статус сервиса
- `/api/{service}/monitoring/health` - Детальная информация о здоровье
- `/api/{service}/monitoring/metrics/summary` - Сводка метрик

## Использование

1. **Запустите все сервисы** используя `start-all-services.bat`
2. **Откройте документацию** используя `api-documentation.bat`
3. **Изучите API** через интерактивный интерфейс Swagger UI
4. **Протестируйте endpoints** прямо в браузере
5. **Используйте примеры** для интеграции с клиентскими приложениями

## Преимущества

- **Автоматическая генерация** документации из кода
- **Интерактивное тестирование** API
- **Актуальность** - документация всегда соответствует коду
- **Стандартизация** - использование OpenAPI стандарта
- **Удобство** - единый интерфейс для всех сервисов
- **Профессиональность** - готовый продукт для клиентов

Документация готова к использованию и может быть легко интегрирована в CI/CD процессы для автоматической генерации и публикации.
