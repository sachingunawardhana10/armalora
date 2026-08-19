package com.armalora.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class GatewayFallbackController {

    @GetMapping("/fallback/product")
    public ResponseEntity<Map<String, Object>> productFallback() {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "service", "product-service",
                        "message", "Product service is temporarily unavailable"
                ));
    }

    @GetMapping("/fallback/inventory")
    public ResponseEntity<Map<String, Object>> inventoryFallback() {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "service", "inventory-service",
                        "message", "Inventory service is temporarily unavailable"
                ));
    }

    @GetMapping("/fallback/user")
    public ResponseEntity<Map<String, Object>> userFallback() {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "service", "user-service",
                        "message", "User service is temporarily unavailable"
                ));
    }
}