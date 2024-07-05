package com.project.WebStore.item.repository;

import com.project.WebStore.item.entity.PointBoxItemEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointBoxItemRepository extends JpaRepository<PointBoxItemEntity, Long> {
  Optional<PointBoxItemEntity> findByName(String name);
}
