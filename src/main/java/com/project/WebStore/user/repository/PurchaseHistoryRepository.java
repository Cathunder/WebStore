package com.project.WebStore.user.repository;

import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {

  @Query("SELECT SUM(p.quantity) "
      + "FROM purchase_history p "
      + "WHERE p.userEntity.id = :userId "
      + "AND p.itemName = :itemName "
      + "AND DATE(p.purchasedAt) = :date")
  Integer findTotalPurchasedQuantityToday(
      @Param("userId") Long userId,
      @Param("itemName") String itemName,
      @Param("date") LocalDate date
  );

  @Query("SELECT p FROM purchase_history p WHERE p.userEntity.id = :userId")
  Page<PurchaseHistoryEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
