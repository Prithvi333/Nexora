package com.nexora.product.product.service;

import com.nexora.product.request.product.OrderItemRequest;
import com.nexora.product.request.variant.VariantPriceResponse;
import com.nexora.product.response.product.ProductResponse;

import java.util.List;
import java.util.Map;

public interface UserProductService {

    List<ProductResponse> getProducts(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    List<VariantPriceResponse> getProductsByOrderItem(List<OrderItemRequest> orderItemRequests);
}
