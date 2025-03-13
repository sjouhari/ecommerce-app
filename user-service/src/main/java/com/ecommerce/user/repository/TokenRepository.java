package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
	@Query("SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id where u.id = :userId and (t.expired = false or t.revoked = false)")
	List<Token> findAllValidTokensByUser(@Param("userId") Long userId);
	
	Optional<Token> findByToken(String token);
}
