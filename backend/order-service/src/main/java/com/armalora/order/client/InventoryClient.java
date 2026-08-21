package com.armalora.order.client;

import com.armalora.order.dto.InventoryReleaseRequest;
import com.armalora.order.dto.InventoryResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/product/{productId}")
    List<InventoryResponse> getInventoryByProductId(
            @PathVariable("productId") Long productId
    );

    @GetMapping(
            "/api/inventory/product/{productId}/variant/{variantId}"
    )
    InventoryResponse getInventoryByProductAndVariant(
            @PathVariable("productId") Long productId,
            @PathVariable("variantId") Long variantId
    );
    @PostMapping("/api/inventory/release")
    void releaseInventory(
            @RequestBody InventoryReleaseRequest request
    );
}