package com.nexora.product.image.service;

import com.nexora.product.S3.S3Service;
import com.nexora.product.exception.image.ProductImageNotFound;
import com.nexora.product.exception.variant.ProductVariantNotFound;
import com.nexora.product.image.repository.ProductImageRepository;
import com.nexora.product.image.model.ProductImage;
import com.nexora.product.request.image.ProductImageRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.image.ProductImageResponse;
import com.nexora.product.utility.GlobalUtility;
import com.nexora.product.variant.model.ProductVariant;
import com.nexora.product.variant.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public ProductImageResponse saveImage(ProductImageRequest productImageRequest) {

        ProductVariant productVariant = productVariantRepository.findByUid(productImageRequest.productVariantUid()).orElseThrow(() -> new ProductVariantNotFound(productImageRequest.productVariantUid()));

        ProductImage.ProductImageBuilder productImageBuilder = GlobalUtility.convertFromProductImageRequestToProductImage(productImageRequest);
        productImageBuilder.productVariant(productVariant);
        productImageBuilder.url(s3Service.uploadImage(productImageRequest.file(), UUID.randomUUID().toString()));
        return GlobalUtility.convertFromProductImageToProductImageResponse(productImageRepository.save(productImageBuilder.build()));
    }

    @Override
    public SuccessResponse deleteImage(String productImageUid) {

        ProductImage productImage = productImageRepository.findByUid(productImageUid).orElseThrow(() -> new ProductImageNotFound(productImageUid));

        String key = productImage.getUrl().substring(productImage.getUrl().lastIndexOf("/") + 1);

        s3Service.deleteImage(key);
        productImageRepository.delete(productImage);

        return new SuccessResponse("Product Image has been deleted with uid " + productImageUid + "", HttpStatus.OK.value(), LocalDateTime.now());
    }
}
