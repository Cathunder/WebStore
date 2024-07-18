package com.project.WebStore.item.repository;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointBoxItemRepository extends JpaRepository<PointBoxItemEntity, Long> {
  Optional<PointBoxItemEntity> findByName(String name);
  Optional<PointBoxItemEntity> findByIdAndType(Long id, ItemType type);
  List<PointBoxItemEntity> findAllByStatus(ItemStatus status);
}
