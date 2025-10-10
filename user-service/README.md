# User Service

Микросервис для управления пользователями и аутентификации в Travel Agency.

## 🚀 Возможности

- **Регистрация пользователей** с валидацией данных
- **Аутентификация** через JWT токены
- **Управление профилями** пользователей
- **Роли пользователей** (USER, ADMIN, MODERATOR)
- **CRUD операции** для пользователей
- **Spring Security** интеграция
- **Eureka Discovery** регистрация

## 🛠 Технологии

- **Spring Boot 3.5.6**
- **Spring Security** с JWT
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven**
- **Java 17**

## 📡 API Endpoints

### Аутентификация
- `POST /api/users/register` - Регистрация нового пользователя
- `POST /api/users/login` - Вход в систему

### Управление пользователями
- `GET /api/users/me` - Получить текущего пользователя
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users` - Получить всех пользователей (только ADMIN)
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя (только ADMIN)
- `PUT /api/users/{id}/enable` - Включить пользователя (только ADMIN)
- `PUT /api/users/{id}/disable` - Отключить пользователя (только ADMIN)

### Health Check
- `GET /api/users/health` - Проверка состояния сервиса
- `GET /api/users/test` - Тестовый endpoint
- `GET /api/users/status` - Статус сервиса

## 🔐 Безопасность

### JWT Configuration
- **Secret Key**: `mySecretKey123456789012345678901234567890`
- **Expiration**: 24 часа (86400000 мс)
- **Algorithm**: HMAC SHA-256

### Роли пользователей
- **USER**: Обычный пользователь
- **ADMIN**: Администратор (полный доступ)
- **MODERATOR**: Модератор

### Защищенные endpoints
- Все endpoints кроме `/register`, `/login`, `/health`, `/test`, `/status` требуют аутентификации
- Endpoints с `@PreAuthorize("hasRole('ADMIN')")` доступны только администраторам

## 📊 Структура данных

### User Entity
```java
- id: Long (Primary Key)
- username: String (Unique, 3-50 chars)
- email: String (Unique, valid email)
- password: String (Encrypted, min 6 chars)
- firstName: String (Required, max 50 chars)
- lastName: String (Required, max 50 chars)
- phone: String (Optional)
- role: UserRole (USER/ADMIN/MODERATOR)
- enabled: Boolean (Default: true)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### DTO Classes
- **RegisterRequestDto**: Данные для регистрации
- **LoginRequestDto**: Данные для входа
- **UserResponseDto**: Ответ с данными пользователя
- **JwtResponseDto**: Ответ с JWT токеном

## 🚀 Запуск

### Предварительные требования
- Java 17+
- Maven
- Eureka Server (порт 8761)

### Команды
```bash
# Компиляция
mvn clean compile

# Запуск
mvn spring-boot:run

# Тестирование
mvn test
```

### Batch скрипты
Используйте `microservices-manager.bat` для управления всеми сервисами.

## 🔧 Конфигурация

### application.properties
```properties
# Application
spring.application.name=user-service
server.port=8084

# Database (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true

# JWT
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## 📝 Примеры использования

### Регистрация пользователя
```bash
curl -X POST http://localhost:8084/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890"
  }'
```

### Вход в систему
```bash
curl -X POST http://localhost:8084/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "john_doe",
    "password": "password123"
  }'
```

### Получение профиля (с JWT токеном)
```bash
curl -X GET http://localhost:8084/api/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🧪 Тестирование

### Health Check
```bash
curl http://localhost:8084/api/users/health
```

### Полное тестирование
Используйте `test-all-services.bat` для тестирования всех сервисов.

## 🔍 Мониторинг

- **Eureka Dashboard**: http://localhost:8761
- **H2 Console**: http://localhost:8084/h2-console
- **Actuator**: http://localhost:8084/actuator/health

## 📋 Логирование

Логи включают:
- Spring Security события
- JWT операции
- SQL запросы (в DEBUG режиме)
- Общие операции сервиса

## ⚠️ Устранение проблем

- **Ошибки аутентификации**: Проверьте JWT токен и его срок действия
- **Ошибки валидации**: Проверьте формат данных в запросе
- **Ошибки авторизации**: Убедитесь, что у пользователя есть нужная роль
- **Проблемы с базой данных**: Проверьте H2 консоль для отладки

## 🎯 Интеграция

User Service интегрируется с другими микросервисами через:
- **Eureka Discovery** для регистрации
- **JWT токены** для аутентификации между сервисами
- **REST API** для взаимодействия

## 📈 Производительность

- **In-memory H2** для быстрого доступа
- **JWT** для stateless аутентификации
- **Spring Security** для оптимизированной безопасности
- **Connection pooling** для эффективной работы с БД


