package com.armalora.order.controller;

import com.armalora.order.dto.CreateOrderRequest;
import com.armalora.order.dto.OrderResponse;
import com.armalora.order.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {

        OrderResponse response =
                orderService.createOrder(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(
            @PathVariable String orderNumber
    ) {

        return ResponseEntity.ok(
                orderService.getOrderByNumber(
                        orderNumber
                )
        );
    }
    @GetMapping("/{orderNumber}/user/{userId}")
    public ResponseEntity<OrderResponse> getUserOrder(
            @PathVariable String orderNumber,
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                orderService.getUserOrder(
                        orderNumber,
                        userId
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>>
    getOrdersByUserId(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                orderService.getOrdersByUserId(
                        userId
                )
        );
    }
}