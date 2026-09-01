package com.armalora.inventory.service;

import com.armalora.inventory.dto.InventoryRequest;
import com.armalora.inventory.dto.InventoryReservationResponse;
import com.armalora.inventory.dto.InventoryResponse;
import com.armalora.inventory.dto.ReserveInventoryRequest;
import com.armalora.inventory.entity.Inventory;
import com.armalora.inventory.exception.InventoryAlreadyExistsException;
import com.armalora.inventory.exception.InventoryNotFoundException;
import com.armalora.inventory.exception.InvalidInventoryOperationException;
import com.armalora.inventory.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(
            InventoryRepository inventoryRepository) {

        this.inventoryRepository =
                inventoryRepository;
    }

    // =========================================================
    // CREATE INVENTORY
    // =========================================================

    public InventoryResponse createInventory(
            InventoryRequest request) {

        validateInventoryValues(request);

        if (request.getVariantId() != null) {

            if (inventoryRepository
                    .existsByProductIdAndVariantId(
                            request.getProductId(),
                            request.getVariantId()
                    )) {

                throw new InventoryAlreadyExistsException(
                        "Inventory already exists for product "
                                + request.getProductId()
                                + " and variant "
                                + request.getVariantId()
                );
            }

        } else {

            if (inventoryRepository
                    .existsByProductIdAndVariantIdIsNull(
                            request.getProductId()
                    )) {

                throw new InventoryAlreadyExistsException(
                        "Inventory already exists for product "
                                + request.getProductId()
                );
            }
        }

        Inventory inventory = new Inventory();

        inventory.setProductId(
                request.getProductId()
        );

        inventory.setVariantId(
                request.getVariantId()
        );

        inventory.setQuantity(
                request.getQuantity()
        );

        inventory.setReservedQuantity(
                request.getReservedQuantity() != null
                        ? request.getReservedQuantity()
                        : 0
        );

        inventory.setReorderLevel(
                request.getReorderLevel() != null
                        ? request.getReorderLevel()
                        : 5
        );

        Inventory savedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(savedInventory);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public InventoryResponse getInventoryById(
            Long id) {

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        return convertToResponse(inventory);
    }

    // =========================================================
    // GET BY PRODUCT
    // =========================================================

    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryByProductId(
            Long productId) {

        return inventoryRepository
                .findByProductId(productId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // GET BY PRODUCT + VARIANT
    // =========================================================

    @Transactional(readOnly = true)
    public InventoryResponse
    getInventoryByProductAndVariant(
            Long productId,
            Long variantId) {

        Inventory inventory =
                inventoryRepository
                        .findByProductIdAndVariantId(
                                productId,
                                variantId
                        )
                        .orElseThrow(() ->
                                new InventoryNotFoundException(
                                        "Inventory not found for product "
                                                + productId
                                                + " and variant "
                                                + variantId
                                )
                        );

        return convertToResponse(inventory);
    }

    // =========================================================
    // UPDATE INVENTORY
    // =========================================================

    public InventoryResponse updateInventory(
            Long id,
            InventoryRequest request) {

        validateInventoryValues(request);

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        if (request.getReservedQuantity() != null
                && request.getReservedQuantity()
                > request.getQuantity()) {

            throw new InvalidInventoryOperationException(
                    "Reserved quantity cannot exceed total quantity"
            );
        }

        inventory.setProductId(
                request.getProductId()
        );

        inventory.setVariantId(
                request.getVariantId()
        );

        inventory.setQuantity(
                request.getQuantity()
        );

        inventory.setReservedQuantity(
                request.getReservedQuantity() != null
                        ? request.getReservedQuantity()
                        : 0
        );

        inventory.setReorderLevel(
                request.getReorderLevel() != null
                        ? request.getReorderLevel()
                        : 5
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(updatedInventory);
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void deleteInventory(Long id) {

        if (!inventoryRepository.existsById(id)) {

            throw new InventoryNotFoundException(id);
        }

        inventoryRepository.deleteById(id);
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateInventoryValues(
            InventoryRequest request) {

        int quantity =
                request.getQuantity();

        int reservedQuantity =
                request.getReservedQuantity() != null
                        ? request.getReservedQuantity()
                        : 0;

        if (reservedQuantity > quantity) {

            throw new InvalidInventoryOperationException(
                    "Reserved quantity cannot exceed total quantity"
            );
        }
    }

    public InventoryResponse increaseStock(
            Long id,
            int amount) {

        if (amount <= 0) {

            throw new InvalidInventoryOperationException(
                    "Increase amount must be greater than 0"
            );
        }

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        inventory.setQuantity(
                inventory.getQuantity() + amount
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(updatedInventory);
    }

    public InventoryResponse decreaseStock(
            Long id,
            int amount) {

        if (amount <= 0) {

            throw new InvalidInventoryOperationException(
                    "Decrease amount must be greater than 0"
            );
        }

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        int availableQuantity =
                inventory.getQuantity()
                        - inventory.getReservedQuantity();

        if (amount > availableQuantity) {

            throw new InvalidInventoryOperationException(
                    "Cannot decrease stock by "
                            + amount
                            + ". Available quantity is "
                            + availableQuantity
            );
        }

        inventory.setQuantity(
                inventory.getQuantity() - amount
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(updatedInventory);
    }

    public InventoryResponse reserveStock(
            Long id,
            int amount) {

        if (amount <= 0) {

            throw new InvalidInventoryOperationException(
                    "Reserve amount must be greater than 0"
            );
        }

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        int availableQuantity =
                inventory.getQuantity()
                        - inventory.getReservedQuantity();

        if (amount > availableQuantity) {

            throw new InvalidInventoryOperationException(
                    "Cannot reserve "
                            + amount
                            + " units. Available quantity is "
                            + availableQuantity
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + amount
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(updatedInventory);
    }

    public InventoryResponse releaseStock(
            Long id,
            int amount) {

        if (amount <= 0) {

            throw new InvalidInventoryOperationException(
                    "Release amount must be greater than 0"
            );
        }

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

        if (amount > inventory.getReservedQuantity()) {

            throw new InvalidInventoryOperationException(
                    "Cannot release "
                            + amount
                            + " units. Reserved quantity is "
                            + inventory.getReservedQuantity()
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - amount
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return convertToResponse(updatedInventory);
    }

    // =========================================================
    // RESPONSE MAPPING
    // =========================================================

    private InventoryResponse convertToResponse(
            Inventory inventory) {

        InventoryResponse response =
                new InventoryResponse();

        response.setId(
                inventory.getId()
        );

        response.setProductId(
                inventory.getProductId()
        );

        response.setVariantId(
                inventory.getVariantId()
        );

        response.setQuantity(
                inventory.getQuantity()
        );

        response.setReservedQuantity(
                inventory.getReservedQuantity()
        );

        int availableQuantity =
                inventory.getQuantity()
                        - inventory.getReservedQuantity();

        response.setAvailableQuantity(
                availableQuantity
        );

        response.setReorderLevel(
                inventory.getReorderLevel()
        );

        response.setLowStock(
                availableQuantity
                        <= inventory.getReorderLevel()
        );

        response.setUpdatedAt(
                inventory.getUpdatedAt()
        );

        return response;
    }


}