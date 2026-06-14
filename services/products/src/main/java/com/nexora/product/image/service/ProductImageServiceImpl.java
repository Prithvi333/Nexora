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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private static final Logger log = LoggerFactory.getLogger(ProductImageServiceImpl.class);

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public ProductImageResponse saveImage(ProductImageRequest productImageRequest) {
        log.info("Request received to save image for Product Variant UID: {}", productImageRequest.productVariantUid());

        ProductVariant productVariant = productVariantRepository.findByUid(productImageRequest.productVariantUid())
                .orElseThrow(() -> {
                    log.error("Image upload aborted: Product Variant not found with UID: {}", productImageRequest.productVariantUid());
                    return new ProductVariantNotFound(productImageRequest.productVariantUid());
                });

        ProductImage.ProductImageBuilder productImageBuilder = GlobalUtility.convertFromProductImageRequestToProductImage(productImageRequest);
        productImageBuilder.productVariant(productVariant);

        String generatedKey = UUID.randomUUID().toString();
        log.debug("Generated unique S3 storage key: {}", generatedKey);

        String s3Url = s3Service.uploadImage(productImageRequest.file(), generatedKey);
        productImageBuilder.url(s3Url);

        ProductImage savedProductImage = productImageRepository.save(productImageBuilder.build());
        log.info("Product image successfully saved in database with Image UID: {}", savedProductImage.getUid());

        return GlobalUtility.convertFromProductImageToProductImageResponse(savedProductImage);
    }

    @Override
    public SuccessResponse deleteImage(String productImageUid) {
        log.info("Request received to delete product image with UID: {}", productImageUid);

        ProductImage productImage = productImageRepository.findByUid(productImageUid)
                .orElseThrow(() -> {
                    log.error("Deletion aborted: Product Image record not found for UID: {}", productImageUid);
                    return new ProductImageNotFound(productImageUid);
                });

        String key = productImage.getUrl().substring(productImage.getUrl().lastIndexOf("/") + 1);
        log.debug("Extracted S3 key '{}' from asset URL: {}", key, productImage.getUrl());

        s3Service.deleteImage(key);
        productImageRepository.delete(productImage);
        log.info("Product Image with UID: {} successfully purged from S3 and metadata repository", productImageUid);

        return new SuccessResponse("Product Image has been deleted with uid " + productImageUid + "", HttpStatus.OK.value(), LocalDateTime.now());
    }
}