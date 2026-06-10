package com.nexora.product.product.service;

import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.product.AlreadyAssociatedProduct;
import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.product.model.Product;
import com.nexora.product.product.repository.ProductRepository;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.GlobalUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Request received to create product: {} (Brand: {})", productRequest.name(), productRequest.brand());

        Product.ProductBuilder productBuilder = GlobalUtility.convertFromProductRequestToProduct(productRequest);
        Category category = categoryRepository.findByUid(productRequest.categoryUid())
                .orElseThrow(() -> {
                    log.warn("Failed to find category with UID: {}", productRequest.categoryUid());
                    return new CategoryNotFound(productRequest.categoryUid());
                });

        if (productRepository.existsByNameAndBrandAndCategory_Uid(productRequest.name(), productRequest.brand(), category.getUid())) {
            log.warn("Product already exists for name: {}, brand: {}, category: {}", productRequest.name(), productRequest.brand(), category.getName());
            throw new AlreadyAssociatedProduct(productRequest.name(), productRequest.brand(), category.getName());
        }

        productBuilder.category(category);
        Product product = productRepository.save(productBuilder.build());
        category.getProducts().add(product);

        log.debug("Caching newly created product with UID: {}", product.getUid());
        redisCacheService.put(Product.class.getSimpleName(), product.getUid(), product);

        log.info("Product created successfully with UID: {}", product.getUid());
        return GlobalUtility.convertFromProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> fetchProduct(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching product(s). UID: {}, Page: {}, Size: {}", productUid, pageNo, pageSize);

        if (productUid != null) {
            Product product = isProductExist(productUid);
            return List.of(GlobalUtility.convertFromProductToProductResponse(product));
        }

        sortBy = sortBy != null ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Product> productPage = productRepository.findAll(pageable);

        if (productPage.isEmpty()) {
            log.warn("No products found for the given pagination criteria");
            throw new EmptyProductList();
        }

        log.debug("Retrieved {} products", productPage.getNumberOfElements());
        return productPage.getContent().stream().map(GlobalUtility::convertFromProductToProductResponse).toList();
    }

    @Override
    public SuccessResponse updateProduct(ProductUpdateRequest productUpdateRequest) {
        log.info("Processing update request for product UID: {}", productUpdateRequest.uid());
        Product product = isProductExist(productUpdateRequest.uid());

        if (productUpdateRequest.categoryUid() != null && !productUpdateRequest.categoryUid().isBlank()) {
            Category category = categoryRepository.findByUid(productUpdateRequest.categoryUid())
                    .orElseThrow(() -> {
                        log.warn("Category update failed: Category not found for UID: {}", productUpdateRequest.categoryUid());
                        return new CategoryNotFound(productUpdateRequest.categoryUid());
                    });
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

        Product updatedProduct = productRepository.save(product);
        log.debug("Updating Redis cache for product UID: {}", updatedProduct.getUid());
        redisCacheService.put(Product.class.getSimpleName(), updatedProduct.getUid(), updatedProduct);

        log.info("Product with UID: {} updated successfully", updatedProduct.getUid());
        return new SuccessResponse("Product updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteProduct(String productUid) {
        log.info("Request received to delete product with UID: {}", productUid);
        Product product = isProductExist(productUid);

        productRepository.delete(product);

        log.debug("Removing product from Redis cache: {}", productUid);
        redisCacheService.delete(Product.class.getSimpleName(), productUid);

        log.info("Product with UID: {} deleted successfully", productUid);
        return new SuccessResponse("Product deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private Product isProductExist(String productUid) {
        log.trace("Validating product existence for UID: {}", productUid);
        return productRepository.findByUid(productUid)
                .orElseThrow(() -> {
                    log.warn("Product not found with UID: {}", productUid);
                    return new ProductNotFound(productUid);
                });
    }
}