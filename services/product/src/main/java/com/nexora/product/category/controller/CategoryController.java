package com.nexora.product.category.controller;
import com.nexora.product.category.service.CategoryService;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.category.UpdateCategoryRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.CATEGORY)
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create category", description = "Used to create the category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        logger.info("Received request to create category");
        ResponseEntity<CategoryResponse> response = new ResponseEntity<>(categoryService.createCategory(categoryRequest), HttpStatus.CREATED);
        logger.info("Category created successfully");
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetching category", description = "Used to fetch the category")
    public ResponseEntity<List<CategoryResponse>> fetchCategory(@RequestParam(required = false) String categoryUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch category with categoryUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", categoryUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<CategoryResponse>> response = new ResponseEntity<>(categoryService.fetchCategory(categoryUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Category fetched successfully");
        return response;
    }


    @PutMapping
    @Operation(summary = "Update category", description = "Used to update the category")
    public ResponseEntity<SuccessResponse> updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        logger.info("Received request to update category");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(categoryService.updateCategory(updateCategoryRequest), HttpStatus.OK);
        logger.info("Category updated successfully");
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete category", description = "Used to delete the category")
    public ResponseEntity<SuccessResponse> deleteCategoryByUid(@RequestParam("categoryUid") String categoryUid) {
        logger.info("Received request to delete category with categoryUid: {}", categoryUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(categoryService.deleteCategoryByUid(categoryUid), HttpStatus.OK);
        logger.info("Category deleted successfully with categoryUid: {}", categoryUid);
        return response;
    }

}