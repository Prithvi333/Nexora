package com.nexora.product.variant.service;

import com.nexora.product.exception.product.EmptyProductList;
import com.nexora.product.exception.variant.EmptyProductVariantList;
import com.nexora.product.exception.variant.ProductVariantNotFound;
import com.nexora.product.product.model.Product;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.GlobalUtility;
import com.nexora.product.variant.model.ProductVariant;
import com.nexora.product.variant.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductVariantServiceImpl implements UserProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public List<ProductVariantResponse> getProductVariant(String productVariantUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {

        if (productVariantUid != null && !productVariantUid.isBlank()) {
            ProductVariant productVariant = productVariantRepository.findByUid(productVariantUid).orElseThrow(() -> new ProductVariantNotFound(productVariantUid));
            return List.of(GlobalUtility.convertFromProductVariantToProductVariantResponse(productVariant));
        }

        sortBy = sortBy == null ? "price" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<ProductVariant> page = productVariantRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyProductVariantList();
        }
        return page.getContent().stream().map(GlobalUtility::convertFromProductVariantToProductVariantResponse).toList();

    }
}
