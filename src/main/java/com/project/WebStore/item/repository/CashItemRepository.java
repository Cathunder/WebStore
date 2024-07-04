package com.project.WebStore.item.repository;

import com.project.WebStore.item.entity.CashItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashItemRepository extends JpaRepository<CashItemEntity, Long> {
  boolean existsByName(String name);
}
