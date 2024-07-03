package com.project.WebStore.item.repository;

import com.project.WebStore.item.entity.PointBoxItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointBoxItemRepository extends JpaRepository<PointBoxItemEntity, Long> {
  boolean existsByName(String name);
}
