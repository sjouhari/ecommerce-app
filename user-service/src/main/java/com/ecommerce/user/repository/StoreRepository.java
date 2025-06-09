package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
