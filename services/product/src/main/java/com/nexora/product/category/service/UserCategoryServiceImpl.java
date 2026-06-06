package com.nexora.product.category.service;

import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> fetchCategory(String categoryUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {

        if (categoryUid != null && !categoryUid.isBlank()) {
            Category category = categoryRepository.findByUid(categoryUid).orElseThrow(() -> new CategoryNotFound(categoryUid));
            return List.of(GlobalUtility.convertFromCategoryToCategoryResponse(category));
        }
        sortBy = sortBy == null || sortBy.isBlank() ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<Category> page = categoryRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyCategoryList();
        }
        return page.getContent().stream().map(GlobalUtility::convertFromCategoryToCategoryResponse).toList();
    }
}
