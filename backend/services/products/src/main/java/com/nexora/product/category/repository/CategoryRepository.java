package com.nexora.product.category.repository;

import com.nexora.product.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByUid(String categoryUid);

}
