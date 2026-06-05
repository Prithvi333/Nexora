package com.nexora.product.variant.repository;

import com.nexora.product.variant.model.ProductVariant;
import jakarta.ws.rs.core.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findByUid(String variantUid);
}
