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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProductServiceImpl implements UserProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public List<ProductResponse> getProducts(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {

        if (productUid != null && !productUid.isBlank()) {

            if (redisCacheService.isHasKeyExists(Product.class.getSimpleName(), productUid)) {
                return List.of(redisCacheService.get(Product.class.getSimpleName(), productUid, ProductResponse.class));
            }

            Product product = productRepository.findByUid(productUid).orElseThrow(() -> new ProductNotFound(productUid));
            return List.of(GlobalUtility.convertFromProductToProductResponse(product));
        }
        sortBy = sortBy == null ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Product> page = productRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyProductList();
        }
        return page.getContent().stream().map(GlobalUtility::convertFromProductToProductResponse).toList();

    }

    @Override
    public List<VariantPriceResponse> getProductsByOrderItem(List<OrderItemRequest> orderItemRequests) {
        List<VariantPriceResponse> variantPriceResponses = new ArrayList<>();
        orderItemRequests.forEach(orderItemRequest -> {
            Product product = productRepository.findByUidAndProductVariants_Uid(orderItemRequest.productUid(), orderItemRequest.variantUid()).orElseThrow(ProductOrVariantNotFound::new);
            Optional<ProductVariant> variant = product.getProductVariants().stream()
                    .filter(productVariant -> productVariant.getUid().equals(orderItemRequest.variantUid())).findFirst();

            Inventory inventory = variant.get().getInventory();
            if (orderItemRequest.quantity() > (inventory.getQuantity() - inventory.getReservedQuantity())) {
                throw new OrderQuantityGreaterThanActualQuantity(orderItemRequest.quantity(), inventory.getQuantity() - inventory.getReservedQuantity());
            }
            variantPriceResponses.add(VariantPriceResponse.builder().variantUid(variant.get().getUid()).price(variant.get().getPrice()).build());

        });
        return variantPriceResponses;
    }
}
