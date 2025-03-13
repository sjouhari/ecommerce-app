package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Long> {
    boolean existsByName(String name);
    Optional<Profil> findByName(String name);
}
