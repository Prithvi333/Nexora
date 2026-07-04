package com.nexora.product.product.service;

import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> fetchProduct(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse updateProduct(ProductUpdateRequest productUpdateRequest);

    SuccessResponse deleteProduct(String productUid);

}
