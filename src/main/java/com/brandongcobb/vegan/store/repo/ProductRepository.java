package com.brandongcobb.vegan.store.repo;

import com.brandongcobb.vegan.store.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @EntityGraph(attributePaths = {"images", "category"})
    Optional<Product> findProductWithImagesById(Long id);
}
