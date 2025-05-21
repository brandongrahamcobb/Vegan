package com.brandongcobb.vegan.store.repo;

import com.brandongcobb.vegan.store.domain.Vegan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VeganRepository extends JpaRepository<Vegan, Long> {
    Optional<Vegan> findByEmail(String email);
    boolean existsByEmail(String email);
}