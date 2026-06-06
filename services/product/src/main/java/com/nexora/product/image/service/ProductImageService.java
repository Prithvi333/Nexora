package com.nexora.product.image.service;

import com.nexora.product.request.image.ProductImageRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.image.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    ProductImageResponse saveImage(ProductImageRequest productImageRequest);

    SuccessResponse deleteImage(String productImageUid);

}
