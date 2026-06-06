package com.nexora.product.product.repository;

import com.nexora.product.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByUid(String productUid);

    boolean existsByNameAndBrandAndCategory_Uid(String name, String brand, String uid);
}
