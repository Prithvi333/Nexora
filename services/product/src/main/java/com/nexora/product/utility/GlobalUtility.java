package com.nexora.product.utility;

import com.nexora.product.category.model.Category;
import com.nexora.product.image.model.ProductImage;
import com.nexora.product.inventory.model.Inventory;
import com.nexora.product.product.model.Product;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.image.ProductImageRequest;
import com.nexora.product.request.inventory.InventoryRequest;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.variant.ProductVariantRequest;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.response.image.ProductImageResponse;
import com.nexora.product.response.inventory.InventoryResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.variant.model.ProductVariant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class GlobalUtility {
    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public static Category.CategoryBuilder convertFromCategoryRequestToCategory(CreateCategoryRequest categoryRequest) {
        return Category.builder().uid(UUID.randomUUID().toString()).name(categoryRequest.name()).products(new ArrayList<>());
    }

    public static CategoryResponse convertFromCategoryToCategoryResponse(Category category) {

        return CategoryResponse.builder().name(category.getName()).uid(category.getUid()).parentCategoryUid(category.getParentCategory() != null ? category.getParentCategory().getUid() : null)
                .parentCategoryName(category.getParentCategory().getName() != null ? category.getParentCategory().getName() : null)
                .productUids(category.getProducts().stream().map(Product::getUid).toList()).build();
    }

    public static Product.ProductBuilder convertFromProductRequestToProduct(ProductRequest productRequest) {
        return Product.builder().uid(UUID.randomUUID().toString()).active(true).name(productRequest.name()).brand(productRequest.brand()).productVariants(new ArrayList<>())
                .description(productRequest.description()).createdAt(LocalDateTime.now());
    }


    public static ProductResponse convertFromProductToProductResponse(Product product) {
        return ProductResponse.builder().uid(product.getUid()).active(product.getActive()).brand(product.getBrand())
                .productVariants(product.getProductVariants().stream()
                        .map(GlobalUtility::convertFromProductVariantToProductVariantResponse).toList())
                .description(product.getDescription()).category(convertFromCategoryToCategoryResponse(product.getCategory()))
                .createdAt(product.getCreatedAt())
                .name(product.getName())
                .build();
    }

    public static ProductVariant.ProductVariantBuilder convertFromProductVariantRequestToProductVariant(ProductVariantRequest productVariantRequest) {
        return ProductVariant.builder().productImages(new ArrayList<>()).uid(UUID.randomUUID().toString()).color(productVariantRequest.color())
                .price(productVariantRequest.price())
                .size(productVariantRequest.size());
    }

    public static ProductVariantResponse convertFromProductVariantToProductVariantResponse(ProductVariant productVariant) {
        return ProductVariantResponse.builder().uid(productVariant.getUid()).color(productVariant.getColor()).price(productVariant.getPrice())
                .size(productVariant.getSize()).inventory(convertFromInventoryToInventoryResponse(productVariant.getInventory()))
                .productImages(productVariant.getProductImages().stream()
                        .map(GlobalUtility::convertFromProductImageToProductImageResponse).toList()).build();
    }

    public static InventoryResponse convertFromInventoryToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .uid(inventory.getUid())
                .quantity(inventory.getQuantity())
                .reserved(inventory.getReservedQuantity())
                .build();
    }

    public static Inventory convertFromInventoryRequestToInventory(InventoryRequest inventoryRequest) {
        return Inventory.builder().uid(UUID.randomUUID().toString())
                .quantity(inventoryRequest.quantity())
                .reservedQuantity(inventoryRequest.reserved()).build();
    }


    public static ProductImageResponse convertFromProductImageToProductImageResponse(ProductImage productImage) {
        return ProductImageResponse.builder().uid(productImage.getUid())
                .url(productImage.getUrl())
                .primary(productImage.getPrimaryImage())
                .build();
    }

    public static ProductImage.ProductImageBuilder convertFromProductImageRequestToProductImage(ProductImageRequest productImageRequest) {
        return ProductImage.builder().uid(UUID.randomUUID().toString())
                .primaryImage(true);


    }
}
