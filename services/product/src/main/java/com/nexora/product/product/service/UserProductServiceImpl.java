package com.nexora.product.product.service;

import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.product.ProductNotFound;
import com.nexora.product.product.model.Product;
import com.nexora.product.product.repository.ProductRepository;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductServiceImpl implements UserProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public List<ProductResponse> getProducts(String productUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {

        if (productUid != null && !productUid.isBlank()) {

            if(redisCacheService.isHasKeyExists(Product.class.getSimpleName(),productUid)){
                return List.of(redisCacheService.get(Product.class.getSimpleName(),productUid,ProductResponse.class));
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
}
