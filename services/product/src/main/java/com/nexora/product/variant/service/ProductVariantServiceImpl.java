package com.nexora.product.variant.service;

import com.nexora.product.exception.inventory.ReservedQuantityGreaterThanActualQuantity;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.exception.variant.ProductVariantAlreadyAssociated;
import com.nexora.product.exception.variant.ProductVariantNotFound;
import com.nexora.product.inventory.model.Inventory;
import com.nexora.product.product.model.Product;
import com.nexora.product.product.repository.ProductRepository;
import com.nexora.product.request.variant.ProductVariantRequest;
import com.nexora.product.request.variant.ProductVariantUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.GlobalUtility;
import com.nexora.product.variant.model.ProductVariant;
import com.nexora.product.variant.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    private static final Logger log = LoggerFactory.getLogger(ProductVariantServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantRequest productVariantRequest) {
        log.info("Request received to create product variant for Product UID: {}", productVariantRequest.productUid());

        ProductVariant.ProductVariantBuilder productVariantBuilder = GlobalUtility.convertFromProductVariantRequestToProductVariant(productVariantRequest);
        Product product = isProductExist(productVariantRequest.productUid());

        if (productVariantRepository.existsByColorAndSizeAndPrice(productVariantRequest.color(), productVariantRequest.size(), productVariantRequest.price())) {
            log.warn("Attempted to create a duplicate variant for product: {}", product.getName());
            throw new ProductVariantAlreadyAssociated(product.getName());
        }

        if (productVariantRequest.inventory().reserved() > productVariantRequest.inventory().quantity()) {
            log.error("Validation failed: Reserved quantity {} is greater than actual quantity {}",
                    productVariantRequest.inventory().reserved(), productVariantRequest.inventory().quantity());
            throw new ReservedQuantityGreaterThanActualQuantity();
        }

        productVariantBuilder.product(product);
        Inventory inventory = GlobalUtility.convertFromInventoryRequestToInventory(productVariantRequest.inventory());
        productVariantBuilder.inventory(inventory);
        ProductVariant actualProduct = productVariantBuilder.build();
        inventory.setProductVariant(actualProduct);

        ProductVariant savedVariant = productVariantRepository.save(actualProduct);
        log.info("Product variant created successfully with ID: {}", savedVariant.getUid());

        return GlobalUtility.convertFromProductVariantToProductVariantResponse(savedVariant);
    }

    @Override
    public SuccessResponse updateProductVariant(ProductVariantUpdateRequest productVariantUpdateRequest) {
        log.info("Updating product variant with UID: {}", productVariantUpdateRequest.uid());
        ProductVariant productVariant = productVariantRepository.findByUid(productVariantUpdateRequest.uid())
                .orElseThrow(() -> {
                    log.warn("Update aborted: Product variant not found with UID: {}", productVariantUpdateRequest.uid());
                    return new ProductVariantNotFound(productVariantUpdateRequest.uid());
                });

        if (productVariantUpdateRequest.price() != null && !productVariantUpdateRequest.price().equals(productVariant.getPrice())) {
            productVariant.setPrice(productVariantUpdateRequest.price());
        }

        if (productVariantUpdateRequest.size() != null && !productVariantUpdateRequest.size().isBlank() && !productVariantUpdateRequest.size().equals(productVariant.getSize())) {
            productVariant.setSize(productVariantUpdateRequest.size());
        }

        if (productVariantUpdateRequest.color() != null && !productVariantUpdateRequest.color().isBlank() && !productVariantUpdateRequest.color().equals(productVariant.getColor())) {
            productVariant.setColor(productVariantUpdateRequest.color());
        }

        if (productVariantUpdateRequest.productUid() != null && !productVariantUpdateRequest.productUid().isBlank()) {
            Product product = isProductExist(productVariantUpdateRequest.productUid());
            productVariant.setProduct(product);
        }

        productVariantRepository.save(productVariant);
        log.info("Product variant with UID: {} updated successfully", productVariantUpdateRequest.uid());

        return new SuccessResponse("Product variant has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteProductVariant(String productVariantUid) {
        log.info("Deleting product variant with UID: {}", productVariantUid);
        ProductVariant productVariant = productVariantRepository.findByUid(productVariantUid)
                .orElseThrow(() -> {
                    log.error("Deletion failed: Product variant not found with UID: {}", productVariantUid);
                    return new ProductNotFound(productVariantUid);
                });

        productVariantRepository.delete(productVariant);
        log.info("Product variant with UID: {} deleted successfully", productVariantUid);

        return new SuccessResponse("Product variant has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Product isProductExist(String productUid) {
        log.trace("Validating product existence for UID: {}", productUid);
        return productRepository.findByUid(productUid)
                .orElseThrow(() -> {
                    log.warn("Product not found with UID: {}", productUid);
                    return new ProductNotFound(productUid);
                });
    }
}