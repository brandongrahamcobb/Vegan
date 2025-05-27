package com.brandongcobb.vegan.store.repo;

import com.brandongcobb.vegan.store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> getCategoryParentIsNull();
    
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> getCategoryByParentId(@Param("parentId") Long parentId);
    
    // This method is already provided by JpaRepository as findById
    // So we don't need to declare it explicitly
    @Query("SELECT c FROM Category c WHERE c.id = :id")
    Optional<Category> getCategoryById(@Param("id") Long id);
}
