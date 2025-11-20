// API Integration для Travelio Frontend
// Связывает фронтенд с микросервисами

class TravelioAPI {
    constructor() {
        // Базовые URL микросервисов
        this.baseURLs = {
            userService: 'https://responsible-education-production.up.railway.app/api/users',
            tripService: 'https://terrific-purpose-production.up.railway.app/api/trips',
            reviewService: 'https://gentle-benevolence-production.up.railway.app/api/reviews',
            matchService: 'https://positive-nature-production.up.railway.app/api/matches'
        };
        
        // Токен аутентификации
        this.authToken = localStorage.getItem('authToken');
    }

    // Общий метод для HTTP запросов
    async makeRequest(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'Cache-Control': 'no-cache',
                'Pragma': 'no-cache',
                ...(this.authToken && { 'Authorization': `Bearer ${this.authToken}` })
            },
            mode: 'cors',
            credentials: 'omit',
            cache: 'no-cache'
        };

        const finalOptions = { ...defaultOptions, ...options };
        
        console.log('Отправляем запрос:', {
            url: url,
            method: finalOptions.method || 'GET',
            headers: finalOptions.headers,
            body: finalOptions.body ? JSON.parse(finalOptions.body) : null
        });
        
        try {
            const response = await fetch(url, finalOptions);
            
            console.log('Получен ответ:', {
                status: response.status,
                statusText: response.statusText,
                headers: Object.fromEntries(response.headers.entries())
            });
            
            if (!response.ok) {
                let errorMessage = '';
                
                // Получаем текст ошибки от сервера
                try {
                    const errorData = await response.json();
                    errorMessage = errorData.message || errorData.error || '';
                } catch {
                    errorMessage = '';
                }
                
                // Определяем понятное сообщение по статусу
                switch (response.status) {
                    case 400:
                        errorMessage = errorMessage || 'Неверные данные запроса';
                        break;
                    case 401:
                        errorMessage = errorMessage || 'Неверный email или пароль';
                        break;
                    case 403:
                        errorMessage = errorMessage || 'Доступ запрещен';
                        break;
                    case 404:
                        errorMessage = errorMessage || 'Пользователь не найден';
                        break;
                    case 409:
                        errorMessage = errorMessage || 'Пользователь с таким email или именем уже существует';
                        break;
                    case 500:
                        errorMessage = errorMessage || 'Ошибка сервера. Попробуйте позже';
                        break;
                    default:
                        errorMessage = errorMessage || `Ошибка сервера (код ${response.status})`;
                }
                
                throw new Error(errorMessage);
            }
            
            return await response.json();
        } catch (error) {
            console.error('API Request failed:', error);
            console.error('Error details:', {
                name: error.name,
                message: error.message,
                stack: error.stack
            });
            
            // Обработка различных типов ошибок
            if (error.name === 'TypeError' && error.message.includes('Failed to fetch')) {
                throw new Error('Не удается подключиться к серверу. Проверьте подключение к интернету и попробуйте снова.');
            } else if (error.name === 'TypeError' && error.message.includes('NetworkError')) {
                throw new Error('Ошибка сети. Сервер недоступен.');
            } else if (error.name === 'AbortError') {
                throw new Error('Запрос был отменен.');
            } else if (error.message) {
                // Если уже есть понятное сообщение, используем его
                throw error;
            } else {
                throw new Error('Произошла неизвестная ошибка. Попробуйте снова.');
            }
        }
    }

    // ==================== USER SERVICE ====================
    
    // Регистрация пользователя
    async registerUser(userData) {
        const { username, email, password, firstName, lastName, phone } = userData;
        
        const requestData = {
            username: username,
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName,
            phone: phone || null
        };

        try {
            const response = await this.makeRequest(`${this.baseURLs.userService}/register`, {
                method: 'POST',
                body: JSON.stringify(requestData)
            });
            
            // Сохраняем токен
            this.authToken = response.token;
            localStorage.setItem('authToken', this.authToken);
            
            return response;
        } catch (error) {
            console.error('Registration failed:', error);
            throw error;
        }
    }
    
    // Авторизация пользователя
    async loginUser(loginData) {
        const { email, password } = loginData;
        
        const requestData = {
            usernameOrEmail: email,  // Исправлено: используем правильное поле
            password: password
        };

        try {
            const response = await this.makeRequest(`${this.baseURLs.userService}/login`, {
                method: 'POST',
                body: JSON.stringify(requestData)
            });
            
            // Сохраняем токен
            this.authToken = response.token;
            localStorage.setItem('authToken', this.authToken);
            
            return response;
        } catch (error) {
            console.error('Login failed:', error);
            throw error;
        }
    }

    // Получение текущего пользователя
    getCurrentUser() {
        const userData = localStorage.getItem('currentUser');
        return userData ? JSON.parse(userData) : null;
    }


    // Выход из системы
    logout() {
        this.authToken = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('currentUser');
    }

    // Проверка авторизации
    isAuthenticated() {
        return !!this.authToken;
    }
    
    // Получение отзывов пользователя
    async getUserReviews() {
        try {
            const user = this.getCurrentUser();
            if (!user) return [];
            
            const response = await this.makeRequest(`${this.baseURLs.reviewService}/user/${user.id}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить отзывы пользователя:', error);
            return [];
        }
    }
    
    // Получение маршрутов пользователя
    async getUserRoutes() {
        try {
            const user = this.getCurrentUser();
            if (!user) return [];
            
            const response = await this.makeRequest(`${this.baseURLs.tripService}/user/${user.id}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить маршруты пользователя:', error);
            return [];
        }
    }
    
    // Получение совпадений пользователя
    async getUserMatches() {
        try {
            const user = this.getCurrentUser();
            if (!user) return [];
            
            const response = await this.makeRequest(`${this.baseURLs.matchService}/user/${user.id}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить совпадения пользователя:', error);
            return [];
        }
    }
    
    // Получение всех пользователей
    async getAllUsers() {
        try {
            const response = await this.makeRequest(`${this.baseURLs.userService}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить пользователей:', error);
            return [];
        }
    }
    
    // Получение пользователя по ID
    async getUserById(userId) {
        try {
            const response = await this.makeRequest(`${this.baseURLs.userService}/${userId}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить пользователя:', error);
            throw error;
        }
    }
    
    // Получение всех совпадений
    async getAllMatches() {
        try {
            const response = await this.makeRequest(`${this.baseURLs.matchService}`, {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.log('Не удалось загрузить совпадения:', error);
            return [];
        }
    }
    
    // Создание совпадения
    async createMatch(matchData) {
        try {
            const response = await this.makeRequest(`${this.baseURLs.matchService}`, {
                method: 'POST',
                body: JSON.stringify(matchData)
            });
            return response;
        } catch (error) {
            console.log('Не удалось создать совпадение:', error);
            throw error;
        }
    }

    // Обновление навигации в зависимости от статуса авторизации
    updateNavigation() {
        const profileLinks = document.querySelectorAll('nav a.profile-link');
        profileLinks.forEach(link => {
            if (this.isAuthenticated()) {
                link.href = 'register.html';
                link.textContent = 'Профиль';
            } else {
                link.href = 'register.html';
                link.textContent = 'Профиль';
            }
        });
    }
    
    // Инициализация навигации на всех страницах
    initializeNavigation() {
        // Обновляем токен из localStorage
        this.authToken = localStorage.getItem('authToken');
        
        // Обновляем навигацию
        this.updateNavigation();
        
        // Если пользователь авторизован, показываем профиль на странице register.html
        if (this.isAuthenticated() && window.location.pathname.includes('register.html')) {
            if (typeof showProfile === 'function') {
                showProfile();
            }
        }
    }

    // Вход в систему
    async loginUser(credentials) {
        const { email, password } = credentials;
        
        const requestData = {
            usernameOrEmail: email,
            password: password
        };

        try {
            const response = await this.makeRequest(`${this.baseURLs.userService}/login`, {
                method: 'POST',
                body: JSON.stringify(requestData)
            });
            
            // Сохраняем токен
            this.authToken = response.token;
            localStorage.setItem('authToken', this.authToken);
            
            return response;
        } catch (error) {
            console.error('Login failed:', error);
            throw error;
        }
    }

    // Получение текущего пользователя
    getCurrentUser() {
        const userData = localStorage.getItem('currentUser');
        return userData ? JSON.parse(userData) : null;
    }

    // Выход из системы
    logout() {
        this.authToken = null;
        localStorage.removeItem('authToken');
    }

    // ==================== TRIP SERVICE ====================
    
    // Получение всех маршрутов
    async getAllTrips() {
        try {
            return await this.makeRequest(`${this.baseURLs.tripService}`);
        } catch (error) {
            console.error('Failed to fetch trips:', error);
            return [];
        }
    }

    // Получение маршрута по ID
    async getTripById(tripId) {
        return await this.makeRequest(`${this.baseURLs.tripService}/${tripId}`);
    }

    // Поиск маршрутов по направлению
    async searchTripsByDestination(destination) {
        try {
            return await this.makeRequest(`${this.baseURLs.tripService}/search?destination=${encodeURIComponent(destination)}`);
        } catch (error) {
            console.error('Search failed:', error);
            return [];
        }
    }

    // Создание нового маршрута
    async createTrip(tripData) {
        return await this.makeRequest(`${this.baseURLs.tripService}`, {
            method: 'POST',
            body: JSON.stringify(tripData)
        });
    }

    // ==================== REVIEW SERVICE ====================
    
    // Получение всех отзывов
    async getAllReviews() {
        try {
            return await this.makeRequest(`${this.baseURLs.reviewService}`);
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
            return [];
        }
    }

    // Получение отзывов по типу (TRIP, PLACE, USER)
    async getReviewsByType(reviewType) {
        try {
            return await this.makeRequest(`${this.baseURLs.reviewService}/type/${reviewType}`);
        } catch (error) {
            console.error('Failed to fetch reviews by type:', error);
            return [];
        }
    }

    // Получение отзывов для конкретного объекта
    async getReviewsByReviewableId(reviewableId, reviewType = 'TRIP') {
        try {
            return await this.makeRequest(`${this.baseURLs.reviewService}/reviewable/${reviewableId}/type/${reviewType}`);
        } catch (error) {
            console.error('Failed to fetch reviews for object:', error);
            return [];
        }
    }

    // Получение среднего рейтинга
    async getAverageRating(reviewableId, reviewType = 'TRIP') {
        try {
            return await this.makeRequest(`${this.baseURLs.reviewService}/reviewable/${reviewableId}/type/${reviewType}/average`);
        } catch (error) {
            console.error('Failed to get average rating:', error);
            return 0;
        }
    }

    // Создание нового отзыва
    async createReview(reviewData) {
        return await this.makeRequest(`${this.baseURLs.reviewService}`, {
            method: 'POST',
            body: JSON.stringify(reviewData)
        });
    }

    // ==================== MATCH SERVICE ====================
    
    // Получение всех совпадений
    async getAllMatches() {
        try {
            return await this.makeRequest(`${this.baseURLs.matchService}`);
        } catch (error) {
            console.error('Failed to fetch matches:', error);
            return [];
        }
    }

    // Получение совпадений по ID поездки
    async getMatchesByTripId(tripId) {
        try {
            return await this.makeRequest(`${this.baseURLs.matchService}/trip/${tripId}`);
        } catch (error) {
            console.error('Failed to fetch matches for trip:', error);
            return [];
        }
    }

    // Создание нового совпадения (поиск попутчиков)
    async createMatch(matchData) {
        return await this.makeRequest(`${this.baseURLs.matchService}`, {
            method: 'POST',
            body: JSON.stringify(matchData)
        });
    }

    // Принятие совпадения
    async acceptMatch(matchId) {
        return await this.makeRequest(`${this.baseURLs.matchService}/${matchId}/accept`, {
            method: 'PUT'
        });
    }

    // Отклонение совпадения
    async rejectMatch(matchId) {
        return await this.makeRequest(`${this.baseURLs.matchService}/${matchId}/reject`, {
            method: 'PUT'
        });
    }
}

// Создаем глобальный экземпляр API
window.travelioAPI = new TravelioAPI();

// Просмотр деталей маршрута
async function viewTripDetails(tripId) {
    try {
        const trip = await window.travelioAPI.getTripById(tripId);
        
        // Показываем модальное окно с деталями маршрута
        const modal = document.createElement('div');
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content">
                <span class="close">&times;</span>
                <h3>${trip.destination || 'Маршрут'}</h3>
                <div class="trip-details">
                    <p><strong>Тип:</strong> ${trip.tripType || 'Пеший'}</p>
                    <p><strong>Сложность:</strong> ${trip.difficulty || 'Средняя'}</p>
                    <p><strong>Регион:</strong> ${trip.region || 'Горы'}</p>
                    <p><strong>Длительность:</strong> ${trip.duration || 3} дня</p>
                    <p><strong>Цена:</strong> ${trip.price || 0} руб.</p>
                    <p><strong>Дата начала:</strong> ${new Date(trip.startDate).toLocaleDateString()}</p>
                    <p><strong>Дата окончания:</strong> ${new Date(trip.endDate).toLocaleDateString()}</p>
                    ${trip.description ? `<p><strong>Описание:</strong> ${trip.description}</p>` : ''}
                </div>
                <div class="trip-actions">
                    <button onclick="findCompanions(${trip.id})">Найти попутчиков</button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        // Закрытие модального окна
        modal.querySelector('.close').onclick = () => modal.remove();
        modal.onclick = (e) => {
            if (e.target === modal) modal.remove();
        };
        
    } catch (error) {
        console.error('Failed to load trip details:', error);
        showMessage('Не удалось загрузить детали маршрута', 'error');
    }
}

// ==================== UI INTEGRATION FUNCTIONS ====================

// Инициализация навигации на всех страницах
function initializeNavigation() {
    if (window.travelioAPI) {
        window.travelioAPI.initializeNavigation();
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    initializeNavigation();
});

// Обработка формы регистрации
async function handleRegistration(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Валидация полей
    const username = formData.get('username');
    const email = formData.get('email');
    const password = formData.get('password');
    const firstName = formData.get('firstName');
    const lastName = formData.get('lastName');
    const phone = formData.get('phone');
    
    // Очищаем предыдущие ошибки
    clearFormErrors();
    
    // Валидация
    let hasErrors = false;
    
    if (!username || username.length < 3 || username.length > 50) {
        showFieldError('username-error', 'Имя пользователя должно быть от 3 до 50 символов');
        hasErrors = true;
    }
    
    if (!email || !isValidEmail(email)) {
        showFieldError('email-error', 'Введите корректный email адрес');
        hasErrors = true;
    }
    
    if (!password || password.length < 6) {
        showFieldError('password-error', 'Пароль должен содержать минимум 6 символов');
        hasErrors = true;
    }
    
    if (!firstName || firstName.length > 50) {
        showFieldError('firstName-error', 'Имя обязательно и не должно превышать 50 символов');
        hasErrors = true;
    }
    
    if (!lastName || lastName.length > 50) {
        showFieldError('lastName-error', 'Фамилия обязательна и не должна превышать 50 символов');
        hasErrors = true;
    }
    
    if (hasErrors) {
        return;
    }
    
    const userData = {
        username: username,
        email: email,
        password: password,
        firstName: firstName,
        lastName: lastName,
        phone: phone || null
    };

        try {
            const response = await window.travelioAPI.registerUser(userData);
        
        // Создаем объект пользователя из ответа
        const user = {
            id: response.id,
            username: response.username,
            email: response.email,
            firstName: userData.firstName,
            lastName: userData.lastName,
            phone: userData.phone,
            role: response.role
        };
        
        // Сохраняем данные пользователя
        localStorage.setItem('currentUser', JSON.stringify(user));
        localStorage.setItem('authToken', response.token || 'dummy-token');
        
        // Показываем успешное сообщение
        showFormMessage('Регистрация успешна! Добро пожаловать в Travelio!', 'success');
        
        // Возвращаем успешный результат
        return Promise.resolve(response);
        
    } catch (error) {
        console.error('Registration error:', error);
        
        // Показываем понятное сообщение об ошибке на русском языке
        let errorMessage = error.message;
        
        // Дополнительная обработка специфичных ошибок
        if (errorMessage.includes('Failed to fetch')) {
            errorMessage = 'Не удается подключиться к серверу. Проверьте подключение к интернету.';
        } else if (errorMessage.includes('NetworkError')) {
            errorMessage = 'Ошибка сети. Сервер недоступен.';
        } else if (errorMessage.includes('timeout')) {
            errorMessage = 'Превышено время ожидания. Попробуйте снова.';
        }
        
        showFormMessage(errorMessage, 'error');
        
        // Возвращаем ошибку
        return Promise.reject(error);
    }
}

// Вспомогательные функции для валидации
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showFieldError(errorId, message) {
    const errorElement = document.getElementById(errorId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.classList.add('show');
    }
    
    // Подсвечиваем поле с ошибкой
    const fieldName = errorId.replace('-error', '');
    const field = document.getElementById(fieldName);
    if (field) {
        field.classList.add('error');
    }
}

function clearFormErrors() {
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(element => {
        element.classList.remove('show');
        element.textContent = '';
    });
    
    const fields = document.querySelectorAll('.register-form input');
    fields.forEach(field => {
        field.classList.remove('error');
    });
    
    const messageElement = document.getElementById('form-message');
    if (messageElement) {
        messageElement.className = 'form-message';
    }
}

function showFormMessage(message, type) {
    const messageElement = document.getElementById('form-message');
    if (messageElement) {
        messageElement.textContent = message;
        messageElement.className = `form-message ${type}`;
    }
}

// Загрузка маршрутов на страницу routes.html
async function loadTrips() {
    try {
        const trips = await window.travelioAPI.getAllTrips();
        displayTrips(trips);
        // Применяем фильтры после загрузки маршрутов
        setTimeout(() => {
            if (typeof applyFilters === 'function') {
                applyFilters();
            }
        }, 100);
    } catch (error) {
        console.error('Failed to load trips:', error);
        showMessage('Не удалось загрузить маршруты', 'error');
    }
}

// Отображение маршрутов в сетке
function displayTrips(trips) {
    const routesGrid = document.getElementById('routesGrid');
    if (!routesGrid) return;

    // Очищаем существующие карточки
    routesGrid.innerHTML = '';

    if (trips.length === 0) {
        routesGrid.innerHTML = '<p>Маршруты не найдены</p>';
        return;
    }

    trips.forEach(trip => {
        console.log('Полученные данные маршрута:', trip);
        
        const card = document.createElement('div');
        card.className = 'card';
        card.setAttribute('data-title', trip.destination || 'Без названия');
        card.setAttribute('data-type', trip.tripType || 'пеший');
        card.setAttribute('data-difficulty', trip.difficulty || 'средняя');
        card.setAttribute('data-region', trip.region || 'горы');
        card.setAttribute('data-days', trip.duration || 3);

        // Определяем изображение на основе типа маршрута
        let imageSrc = 'images/route1.jpg'; // По умолчанию
        if (trip.tripType === 'водный') {
            imageSrc = 'images/route2.jpg';
        } else if (trip.tripType === 'велосипед') {
            imageSrc = 'images/route3.jpg';
        } else if (trip.tripType === 'пеший') {
            imageSrc = 'images/route1.jpg';
        }

        card.innerHTML = `
            <img src="${imageSrc}" alt="${trip.destination || 'Маршрут'}">
            <h3>${trip.destination || 'Без названия'} — ${trip.duration || 3} дня</h3>
            <div class="meta">
                <span>${trip.tripType || 'Пеший'}</span>
                <span>${trip.difficulty || 'Средняя'}</span>
            </div>
            <div class="trip-actions">
                <button onclick="viewTripDetails(${trip.id})">Подробнее</button>
                <button onclick="findCompanions(${trip.id})">Найти попутчиков</button>
            </div>
        `;

        routesGrid.appendChild(card);
    });
}

// Загрузка отзывов на страницу reviews.html
async function loadReviews() {
    try {
        const reviews = await window.travelioAPI.getReviewsByType('TRIP');
        displayReviews(reviews);
    } catch (error) {
        console.error('Failed to load reviews:', error);
        showMessage('Не удалось загрузить отзывы', 'error');
    }
}

// Отображение отзывов
function displayReviews(reviews) {
    const reviewsCards = document.querySelector('.reviews-cards');
    if (!reviewsCards) return;

    // Очищаем существующие отзывы
    reviewsCards.innerHTML = '';

    if (reviews.length === 0) {
        reviewsCards.innerHTML = '<p>Отзывы не найдены</p>';
        return;
    }

    reviews.forEach(review => {
        const reviewElement = document.createElement('div');
        reviewElement.className = 'review-card';
        
        // Определяем изображение на основе ID пользователя
        let reviewImageSrc = 'images/review1.jpg'; // По умолчанию
        if (review.userId % 2 === 0) {
            reviewImageSrc = 'images/review2.jpg';
        }
        
        reviewElement.innerHTML = `
            <img src="${reviewImageSrc}" alt="Пользователь #${review.userId}">
            <div class="review-content">
                <h4>Пользователь #${review.userId}</h4>
                <p>${review.content}</p>
                <div class="rating">⭐ ${review.rating}/5</div>
            </div>
        `;

        reviewsCards.appendChild(reviewElement);
    });
}

// Поиск попутчиков для маршрута
async function findCompanions(tripId) {
    try {
        const matches = await window.travelioAPI.getMatchesByTripId(tripId);
        
        if (matches.length === 0) {
            showMessage('Попутчики для этого маршрута не найдены', 'info');
            return;
        }

        // Показываем модальное окно с попутчиками
        showCompanionsModal(matches);
        
    } catch (error) {
        console.error('Failed to find companions:', error);
        showMessage('Не удалось найти попутчиков', 'error');
    }
}

// Показ модального окна с попутчиками
function showCompanionsModal(matches) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close">&times;</span>
            <h3>Найденные попутчики</h3>
            <div class="companions-list">
                ${matches.map(match => `
                    <div class="companion-card">
                        <h4>Пользователь #${match.userId}</h4>
                        <p>Направление: ${match.destination}</p>
                        <p>Дата отправления: ${new Date(match.departureDate).toLocaleDateString()}</p>
                        <div class="companion-actions">
                            <button onclick="acceptCompanion(${match.id})">Принять</button>
                            <button onclick="rejectCompanion(${match.id})">Отклонить</button>
                        </div>
                    </div>
                `).join('')}
            </div>
        </div>
    `;

    document.body.appendChild(modal);

    // Закрытие модального окна
    modal.querySelector('.close').onclick = () => modal.remove();
    modal.onclick = (e) => {
        if (e.target === modal) modal.remove();
    };
}

// Принятие попутчика
async function acceptCompanion(matchId) {
    try {
        await window.travelioAPI.acceptMatch(matchId);
        showMessage('Попутчик принят!', 'success');
        // Закрываем модальное окно
        document.querySelector('.modal')?.remove();
    } catch (error) {
        showMessage('Не удалось принять попутчика', 'error');
    }
}

// Отклонение попутчика
async function rejectCompanion(matchId) {
    try {
        await window.travelioAPI.rejectMatch(matchId);
        showMessage('Попутчик отклонен', 'info');
        // Закрываем модальное окно
        document.querySelector('.modal')?.remove();
    } catch (error) {
        showMessage('Не удалось отклонить попутчика', 'error');
    }
}

// Показ модального окна создания маршрута
function showCreateRouteModal() {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="close">&times;</span>
            <h3>Создать новый маршрут</h3>
            <form id="createRouteForm" class="create-route-form">
                <div class="form-group">
                    <label for="routeDestination">Название маршрута:</label>
                    <input type="text" id="routeDestination" name="destination" required placeholder="Например: Горный перевал">
                </div>
                <div class="form-group">
                    <label for="routeType">Тип маршрута:</label>
                    <select id="routeType" name="tripType" required>
                        <option value="">Выберите тип</option>
                        <option value="пеший">Пеший</option>
                        <option value="велосипед">Велосипед</option>
                        <option value="водный">Водный</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="routeDifficulty">Сложность:</label>
                    <select id="routeDifficulty" name="difficulty" required>
                        <option value="">Выберите сложность</option>
                        <option value="лёгкая">Лёгкая</option>
                        <option value="средняя">Средняя</option>
                        <option value="сложная">Сложная</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="routeRegion">Регион:</label>
                    <select id="routeRegion" name="region" required>
                        <option value="">Выберите регион</option>
                        <option value="горы">Горы</option>
                        <option value="лес">Лес</option>
                        <option value="побережье">Побережье</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="routeDuration">Длительность (дни):</label>
                    <input type="number" id="routeDuration" name="duration" required min="1" max="30" placeholder="3">
                </div>
                <div class="form-group">
                    <label for="routePrice">Цена (руб.):</label>
                    <input type="number" id="routePrice" name="price" required min="0" step="0.01" placeholder="5000">
                </div>
                <div class="form-group">
                    <label for="routeStartDate">Дата начала:</label>
                    <input type="date" id="routeStartDate" name="startDate" required>
                </div>
                <div class="form-group">
                    <label for="routeEndDate">Дата окончания:</label>
                    <input type="date" id="routeEndDate" name="endDate" required>
                </div>
                <div class="form-group full-width">
                    <label for="routeDescription">Описание:</label>
                    <textarea id="routeDescription" name="description" required placeholder="Опишите маршрут..."></textarea>
                </div>
                <div class="form-actions">
                    <button type="button" onclick="closeCreateRouteModal()" class="btn btn-secondary">Отмена</button>
                    <button type="submit" class="btn">Создать маршрут</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);

    // Закрытие модального окна
    modal.querySelector('.close').onclick = () => modal.remove();
    modal.onclick = (e) => {
        if (e.target === modal) modal.remove();
    };

    // Обработка формы
    const form = modal.querySelector('#createRouteForm');
    form.addEventListener('submit', handleCreateRoute);
    
    // Валидация дат
    const startDateInput = modal.querySelector('#routeStartDate');
    const endDateInput = modal.querySelector('#routeEndDate');
    
    startDateInput.addEventListener('change', function() {
        endDateInput.min = this.value;
        if (endDateInput.value && endDateInput.value < this.value) {
            endDateInput.value = this.value;
        }
    });
}

// Закрытие модального окна создания маршрута
function closeCreateRouteModal() {
    const modal = document.querySelector('.modal');
    if (modal) modal.remove();
}

// Обработка создания маршрута
async function handleCreateRoute(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Получаем текущего пользователя (или используем ID по умолчанию)
    let userId = 1; // По умолчанию
    try {
        const currentUser = window.travelioAPI.getCurrentUser();
        userId = currentUser.id;
    } catch (error) {
        console.log('User not logged in, using default userId');
    }
    
    const routeData = {
        destination: formData.get('destination'),
        description: formData.get('description'),
        startDate: formData.get('startDate'),
        endDate: formData.get('endDate'),
        price: parseFloat(formData.get('price')) || 0,
        userId: userId,
        tripType: formData.get('tripType'),
        difficulty: formData.get('difficulty'),
        region: formData.get('region'),
        duration: parseInt(formData.get('duration'))
    };

    console.log('Отправляемые данные маршрута:', routeData);

    try {
        const newRoute = await window.travelioAPI.createTrip(routeData);
        
        showMessage('Маршрут успешно создан!', 'success');
        
        // Закрываем модальное окно
        closeCreateRouteModal();
        
        // Перезагружаем список маршрутов
        await loadTrips();
        
    } catch (error) {
        console.error('Failed to create route:', error);
        showMessage(`Ошибка создания маршрута: ${error.message}`, 'error');
    }
}

// Показ сообщений пользователю
function showMessage(message, type = 'info') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;
    
    // Добавляем стили для сообщений
    messageDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        z-index: 1000;
        max-width: 300px;
        ${type === 'success' ? 'background-color: #4CAF50;' : ''}
        ${type === 'error' ? 'background-color: #f44336;' : ''}
        ${type === 'info' ? 'background-color: #2196F3;' : ''}
    `;
    
    document.body.appendChild(messageDiv);
    
    // Удаляем сообщение через 5 секунд
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    // Привязываем обработчик формы регистрации
    const regForm = document.getElementById('regForm');
    if (regForm) {
        regForm.addEventListener('submit', handleRegistration);
    }

    // Загружаем данные в зависимости от страницы
    const currentPage = window.location.pathname.split('/').pop();
    
    switch (currentPage) {
        case 'routes.html':
            loadTrips();
            break;
        case 'reviews.html':
            loadReviews();
            break;
    }
});

