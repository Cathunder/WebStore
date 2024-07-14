package com.project.WebStore.user.repository;

import com.project.WebStore.user.entity.CashHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashHistoryRepository extends JpaRepository<CashHistoryEntity, Long> {

  @Query("SELECT p FROM cash_history p WHERE p.userEntity.id = :userId")
  Page<CashHistoryEntity> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
