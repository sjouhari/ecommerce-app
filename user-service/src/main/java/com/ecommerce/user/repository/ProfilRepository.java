package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profil, Long> {
    boolean existsByName(String name);
}
