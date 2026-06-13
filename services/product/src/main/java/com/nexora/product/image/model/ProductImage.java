package com.nexora.product.image.model;

import com.nexora.product.product.model.Product;
import com.nexora.product.variant.model.ProductVariant;
import jakarta.persistence.*;
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
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ProductImage uid is required")
    @Size(max = 50, message = "ProductImage uid cannot exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String uid;

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String url;

    private Boolean primaryImage;

    @NotNull(message = "Product variant is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;
}