package com.nexora.product.category.service;

import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest categoryRequest);

    List<CategoryResponse> fetchCategory(String categoryUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);

    SuccessResponse deleteCategoryByUid(String categoryUid);

}
