package com.shoplite.repository;

import com.shoplite.model.Bill;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Long> {
    boolean existsByItems_Product_Id(Long productId);

    @Query("select coalesce(sum(b.totalAmount), 0) from Bill b where b.status = com.shoplite.model.BillStatus.FINALIZED and b.finalizedAt >= :start and b.finalizedAt < :end")
    BigDecimal totalSales(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select count(b) from Bill b where b.status = com.shoplite.model.BillStatus.FINALIZED and b.finalizedAt >= :start and b.finalizedAt < :end")
    long countFinalized(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
