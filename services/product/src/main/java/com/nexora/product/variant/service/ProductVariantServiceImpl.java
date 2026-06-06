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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantRequest productVariantRequest) {
        ProductVariant.ProductVariantBuilder productVariantBuilder = GlobalUtility.convertFromProductVariantRequestToProductVariant(productVariantRequest);
        Product product = isProductExist(productVariantRequest.productUid());
        if (productVariantRepository.existsByColorAndSizeAndPrice(productVariantRequest.color(), productVariantRequest.size(), productVariantRequest.price())) {
            throw new ProductVariantAlreadyAssociated(product.getName());
        }

        if (productVariantRequest.inventory().reserved() > productVariantRequest.inventory().quantity()) {
            throw new ReservedQuantityGreaterThanActualQuantity();
        }

        productVariantBuilder.product(product);
        Inventory inventory = GlobalUtility.convertFromInventoryRequestToInventory(productVariantRequest.inventory());
        productVariantBuilder.inventory(inventory);
        ProductVariant actualProduct = productVariantBuilder.build();
        inventory.setProductVariant(actualProduct);
        return GlobalUtility.convertFromProductVariantToProductVariantResponse(productVariantRepository.save(actualProduct));

    }

    @Override
    public SuccessResponse updateProductVariant(ProductVariantUpdateRequest productVariantUpdateRequest) {
        ProductVariant productVariant = productVariantRepository.findByUid(productVariantUpdateRequest.uid()).orElseThrow(() -> new ProductVariantNotFound(productVariantUpdateRequest.uid()));

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

        return new SuccessResponse("Product variant has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());

    }

    @Override
    public SuccessResponse deleteProductVariant(String productVariantUid) {
        ProductVariant productVariant = productVariantRepository.findByUid(productVariantUid).orElseThrow(() -> new ProductNotFound(productVariantUid));
        productVariantRepository.delete(productVariant);
        return new SuccessResponse("Product variant has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Product isProductExist(String productUid) {
        return productRepository.findByUid(productUid).orElseThrow(() -> new ProductNotFound(productUid));

    }
}
