# ОТЧЕТ О ВЫПОЛНЕНИИ ПРОЕКТА TRAVEL AGENCY MICROSERVICES

## Обзор проекта

Проект Travel Agency представляет собой микросервисную архитектуру, построенную на Spring Boot и Spring Cloud, для управления туристическими поездками. Система состоит из нескольких независимых сервисов, взаимодействующих через API Gateway и Service Discovery.

## Архитектура системы

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Eureka Server  │    │  Config Server  │
│   (Port 8080)   │    │  (Port 8761)    │    │   (Port 8888)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Trip Service   │    │ Review Service  │    │ Match Service   │
│  (Port 8081)    │    │  (Port 8082)    │    │  (Port 8083)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │  User Service   │
                    │  (Port 8084)    │
                    └─────────────────┘
```

## Описание сервисов

### 1. Eureka Server (Service Discovery)
**Порт:** 8761  
**Назначение:** Центральный сервис для регистрации и обнаружения микросервисов
- Регистрирует все микросервисы при их запуске
- Предоставляет информацию о доступных сервисах
- Обеспечивает балансировку нагрузки между экземплярами сервисов
- Веб-интерфейс для мониторинга: http://localhost:8761

### 2. API Gateway (Spring Cloud Gateway)
**Порт:** 8080  
**Назначение:** Единая точка входа для всех клиентских запросов
- Маршрутизация запросов к соответствующим микросервисам
- Балансировка нагрузки между экземплярами сервисов
- CORS поддержка для веб-клиентов
- Circuit Breaker для отказоустойчивости
- Централизованная аутентификация и авторизация

**Маршруты:**
- `/api/trips/**` → Trip Service
- `/api/reviews/**` → Review Service  
- `/api/matches/**` → Match Service
- `/api/users/**` → User Service

### 3. Trip Service
**Порт:** 8081  
**Назначение:** Управление туристическими поездками
- Создание, обновление, удаление поездок
- Поиск поездок по различным критериям
- Управление статусами поездок (PLANNED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED)
- Интеграция с Review Service для получения сводки отзывов
- Circuit Breaker для вызовов внешних сервисов

**Основные endpoints:**
- `POST /api/trips` - создание поездки
- `GET /api/trips/{id}` - получение поездки по ID
- `GET /api/trips` - получение всех поездок
- `GET /api/trips/user/{userId}` - поездки пользователя
- `GET /api/trips/status/{status}` - поездки по статусу
- `GET /api/trips/search?destination=X` - поиск по направлению

### 4. Review Service
**Порт:** 8082  
**Назначение:** Управление отзывами о поездках
- Создание и управление отзывами
- Расчет средних оценок
- Фильтрация отзывов по типу (TRIP, USER, SERVICE)
- Предоставление сводной статистики для Trip Service

**Основные endpoints:**
- `POST /api/reviews` - создание отзыва
- `GET /api/reviews/{id}` - получение отзыва по ID
- `GET /api/reviews/trip/{tripId}` - отзывы по поездке
- `GET /api/reviews/user/{userId}` - отзывы пользователя
- `GET /api/reviews/trip/{tripId}/summary` - сводка отзывов

### 5. Match Service
**Порт:** 8083  
**Назначение:** Управление совпадениями поездок
- Создание совпадений между пользователями
- Управление статусами совпадений (PENDING, ACCEPTED, REJECTED)
- Поиск совпадений по различным критериям
- Интеграция с Trip Service и User Service

**Основные endpoints:**
- `POST /api/matches` - создание совпадения
- `GET /api/matches/{id}` - получение совпадения по ID
- `GET /api/matches/user/{userId}` - совпадения пользователя
- `GET /api/matches/trip/{tripId}` - совпадения по поездке

### 6. User Service
**Порт:** 8084  
**Назначение:** Управление пользователями и аутентификация
- Регистрация и аутентификация пользователей
- JWT токены для авторизации
- Управление профилями пользователей
- Интеграция с Spring Security

**Основные endpoints:**
- `POST /api/users/register` - регистрация пользователя
- `POST /api/users/login` - вход пользователя
- `GET /api/users/{id}` - получение пользователя по ID
- `PUT /api/users/{id}` - обновление пользователя
- `DELETE /api/users/{id}` - удаление пользователя

## Выполненные задания

### ✅ 1. Настройка Spring Cloud Config

**Статус:** Реализовано  
**Описание:** Настроен централизованный сервис конфигурации для всех микросервисов

**Реализованные компоненты:**
- Config Server на порту 8888
- Централизованные конфигурации для всех сервисов
- Поддержка профилей (dev, prod)
- Автоматическое обновление конфигураций

**Файлы конфигурации:**
- `config-server/src/main/resources/application.properties`
- Конфигурации для каждого сервиса в `config-repo/`

### ✅ 2. Использование Eureka для обнаружения и регистрации сервисов

**Статус:** Полностью реализовано  
**Описание:** Настроен Eureka Server для автоматической регистрации и обнаружения сервисов

**Реализованные функции:**
- Eureka Server на порту 8761
- Автоматическая регистрация всех микросервисов
- Health checks и heartbeat механизм
- Веб-интерфейс для мониторинга сервисов
- Настройка стабильности Eureka Server

**Конфигурация:**
```properties
# Eureka Server Configuration
eureka.server.enable-self-preservation=false
eureka.server.eviction-interval-timer-in-ms=60000
eureka.server.renewal-percent-threshold=0.49
```

**Скрипты запуска:**
- `start-eureka-only.bat` - запуск только Eureka Server
- `start-microservices-only.bat` - запуск микросервисов

### ✅ 3. Создание API Gateway с Spring Cloud Gateway

**Статус:** Полностью реализовано  
**Описание:** Настроен API Gateway для маршрутизации запросов и централизованного управления

**Реализованные функции:**
- Маршрутизация запросов к микросервисам
- Балансировка нагрузки (Load Balancing)
- CORS поддержка
- Circuit Breaker интеграция
- Fallback механизмы

**Конфигурация маршрутов:**
```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("trip-service", r -> r
                .path("/api/trips/**")
                .filters(f -> f
                    .circuitBreaker(config -> config
                        .setName("trip-service")
                        .setFallbackUri("forward:/fallback/trip-service"))
                    .retry(config -> config.setRetries(3)))
                .uri("lb://trip-service"))
            // ... другие маршруты
            .build();
    }
}
```

### ✅ 4. Добавление поддержки Circuit Breaker

**Статус:** Полностью реализовано  
**Описание:** Интегрирован Resilience4J для обеспечения отказоустойчивости системы

**Реализованные компоненты:**
- Circuit Breaker для всех сервисов
- Retry механизмы
- Time Limiter для контроля времени выполнения
- Fallback методы для graceful degradation

**Конфигурация Circuit Breaker:**
```properties
# Circuit Breaker Configuration
resilience4j.circuitbreaker.instances.trip-service.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.trip-service.wait-duration-in-open-state=30s
resilience4j.circuitbreaker.instances.trip-service.sliding-window-size=10
resilience4j.circuitbreaker.instances.trip-service.minimum-number-of-calls=5
```

**Реализованные паттерны:**
- Circuit Breaker в API Gateway для всех маршрутов
- Circuit Breaker в микросервисах для межсервисных вызовов
- Feign клиенты с fallback методами
- Асинхронные и синхронные fallback стратегии

**Тестирование:**
- Скрипт `test-circuit-breaker.bat` для тестирования Circuit Breaker
- Скрипт `monitor-circuit-breaker.bat` для мониторинга состояния

### ✅ 5. Реализация мониторинга и логирования

**Статус:** Полностью реализовано  
**Описание:** Настроен комплексный мониторинг и структурированное логирование

#### Spring Boot Actuator
**Реализованные endpoints:**
- `/actuator/health` - проверка здоровья сервиса
- `/actuator/info` - информация о сервисе
- `/actuator/metrics` - метрики производительности
- `/actuator/prometheus` - метрики в формате Prometheus
- `/actuator/circuitbreakers` - состояние Circuit Breaker
- `/actuator/retries` - метрики retry
- `/actuator/timelimiters` - метрики time limiter

#### Структурированное логирование
**Реализованные компоненты:**
- Logback конфигурация для всех сервисов
- JSON формат логов с Logstash encoder
- Ротация логов по времени и размеру
- Отдельные файлы для каждого сервиса
- Асинхронное логирование для производительности

**Конфигурация логирования:**
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <loggerName/>
                <message/>
                <mdc/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    <!-- ... другие appenders -->
</configuration>
```

#### Мониторинг скрипты
- `monitor-all-services.bat` - проверка всех сервисов
- `view-logs.bat` - просмотр логов
- `metrics-dashboard.bat` - метрики и производительность

### ✅ 6. Создание API документации для каждого сервиса

**Статус:** Полностью реализовано  
**Описание:** Создана интерактивная API документация с использованием OpenAPI/Swagger

#### Swagger/OpenAPI интеграция
**Реализованные компоненты:**
- Swagger UI для всех сервисов
- Детальные аннотации OpenAPI
- Интерактивное тестирование API
- Автоматическая генерация документации

**URL документации:**
- Trip Service: http://localhost:8081/swagger-ui/index.html
- Review Service: http://localhost:8082/swagger-ui/index.html
- Match Service: http://localhost:8083/swagger-ui/index.html
- User Service: http://localhost:8084/swagger-ui/index.html
- API Gateway: http://localhost:8080/swagger-ui/index.html

#### Документированные endpoints
**Каждый endpoint включает:**
- Подробное описание функциональности
- Описание всех параметров
- Возможные коды ответов
- Примеры использования
- Схемы данных

**Пример аннотации:**
```java
@Operation(summary = "Создать новую поездку", description = "Создает новую поездку с указанными параметрами")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Поездка успешно создана",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class))),
    @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
@PostMapping
public ResponseEntity<TripResponse> createTrip(
        @Parameter(description = "Данные для создания поездки", required = true)
        @Valid @RequestBody CreateTripRequest request) {
    // ...
}
```

## Технологический стек

### Основные технологии
- **Java 17** - язык программирования
- **Spring Boot 3.5.6** - основной фреймворк
- **Spring Cloud 2025.0.0** - экосистема для микросервисов
- **Spring Data JPA** - работа с данными
- **H2 Database** - in-memory база данных для разработки

### Spring Cloud компоненты
- **Spring Cloud Netflix Eureka** - Service Discovery
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud OpenFeign** - межсервисная коммуникация
- **Spring Cloud Config** - централизованная конфигурация

### Дополнительные технологии
- **Resilience4J** - Circuit Breaker, Retry, Time Limiter
- **Micrometer** - метрики и мониторинг
- **Prometheus** - сбор метрик
- **Swagger/OpenAPI** - API документация
- **Logback** - структурированное логирование
- **Maven** - система сборки

## Структура проекта

```
travel_agency/
├── eureka-server/           # Service Discovery
├── config-server/           # Configuration Server
├── api-gateway/            # API Gateway
├── trip-service/           # Trip Management Service
├── review-service/        # Review Management Service
├── match-service/         # Match Management Service
├── user-service/          # User Management Service
├── config-repo/           # Configuration Repository
├── start-*.bat            # Startup Scripts
├── monitor-*.bat          # Monitoring Scripts
├── test-*.bat            # Testing Scripts
└── README.md             # Project Documentation
```

## Скрипты управления

### Запуск сервисов
- `start-eureka-only.bat` - запуск только Eureka Server
- `start-microservices-only.bat` - запуск микросервисов
- `start-all-services.bat` - запуск всех сервисов

### Мониторинг
- `monitor-all-services.bat` - проверка всех сервисов
- `view-logs.bat` - просмотр логов
- `metrics-dashboard.bat` - метрики и производительность

### Тестирование
- `test-circuit-breaker.bat` - тестирование Circuit Breaker
- `monitor-circuit-breaker.bat` - мониторинг Circuit Breaker
- `api-documentation.bat` - открытие API документации

## Результаты выполнения

### ✅ Все задания выполнены успешно

1. **Spring Cloud Config** - настроен централизованный сервис конфигурации
2. **Eureka Service Discovery** - реализована автоматическая регистрация и обнаружение сервисов
3. **API Gateway** - создан единый входной пункт с маршрутизацией и балансировкой нагрузки
4. **Circuit Breaker** - добавлена отказоустойчивость с Resilience4J
5. **Мониторинг и логирование** - настроен комплексный мониторинг с Actuator и структурированное логирование
6. **API документация** - создана интерактивная документация с Swagger/OpenAPI

### Ключевые достижения

- **Полная микросервисная архитектура** с 6 сервисами
- **Отказоустойчивость** через Circuit Breaker паттерны
- **Централизованное управление** через API Gateway
- **Автоматическое обнаружение сервисов** через Eureka
- **Комплексный мониторинг** с метриками и логами
- **Профессиональная документация** API
- **Готовые скрипты** для управления и тестирования

### Готовность к продакшену

Проект готов к развертыванию в продакшене с дополнительными настройками:
- Замена H2 на PostgreSQL/MySQL
- Настройка внешнего Config Server
- Интеграция с внешним мониторингом (Prometheus + Grafana)
- Настройка безопасности и аутентификации
- Контейнеризация с Docker/Kubernetes

## Заключение

Проект Travel Agency успешно демонстрирует современную микросервисную архитектуру с использованием Spring Cloud. Все поставленные задачи выполнены с высоким качеством, включая дополнительные возможности мониторинга, логирования и документации. Система готова к дальнейшему развитию и масштабированию.

---

**Дата создания отчета:** 10 октября 2025  
**Версия проекта:** 1.0.0  
**Статус:** Все задания выполнены ✅
