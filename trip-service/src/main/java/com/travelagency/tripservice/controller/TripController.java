package com.travelagency.tripservice.controller;

import com.travelagency.tripservice.dto.CreateTripRequest;
import com.travelagency.tripservice.dto.TripResponse;
import com.travelagency.tripservice.dto.ReviewSummaryDto;
import com.travelagency.tripservice.entity.TripStatus;
import com.travelagency.tripservice.service.TripService;
import com.travelagency.tripservice.service.TripReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
@Tag(name = "Trip Management", description = "API для управления поездками")
public class TripController {
    
    @Autowired
    private TripService tripService;
    
    @Autowired
    private TripReviewService tripReviewService;
    
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
        TripResponse response = tripService.createTrip(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Получить поездку по ID", description = "Возвращает информацию о поездке по указанному идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Поездка найдена",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class))),
        @ApiResponse(responseCode = "404", description = "Поездка не найдена"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(
            @Parameter(description = "Идентификатор поездки", required = true)
            @PathVariable Long id) {
        TripResponse response = tripService.getTripById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Получить все поездки", description = "Возвращает список всех поездок")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список поездок получен",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponse.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        List<TripResponse> responses = tripService.getAllTrips();
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Получить поездки пользователя", description = "Возвращает все поездки конкретного пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список поездок пользователя получен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripResponse>> getTripsByUserId(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable Long userId) {
        List<TripResponse> responses = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Получить поездки по статусу", description = "Возвращает все поездки с указанным статусом")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список поездок по статусу получен"),
        @ApiResponse(responseCode = "400", description = "Неверный статус")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TripResponse>> getTripsByStatus(
            @Parameter(description = "Статус поездки", required = true)
            @PathVariable TripStatus status) {
        List<TripResponse> responses = tripService.getTripsByStatus(status);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Поиск поездок по направлению", description = "Возвращает поездки, содержащие указанное направление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Результаты поиска получены"),
        @ApiResponse(responseCode = "400", description = "Пустой параметр поиска")
    })
    @GetMapping("/search")
    public ResponseEntity<List<TripResponse>> searchTripsByDestination(
            @Parameter(description = "Направление для поиска", required = true)
            @RequestParam String destination) {
        List<TripResponse> responses = tripService.searchTripsByDestination(destination);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "Обновить статус поездки", description = "Изменяет статус указанной поездки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Статус поездки обновлен"),
        @ApiResponse(responseCode = "404", description = "Поездка не найдена"),
        @ApiResponse(responseCode = "400", description = "Неверный статус")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<TripResponse> updateTripStatus(
            @Parameter(description = "Идентификатор поездки", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Новый статус поездки", required = true)
            @RequestParam TripStatus status) {
        TripResponse response = tripService.updateTripStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Удалить поездку", description = "Удаляет поездку по указанному идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Поездка успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Поездка не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @Parameter(description = "Идентификатор поездки", required = true)
            @PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
    
    // Circuit Breaker Demo Endpoints
    
    @Operation(summary = "Получить сводку отзывов поездки", description = "Получает сводку отзывов для указанной поездки с использованием Circuit Breaker")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сводка отзывов получена",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewSummaryDto.class))),
        @ApiResponse(responseCode = "404", description = "Поездка не найдена"),
        @ApiResponse(responseCode = "503", description = "Сервис отзывов недоступен")
    })
    @GetMapping("/{id}/reviews/summary")
    public ResponseEntity<ReviewSummaryDto> getTripReviewSummary(
            @Parameter(description = "Идентификатор поездки", required = true)
            @PathVariable Long id) {
        ReviewSummaryDto response = tripReviewService.getTripReviewSummary(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Получить сводку отзывов поездки (асинхронно)", description = "Получает сводку отзывов для указанной поездки асинхронно с использованием Circuit Breaker")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сводка отзывов получена асинхронно",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewSummaryDto.class))),
        @ApiResponse(responseCode = "404", description = "Поездка не найдена"),
        @ApiResponse(responseCode = "503", description = "Сервис отзывов недоступен")
    })
    @GetMapping("/{id}/reviews/summary/async")
    public CompletableFuture<ResponseEntity<ReviewSummaryDto>> getTripReviewSummaryAsync(
            @Parameter(description = "Идентификатор поездки", required = true)
            @PathVariable Long id) {
        return tripReviewService.getTripReviewSummaryAsync(id)
                .thenApply(ResponseEntity::ok);
    }
}
