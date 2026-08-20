package com.armalora.order.service;

import com.armalora.order.client.InventoryClient;
import com.armalora.order.client.ProductClient;
import com.armalora.order.dto.CreateOrderRequest;
import com.armalora.order.dto.InventoryResponse;
import com.armalora.order.dto.OrderItemRequest;
import com.armalora.order.dto.OrderItemResponse;
import com.armalora.order.dto.OrderResponse;
import com.armalora.order.dto.ProductResponse;
import com.armalora.order.entity.Order;
import com.armalora.order.entity.OrderItem;
import com.armalora.order.entity.OrderStatus;
import com.armalora.order.exception.InsufficientInventoryException;
import com.armalora.order.exception.InvalidOrderStatusException;
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
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    public OrderService(
            OrderRepository orderRepository,
            ProductClient productClient,
            InventoryClient inventoryClient
    ) {

        this.orderRepository =
                orderRepository;

        this.productClient =
                productClient;

        this.inventoryClient =
                inventoryClient;
    }

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

        // Authenticated user
        order.setUserId(userId);

        // New orders start as PENDING
        order.setStatus(
                OrderStatus.PENDING
        );

        // Shipping address
        order.setShippingAddress(
                request.getShippingAddress()
        );

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        List<OrderItem> orderItems =
                new ArrayList<>();

        for (OrderItemRequest itemRequest :
                request.getItems()) {

            ProductResponse product =
                    productClient.getProductById(
                            itemRequest.getProductId()
                    );


            if (product == null) {

                throw new RuntimeException(
                        "Product not found: "
                                + itemRequest.getProductId()
                );
            }

            if (Boolean.FALSE.equals(
                    product.getActive()
            )) {

                throw new RuntimeException(
                        "Product is not active: "
                                + itemRequest.getProductId()
                );
            }


            InventoryResponse inventory;

            if (itemRequest.getVariantId() != null) {

                inventory =
                        inventoryClient
                                .getInventoryByProductAndVariant(
                                        itemRequest.getProductId(),
                                        itemRequest.getVariantId()
                                );

            } else {

                List<InventoryResponse>
                        inventories =
                        inventoryClient
                                .getInventoryByProductId(
                                        itemRequest.getProductId()
                                );

                if (inventories.isEmpty()) {

                    throw new RuntimeException(
                            "Inventory not found for product: "
                                    + itemRequest.getProductId()
                    );
                }

                inventory =
                        inventories.get(0);
            }

            if (inventory == null) {

                throw new RuntimeException(
                        "Inventory not found for product: "
                                + itemRequest.getProductId()
                );
            }

            Integer availableQuantity =
                    inventory.getAvailableQuantity();

            if (availableQuantity == null) {

                availableQuantity =
                        inventory.getQuantity()
                                - inventory
                                .getReservedQuantity();
            }

            if (availableQuantity
                    < itemRequest.getQuantity()) {

                throw new InsufficientInventoryException(
                        itemRequest.getProductId(),
                        itemRequest.getVariantId(),
                        itemRequest.getQuantity(),
                        availableQuantity
                );
            }


            BigDecimal unitPrice =
                    product.getPrice();

            BigDecimal subtotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

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
                    unitPrice
            );

            orderItem.setSubtotal(
                    subtotal
            );

            orderItem.setOrder(
                    order
            );

            orderItems.add(
                    orderItem
            );

            // Add subtotal to total
            totalAmount =
                    totalAmount.add(
                            subtotal
                    );
        }


        order.setItems(
                orderItems
        );

        order.setTotalAmount(
                totalAmount
        );

        Order savedOrder =
                orderRepository.save(order);

        return convertToResponse(
                savedOrder
        );
    }

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

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(
            Long userId
    ) {

        return orderRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long id,
            OrderStatus newStatus
    ) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        id
                                )
                        );

        OrderStatus currentStatus =
                order.getStatus();

        if (!isValidStatusTransition(
                currentStatus,
                newStatus
        )) {

            throw new InvalidOrderStatusException(
                    "Cannot change order status from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }

        order.setStatus(
                newStatus
        );

        Order updatedOrder =
                orderRepository.save(order);

        return convertToResponse(
                updatedOrder
        );
    }

    private boolean isValidStatusTransition(
            OrderStatus current,
            OrderStatus next
    ) {

        if (current == OrderStatus.PENDING) {

            return next == OrderStatus.CONFIRMED
                    || next == OrderStatus.CANCELLED;
        }

        if (current == OrderStatus.CONFIRMED) {

            return next == OrderStatus.PROCESSING
                    || next == OrderStatus.CANCELLED;
        }

        if (current == OrderStatus.PROCESSING) {

            return next == OrderStatus.SHIPPED;
        }

        if (current == OrderStatus.SHIPPED) {

            return next == OrderStatus.DELIVERED;
        }

        return false;
    }

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