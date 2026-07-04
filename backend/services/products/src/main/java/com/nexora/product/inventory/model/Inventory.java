package com.nexora.product.inventory.model;

import com.nexora.product.image.model.ProductImage;
import com.nexora.product.variant.model.ProductVariant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Inventory uid is required")
    @Size(max = 50, message = "Inventory uid cannot exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String uid;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private int quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    @Column(nullable = false)
    private int reservedQuantity;

    @NotNull(message = "Product variant is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false, unique = true)
    private ProductVariant productVariant;
}