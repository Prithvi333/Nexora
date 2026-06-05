package com.nexora.product.category.service;

import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.category.UpdateCategoryRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest categoryRequest) {
        Category.CategoryBuilder categoryBuilder = GlobalUtility.convertFromCategoryRequestToCategory(categoryRequest);
        if (categoryRequest.parentCategoryUid() != null && !categoryRequest.parentCategoryUid().isBlank()) {
            Optional<Category> category = categoryRepository.findByUid(categoryRequest.parentCategoryUid());
            category.ifPresent(categoryBuilder::parentCategory);
        }
        return GlobalUtility.convertFromCategoryToCategoryResponse(categoryRepository.save(categoryBuilder.build()));
    }

    @Override
    public SuccessResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        Optional<Category> optionalCategory = categoryRepository.findByUid(updateCategoryRequest.categoryUid());

        Category category = optionalCategory.orElseThrow(() -> new CategoryNotFound(updateCategoryRequest.categoryUid()));

        if (category.getName() != null && !category.getName().isBlank() && !category.getName().equals(updateCategoryRequest.name())) {
            category.setName(updateCategoryRequest.name());
        }
        if (updateCategoryRequest.parentCategoryUid() != null && !updateCategoryRequest.parentCategoryUid().isBlank()) {
            Category parentCategory = optionalCategory.orElseThrow(() -> new CategoryNotFound(updateCategoryRequest.categoryUid()));
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);

        return new SuccessResponse("Category has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteCategoryByUid(String categoryUid) {
        Category category = categoryRepository.findByUid(categoryUid).orElseThrow(() -> new CategoryNotFound(categoryUid));
        categoryRepository.delete(category);
        return new SuccessResponse("Category has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }
}
