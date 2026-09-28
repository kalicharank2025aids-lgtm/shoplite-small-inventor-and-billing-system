package com.shoplite.repository;

import com.shoplite.model.Product;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    // Reorder alert: stock has fallen below the product's own threshold
    @Query("select p from Product p where p.stockQuantity < p.reorderThreshold order by p.stockQuantity")
    List<Product> findBelowReorderThreshold();
}
