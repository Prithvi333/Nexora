package com.nexora.product.product.service;

import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    SuccessResponse updateProduct(ProductUpdateRequest productUpdateRequest);

    SuccessResponse deleteProduct(String productUid);

}
