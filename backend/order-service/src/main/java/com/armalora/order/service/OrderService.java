package com.armalora.order.service;

import com.armalora.order.dto.CreateOrderRequest;
import com.armalora.order.dto.OrderItemRequest;
import com.armalora.order.dto.OrderItemResponse;
import com.armalora.order.dto.OrderResponse;
import com.armalora.order.entity.Order;
import com.armalora.order.entity.OrderItem;
import com.armalora.order.entity.OrderStatus;
import com.armalora.order.exception.OrderNotFoundException;
import com.armalora.order.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(
            OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    // =========================================================
    // CREATE ORDER
    // =========================================================

    @Transactional
    public OrderResponse createOrder(
            Long userId,
            CreateOrderRequest request
    ) {

        Order order = new Order();

        // Generate unique order number
        order.setOrderNumber(
                generateOrderNumber()
        );

        // Set authenticated user
        order.setUserId(userId);

        // New orders start as PENDING
        order.setStatus(
                OrderStatus.PENDING
        );

        // Set shipping address
        order.setShippingAddress(
                request.getShippingAddress()
        );

        // Calculate total amount
        BigDecimal totalAmount =
                calculateTotal(
                        request.getItems()
                );

        order.setTotalAmount(
                totalAmount
        );

        // Create order items
        List<OrderItem> orderItems =
                new ArrayList<>();

        for (OrderItemRequest itemRequest :
                request.getItems()) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setProductId(
                    itemRequest.getProductId()
            );

            orderItem.setVariantId(
                    itemRequest.getVariantId()
            );

            orderItem.setQuantity(
                    itemRequest.getQuantity()
            );

            orderItem.setUnitPrice(
                    itemRequest.getUnitPrice()
            );

            BigDecimal subtotal =
                    itemRequest
                            .getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            orderItem.setSubtotal(
                    subtotal
            );

            // Establish relationship
            orderItem.setOrder(
                    order
            );

            orderItems.add(
                    orderItem
            );
        }

        order.setItems(
                orderItems
        );

        // Save order and items
        Order savedOrder =
                orderRepository.save(order);

        return convertToResponse(
                savedOrder
        );
    }

    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(
            Long id
    ) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        id
                                )
                        );

        return convertToResponse(
                order
        );
    }

    // =========================================================
    // GET ORDER BY ORDER NUMBER
    // =========================================================

    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(
            String orderNumber
    ) {

        Order order =
                orderRepository
                        .findByOrderNumber(
                                orderNumber
                        )
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        orderNumber
                                )
                        );

        return convertToResponse(
                order
        );
    }

    // =========================================================
    // GET USER ORDER
    // =========================================================

    @Transactional(readOnly = true)
    public OrderResponse getUserOrder(
            String orderNumber,
            Long userId
    ) {

        Order order =
                orderRepository
                        .findByOrderNumberAndUserId(
                                orderNumber,
                                userId
                        )
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        orderNumber
                                )
                        );

        return convertToResponse(
                order
        );
    }

    // =========================================================
    // CALCULATE ORDER TOTAL
    // =========================================================

    private BigDecimal calculateTotal(
            List<OrderItemRequest> items
    ) {

        return items.stream()
                .map(item ->
                        item.getUnitPrice()
                                .multiply(
                                        BigDecimal.valueOf(
                                                item.getQuantity()
                                        )
                                )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    // =========================================================
    // GENERATE ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        return "ARM-"
                + UUID.randomUUID()
                .toString()
                .substring(
                        0,
                        8
                )
                .toUpperCase();
    }

    // =========================================================
    // CONVERT ORDER → RESPONSE
    // =========================================================

    private OrderResponse convertToResponse(
            Order order
    ) {

        OrderResponse response =
                new OrderResponse();

        response.setId(
                order.getId()
        );

        response.setOrderNumber(
                order.getOrderNumber()
        );

        response.setUserId(
                order.getUserId()
        );

        response.setStatus(
                order.getStatus()
        );

        response.setShippingAddress(
                order.getShippingAddress()
        );

        response.setTotalAmount(
                order.getTotalAmount()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        response.setUpdatedAt(
                order.getUpdatedAt()
        );

        // Convert order items
        if (order.getItems() != null) {

            List<OrderItemResponse>
                    itemResponses =
                    order.getItems()
                            .stream()
                            .map(
                                    this::convertItemToResponse
                            )
                            .toList();

            response.setItems(
                    itemResponses
            );
        }

        return response;
    }

    // =========================================================
    // CONVERT ORDER ITEM → RESPONSE
    // =========================================================

    private OrderItemResponse
    convertItemToResponse(
            OrderItem item
    ) {

        OrderItemResponse response =
                new OrderItemResponse();

        response.setId(
                item.getId()
        );

        response.setProductId(
                item.getProductId()
        );

        response.setVariantId(
                item.getVariantId()
        );

        response.setQuantity(
                item.getQuantity()
        );

        response.setUnitPrice(
                item.getUnitPrice()
        );

        response.setSubtotal(
                item.getSubtotal()
        );

        return response;
    }
}