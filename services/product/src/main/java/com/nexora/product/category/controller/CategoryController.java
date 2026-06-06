package com.nexora.product.category.controller;

import com.nexora.product.category.service.CategoryService;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.category.UpdateCategoryRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.CATEGORY)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create category",description = "Used to create the category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update category",description = "Used to update the category")
    public ResponseEntity<SuccessResponse> updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return new ResponseEntity<>(categoryService.updateCategory(updateCategoryRequest), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Delete category",description = "Used to delete the category")
    public ResponseEntity<SuccessResponse> deleteCategoryByUid(@RequestParam("categoryUid") String categoryUid) {
        return new ResponseEntity<>(categoryService.deleteCategoryByUid(categoryUid), HttpStatus.OK);
    }


}
