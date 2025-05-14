package com.ecommerce.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.category.entity.Size;
import org.springframework.data.jpa.repository.Query;

public interface SizeRepository extends JpaRepository<Size, Long> {

    @Query("SELECT s FROM Size s LEFT JOIN SubCategory sc ON sc.category.id = s.category.id WHERE s.libelle = ?1 AND s.category.id = ?2")
    boolean existsByLibelleAndSubCategoryId(String libelle, Long subCategoryId);

}
