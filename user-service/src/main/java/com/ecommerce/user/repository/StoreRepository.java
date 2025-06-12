package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s.id FROM Store s where s.approved = true")
    List<Long> findAllApprovedStoresIds();

}
