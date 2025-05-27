package com.brandongcobb.vegan.store.repo;

import com.brandongcobb.vegan.store.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @EntityGraph(attributePaths = {"images", "category"})
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> getProductByProductId(@Param("id") Long id);
}
