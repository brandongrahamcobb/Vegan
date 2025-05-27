package com.brandongcobb.vegan.store.repo;

import com.brandongcobb.vegan.store.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.vegan.id = :veganId")
    List<Order> getVeganByUserId(@Param("veganId") Long veganId);
}