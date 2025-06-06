package com.ecommerce.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.category.entity.Size;

public interface SizeRepository extends JpaRepository<Size, Long> {

    boolean existsByLibelleAndCategoryId(String libelle, Long categoryId);

}
