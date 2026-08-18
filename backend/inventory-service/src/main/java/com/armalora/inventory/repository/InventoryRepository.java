package com.armalora.inventory.repository;

import com.armalora.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndVariantId(
            Long productId,
            Long variantId
    );

    List<Inventory> findByProductId(
            Long productId
    );
}