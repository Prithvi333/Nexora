package com.nexora.product.category.service;

import com.nexora.product.response.category.CategoryResponse;

import java.util.List;

public interface UserCategoryService {

    List<CategoryResponse> fetchCategory(String categoryUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

}
