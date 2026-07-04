package com.nexora.product.inventory.repository;

import com.nexora.product.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByUid(String uid);

}
