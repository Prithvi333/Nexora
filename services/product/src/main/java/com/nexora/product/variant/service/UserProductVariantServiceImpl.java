package com.nexora.product.variant.service;

import com.nexora.product.exception.inventory.OrderQuantityGreaterThanActualQuantity;
import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.variant.EmptyProductVariantList;
import com.nexora.product.exception.variant.ProductVariantNotFound;
import com.nexora.product.product.model.Product;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.GlobalUtility;
import com.nexora.product.variant.model.ProductVariant;
import com.nexora.product.variant.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductVariantServiceImpl implements UserProductVariantService {

    private static final Logger log = LoggerFactory.getLogger(UserProductVariantServiceImpl.class);

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public List<ProductVariantResponse> getProductVariant(String productVariantUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching product variant(s). UID: {}, Page: {}, Size: {}, SortBy: {}", productVariantUid, pageNo, pageSize, sortBy);

        if (productVariantUid != null && !productVariantUid.isBlank()) {
            ProductVariant productVariant = productVariantRepository.findByUid(productVariantUid)
                    .orElseThrow(() -> {
                        log.warn("Product variant fetch failed: Not found with UID: {}", productVariantUid);
                        return new ProductVariantNotFound(productVariantUid);
                    });
            return List.of(GlobalUtility.convertFromProductVariantToProductVariantResponse(productVariant));
        }

        sortBy = sortBy == null ? "price" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<ProductVariant> page = productVariantRepository.findAll(pageable);

        if (page.isEmpty()) {
            log.warn("No product variants found for pagination criteria");
            throw new EmptyProductVariantList();
        }

        log.debug("Successfully retrieved {} product variants", page.getNumberOfElements());
        return page.getContent().stream().map(GlobalUtility::convertFromProductVariantToProductVariantResponse).toList();
    }

    @Override
    public void validateQuantity(String variantUid, Integer quantity) {
        log.trace("Validating stock quantity for variant UID: {}, Requested: {}", variantUid, quantity);
        ProductVariant productVariant = productVariantRepository.findByUid(variantUid)
                .orElseThrow(() -> {
                    log.error("Quantity validation failed: Product variant not found with UID: {}", variantUid);
                    return new ProductVariantNotFound(variantUid);
                });

        int availableStock = productVariant.getInventory().getQuantity() - productVariant.getInventory().getReservedQuantity();

        if (quantity > availableStock) {
            log.warn("Order quantity check failed. Requested: {}, Available: {}", quantity, availableStock);
            throw new OrderQuantityGreaterThanActualQuantity(quantity, availableStock);
        }
        log.trace("Quantity validation successful for variant UID: {}", variantUid);
    }
}