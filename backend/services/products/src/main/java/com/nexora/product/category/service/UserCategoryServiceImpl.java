package com.nexora.product.category.service;
import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.GlobalUtility;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserCategoryServiceImpl implements UserCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(UserCategoryServiceImpl.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<CategoryResponse> fetchCategory(String categoryUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchCategory with categoryUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", categoryUid, pageNo, pageSize, sortBy, direction);

        if (categoryUid != null && !categoryUid.isBlank()) {
            logger.info("Fetching category by uid: {}", categoryUid);
            if (redisCacheService.isHasKeyExists(Category.class.getSimpleName(), categoryUid)) {
                logger.info("Category found in cache with uid: {}", categoryUid);
                return List.of(redisCacheService.get(Category.class.getSimpleName(), categoryUid, CategoryResponse.class));
            }
            logger.info("Category not found in cache, fetching from database with uid: {}", categoryUid);
            Category category = categoryRepository.findByUid(categoryUid).orElseThrow(() -> new CategoryNotFound(categoryUid));
            logger.info("Category fetched successfully from database with uid: {}", categoryUid);
            return List.of(GlobalUtility.convertFromCategoryToCategoryResponse(category));
        }
        sortBy = sortBy == null || sortBy.isBlank() ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching category list with pagination. pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        Page<Category> page = categoryRepository.findAll(pageable);
        if (page.isEmpty()) {
            logger.warn("No categories found");
            throw new EmptyCategoryList();
        }
        logger.info("Fetched {} categories successfully", page.getContent().size());
        return page.getContent().stream().map(GlobalUtility::convertFromCategoryToCategoryResponse).toList();
    }

}