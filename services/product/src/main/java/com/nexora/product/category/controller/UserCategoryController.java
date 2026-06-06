package com.nexora.product.category.controller;

import com.nexora.product.category.service.UserCategoryService;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.CATEGORY)
public class UserCategoryController {

    @Autowired
    private UserCategoryService userCategoryService;

    @GetMapping
    @Operation(summary = "Fetch category", description = "User to get the category")
    public ResponseEntity<List<CategoryResponse>> getCategory(@RequestParam(required = false) String categoryUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(userCategoryService.fetchCategory(categoryUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }
}
