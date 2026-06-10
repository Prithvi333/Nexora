package com.nexora.product.product.service;

import com.nexora.product.exception.inventory.OrderQuantityGreaterThanActualQuantity;
import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.exception.product.ProductOrVariantNotFound;
import com.nexora.product.inventory.model.Inventory;
import com.nexora.product.product.model.Product;
import com.nexora.product.product.repository.ProductRepository;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.request.product.OrderItemRequest;
import com.nexora.product.request.variant.VariantPriceResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.GlobalUtility;
import com.nexora.product.variant.model.ProductVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProductServiceImpl implements UserProductService {

    private static final Logger log = LoggerFactory.getLogger(UserProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public List<ProductResponse> getProducts(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching user products. UID: {}, Page: {}, Size: {}", productUid, pageNo, pageSize);

        if (productUid != null && !productUid.isBlank()) {
            if (redisCacheService.isHasKeyExists(Product.class.getSimpleName(), productUid)) {
                log.info("Product cache hit for UID: {}", productUid);
                return List.of(redisCacheService.get(Product.class.getSimpleName(), productUid, ProductResponse.class));
            }

            log.info("Product cache miss for UID: {}. Fetching from Database...", productUid);
            Product product = productRepository.findByUid(productUid).orElseThrow(() -> {
                log.warn("Product fetch failed. Not found with UID: {}", productUid);
                return new ProductNotFound(productUid);
            });
            return List.of(GlobalUtility.convertFromProductToProductResponse(product));
        }

        sortBy = sortBy == null ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Product> page = productRepository.findAll(pageable);
        if (page.isEmpty()) {
            log.warn("No products found matching the pagination criteria");
            throw new EmptyProductList();
        }

        log.debug("Successfully fetched {} products", page.getNumberOfElements());
        return page.getContent().stream().map(GlobalUtility::convertFromProductToProductResponse).toList();
    }

    @Override
    public List<VariantPriceResponse> getProductsByOrderItem(List<OrderItemRequest> orderItemRequests) {
        log.info("Validating and pricing {} order items", orderItemRequests != null ? orderItemRequests.size() : 0);
        List<VariantPriceResponse> variantPriceResponses = new ArrayList<>();

        orderItemRequests.forEach(orderItemRequest -> {
            log.debug("Processing validation for Product UID: {}, Variant UID: {}", orderItemRequest.productUid(), orderItemRequest.variantUid());

            Product product = productRepository.findByUidAndProductVariants_Uid(orderItemRequest.productUid(), orderItemRequest.variantUid())
                    .orElseThrow(() -> {
                        log.error("Order processing aborted. Product/Variant combination invalid for Prod UID: {}, Var UID: {}",
                                orderItemRequest.productUid(), orderItemRequest.variantUid());
                        return new ProductOrVariantNotFound();
                    });

            Optional<ProductVariant> variant = product.getProductVariants().stream()
                    .filter(productVariant -> productVariant.getUid().equals(orderItemRequest.variantUid())).findFirst();

            Inventory inventory = variant.get().getInventory();
            int availableStock = inventory.getQuantity() - inventory.getReservedQuantity();

            if (orderItemRequest.quantity() > availableStock) {
                log.warn("Insufficient stock for Variant UID: {}. Requested: {}, Available: {}",
                        orderItemRequest.variantUid(), orderItemRequest.quantity(), availableStock);
                throw new OrderQuantityGreaterThanActualQuantity(orderItemRequest.quantity(), availableStock);
            }

            variantPriceResponses.add(VariantPriceResponse.builder()
                    .variantUid(variant.get().getUid())
                    .price(variant.get().getPrice())
                    .build());
        });

        log.info("Successfully validated and priced all items in the request batch");
        return variantPriceResponses;
    }
}