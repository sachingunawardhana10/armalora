package com.armalora.inventory.service;

import com.armalora.inventory.dto.InventoryRequest;
import com.armalora.inventory.dto.InventoryResponse;
import com.armalora.inventory.entity.Inventory;
import com.armalora.inventory.exception.InventoryNotFoundException;
import com.armalora.inventory.repository.InventoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(
            InventoryRepository inventoryRepository) {

        this.inventoryRepository =
                inventoryRepository;
    }

    public InventoryResponse createInventory(
            InventoryRequest request) {

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

    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

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

    public InventoryResponse updateInventory(
            Long id,
            InventoryRequest request) {

        Inventory inventory =
                inventoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new InventoryNotFoundException(id)
                        );

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

    public void deleteInventory(Long id) {

        if (!inventoryRepository.existsById(id)) {

            throw new InventoryNotFoundException(id);
        }

        inventoryRepository.deleteById(id);
    }
    public List<InventoryResponse> getInventoryByProductId(
            Long productId) {

        return inventoryRepository
                .findByProductId(productId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

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