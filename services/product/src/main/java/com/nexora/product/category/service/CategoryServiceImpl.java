package com.nexora.product.category.service;
import com.nexora.product.category.model.Category;
import com.nexora.product.category.repository.CategoryRepository;
import com.nexora.product.exception.category.CategoryNotFound;
import com.nexora.product.exception.category.EmptyCategoryList;
import com.nexora.product.exception.category.SameCategoryException;
import com.nexora.product.product.model.Product;
import com.nexora.product.redis.RedisCacheService;
import com.nexora.product.request.category.CreateCategoryRequest;
import com.nexora.product.request.category.UpdateCategoryRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.utility.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest categoryRequest) {
        logger.info("Entering createCategory with category name: {}", categoryRequest.name());
        Category.CategoryBuilder categoryBuilder = GlobalUtility.convertFromCategoryRequestToCategory(categoryRequest);
        if (categoryRequest.parentCategoryUid() != null && !categoryRequest.parentCategoryUid().isBlank()) {
            logger.info("Fetching parent category with uid: {}", categoryRequest.parentCategoryUid());
            Optional<Category> category = categoryRepository.findByUid(categoryRequest.parentCategoryUid());
            category.ifPresent(categoryBuilder::parentCategory);
        }
        Category category = categoryRepository.save(categoryBuilder.build());
        logger.info("Category created successfully with uid: {}", category.getUid());
        redisCacheService.put(Category.class.getSimpleName(), category.getUid(), category);
        logger.info("Category cached successfully with uid: {}", category.getUid());
        return GlobalUtility.convertFromCategoryToCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> fetchCategory(String categoryUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchCategory with categoryUid: {}", categoryUid);
        Category category = categoryRepository.findByUid(categoryUid).orElseThrow(() -> new CategoryNotFound(categoryUid));

        if (category != null) {
            logger.info("Category found with uid: {}", categoryUid);
            return List.of(GlobalUtility.convertFromCategoryToCategoryResponse(category));
        }

        sortBy = sortBy == null ? "name" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        logger.info("Fetching category list with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        if (categoryPage.isEmpty()) {
            logger.warn("Category list is empty");
            throw new EmptyCategoryList();
        }
        logger.info("Fetched {} categories successfully", categoryPage.getContent().size());
        return categoryPage.getContent().stream().map(GlobalUtility::convertFromCategoryToCategoryResponse).toList();
    }

    @Override
    public SuccessResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        logger.info("Entering updateCategory with categoryUid: {}", updateCategoryRequest.categoryUid());
        Optional<Category> optionalCategory = categoryRepository.findByUid(updateCategoryRequest.categoryUid());

        Category category = optionalCategory.orElseThrow(() -> new CategoryNotFound(updateCategoryRequest.categoryUid()));

        if (updateCategoryRequest.name() != null && !updateCategoryRequest.name().isBlank() && !category.getName().equals(updateCategoryRequest.name())) {
            logger.info("Updating category name for uid: {}", updateCategoryRequest.categoryUid());
            category.setName(updateCategoryRequest.name());
        }
        if (updateCategoryRequest.parentCategoryUid() != null && !updateCategoryRequest.parentCategoryUid().isBlank()) {
            logger.info("Updating parent category for uid: {} with parent uid: {}", updateCategoryRequest.categoryUid(), updateCategoryRequest.parentCategoryUid());
            if (updateCategoryRequest.categoryUid().equals(updateCategoryRequest.parentCategoryUid())) {
                logger.warn("Category uid and parent category uid are the same: {}", updateCategoryRequest.categoryUid());
                throw new SameCategoryException();
            }
            Category parentCategory = categoryRepository.findByUid(updateCategoryRequest.parentCategoryUid()).orElseThrow(() -> new CategoryNotFound(updateCategoryRequest.parentCategoryUid()));
            category.setParentCategory(parentCategory);
        }
        redisCacheService.put(Category.class.getSimpleName(), category.getUid(), categoryRepository.save(category));
        logger.info("Category updated and cached successfully with uid: {}", category.getUid());

        return new SuccessResponse("Category has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteCategoryByUid(String categoryUid) {
        logger.info("Entering deleteCategoryByUid with categoryUid: {}", categoryUid);
        Category category = categoryRepository.findByUid(categoryUid).orElseThrow(() -> new CategoryNotFound(categoryUid));
        categoryRepository.delete(category);
        logger.info("Category deleted successfully with uid: {}", categoryUid);
        redisCacheService.delete(Category.class.getSimpleName(), categoryUid);
        logger.info("Category cache deleted successfully with uid: {}", categoryUid);
        return new SuccessResponse("Category has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

}