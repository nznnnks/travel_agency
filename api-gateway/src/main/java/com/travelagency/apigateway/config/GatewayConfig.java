package com.travelagency.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Trip Service Routes with Circuit Breaker
                .route("trip-service", r -> r
                        .path("/api/trips/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("trip-service")
                                        .setFallbackUri("forward:/fallback/trip-service"))
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, 
                                                  org.springframework.http.HttpMethod.POST,
                                                  org.springframework.http.HttpMethod.PUT,
                                                  org.springframework.http.HttpMethod.DELETE))
                        )
                        .uri("lb://trip-service"))
                
                // Review Service Routes with Circuit Breaker
                .route("review-service", r -> r
                        .path("/api/reviews/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("review-service")
                                        .setFallbackUri("forward:/fallback/review-service"))
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, 
                                                  org.springframework.http.HttpMethod.POST,
                                                  org.springframework.http.HttpMethod.PUT,
                                                  org.springframework.http.HttpMethod.DELETE))
                        )
                        .uri("lb://review-service"))
                
                // Match Service Routes with Circuit Breaker
                .route("match-service", r -> r
                        .path("/api/matches/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("match-service")
                                        .setFallbackUri("forward:/fallback/match-service"))
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, 
                                                  org.springframework.http.HttpMethod.POST,
                                                  org.springframework.http.HttpMethod.PUT,
                                                  org.springframework.http.HttpMethod.DELETE))
                        )
                        .uri("lb://match-service"))
                
                // User Service Routes with Circuit Breaker
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("user-service")
                                        .setFallbackUri("forward:/fallback/user-service"))
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, 
                                                  org.springframework.http.HttpMethod.POST,
                                                  org.springframework.http.HttpMethod.PUT,
                                                  org.springframework.http.HttpMethod.DELETE))
                        )
                        .uri("lb://user-service"))
                
                // Eureka Dashboard Route
                .route("eureka-server", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                
                .build();
    }
}

