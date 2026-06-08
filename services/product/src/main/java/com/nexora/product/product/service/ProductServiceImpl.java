package com.nexora.product.product.service;

import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.product.AlreadyAssociatedProduct;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.product.model.Product;
import com.nexora.product.product.repository.ProductRepository;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.GlobalUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product.ProductBuilder productBuilder = GlobalUtility.convertFromProductRequestToProduct(productRequest);
        Category category = categoryRepository.findByUid(productRequest.categoryUid()).orElseThrow(() -> new CategoryNotFound(productRequest.categoryUid()));
        if (productRepository.existsByNameAndBrandAndCategory_Uid(productRequest.name(), productRequest.brand(), category.getUid())) {
            throw new AlreadyAssociatedProduct(productRequest.name(), productRequest.brand(), category.getName());
        }
        productBuilder.category(category);
        Product product = productRepository.save(productBuilder.build());
        category.getProducts().add(product);
        redisCacheService.put(Product.class.getSimpleName(),product.getUid(),product);
        return GlobalUtility.convertFromProductToProductResponse(product);
    }


    @Override
    public SuccessResponse updateProduct(ProductUpdateRequest productUpdateRequest) {

        Product product = isProductExist(productUpdateRequest.uid());

        if (productUpdateRequest.categoryUid() != null && !productUpdateRequest.categoryUid().isBlank()) {
            Category category = categoryRepository.findByUid(productUpdateRequest.categoryUid()).orElseThrow(() -> new CategoryNotFound(productUpdateRequest.categoryUid()));
            product.setCategory(category);
        }

        if (productUpdateRequest.active() != null) {
            product.setActive(productUpdateRequest.active());
        }
        if (productUpdateRequest.name() != null && !productUpdateRequest.name().isEmpty()) {
            product.setName(productUpdateRequest.name());
        }
        if (productUpdateRequest.description() != null && !productUpdateRequest.description().isEmpty()) {
            product.setDescription(productUpdateRequest.description());
        }

        redisCacheService.put(Product.class.getSimpleName(),product.getUid(),productRepository.save(product));

        return new SuccessResponse("Product updated successfully", HttpStatus.OK.value(), LocalDateTime.now());

    }

    @Override
    public SuccessResponse deleteProduct(String productUid) {

        Product product = isProductExist(productUid);
        productRepository.delete(product);
        redisCacheService.delete(Product.class.getSimpleName(),productUid);
        return new SuccessResponse("Product deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Product isProductExist(String productUid) {
        return productRepository.findByUid(productUid).orElseThrow(() -> new ProductNotFound(productUid));

    }
}
