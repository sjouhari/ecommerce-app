package com.ecommerce.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.category.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

}
