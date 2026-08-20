package com.armalora.inventory.controller;

import com.armalora.inventory.dto.InventoryRequest;
import com.armalora.inventory.dto.InventoryResponse;
import com.armalora.inventory.dto.StockOperationRequest;
import com.armalora.inventory.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService) {

        this.inventoryService =
                inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse>
    createInventory(
            @Valid @RequestBody InventoryRequest request) {

        return new ResponseEntity<>(
                inventoryService.createInventory(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>>
    getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse>
    getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id)
        );
    }

    @GetMapping(
            "/product/{productId}/variant/{variantId}"
    )
    public ResponseEntity<InventoryResponse>
    getInventoryByProductAndVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                inventoryService
                        .getInventoryByProductAndVariant(
                                productId,
                                variantId
                        )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse>
    updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryResponse>>
    getInventoryByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProductId(
                        productId
                )
        );
    }

    @PostMapping("/{id}/increase")
    public ResponseEntity<InventoryResponse> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockOperationRequest request) {

        return ResponseEntity.ok(
                inventoryService.increaseStock(
                        id,
                        request.getAmount()
                )
        );
    }
    @PostMapping("/{id}/decrease")
    public ResponseEntity<InventoryResponse> decreaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockOperationRequest request) {

        return ResponseEntity.ok(
                inventoryService.decreaseStock(
                        id,
                        request.getAmount()
                )
        );
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<InventoryResponse> reserveStock(
            @PathVariable Long id,
            @Valid @RequestBody StockOperationRequest request) {

        return ResponseEntity.ok(
                inventoryService.reserveStock(
                        id,
                        request.getAmount()
                )
        );
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<InventoryResponse> releaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockOperationRequest request) {

        return ResponseEntity.ok(
                inventoryService.releaseStock(
                        id,
                        request.getAmount()
                )
        );
    }
}