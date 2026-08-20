package com.armalora.order.service;

import com.armalora.order.dto.CreateOrderRequest;
import com.armalora.order.dto.OrderItemRequest;
import com.armalora.order.dto.OrderItemResponse;
import com.armalora.order.dto.OrderResponse;
import com.armalora.order.entity.Order;
import com.armalora.order.entity.OrderItem;
import com.armalora.order.entity.OrderStatus;
import com.armalora.order.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        order.setOrderNumber(
                generateOrderNumber()
        );

        order.setUserId(userId);

        order.setStatus(
                OrderStatus.PENDING
        );

        order.setCreatedAt(
                LocalDateTime.now()
        );

        order.setUpdatedAt(
                LocalDateTime.now()
        );

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (OrderItemRequest itemRequest :
                request.getItems()) {

            OrderItem item =
                    new OrderItem();

            item.setProductId(
                    itemRequest.getProductId()
            );

            item.setVariantId(
                    itemRequest.getVariantId()
            );

            item.setQuantity(
                    itemRequest.getQuantity()
            );

            /*
             * Product price will be retrieved from
             * Product Service in a later batch.
             *
             * For now this is a temporary value
             * so that the order workflow can be
             * implemented and tested independently.
             */
            BigDecimal unitPrice =
                    BigDecimal.ZERO;

            item.setUnitPrice(unitPrice);

            BigDecimal subtotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

            item.setSubtotal(subtotal);

            item.setOrder(order);

            order.getItems().add(item);

            totalAmount =
                    totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder =
                orderRepository.save(order);

        return convertToResponse(savedOrder);
    }

    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(
            Long id
    ) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(order);
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
                        .findByOrderNumber(orderNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found with order number: "
                                                + orderNumber
                                )
                        );

        return convertToResponse(order);
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
                                new RuntimeException(
                                        "Order not found"
                                )
                        );

        return convertToResponse(order);
    }

    // =========================================================
    // GENERATE ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        return "ARM-"
                + System.currentTimeMillis()
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    // =========================================================
    // ENTITY → RESPONSE
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

        response.setTotalAmount(
                order.getTotalAmount()
        );

        response.setStatus(
                order.getStatus()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        response.setUpdatedAt(
                order.getUpdatedAt()
        );

        List<OrderItemResponse> itemResponses =
                order.getItems()
                        .stream()
                        .map(this::convertItemToResponse)
                        .toList();

        response.setItems(
                itemResponses
        );

        return response;
    }

    // =========================================================
    // ORDER ITEM → RESPONSE
    // =========================================================

    private OrderItemResponse convertItemToResponse(
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

        response.setProductName(
                item.getProductName()
        );

        response.setUnitPrice(
                item.getUnitPrice()
        );

        response.setQuantity(
                item.getQuantity()
        );

        response.setSubtotal(
                item.getSubtotal()
        );

        return response;
    }
}