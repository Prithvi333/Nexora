package com.nexora.product.product.service;

import com.nexora.product.response.product.ProductResponse;

import java.util.List;

public interface UserProductService {

    List<ProductResponse> getProducts(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction);
}
