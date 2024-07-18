package com.project.WebStore.user.service;

import com.project.WebStore.common.redis.DistributedLock;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

  private final PointBoxItemRepository pointBoxItemRepository;

  @DistributedLock(key = "'itemName-' + #pointBoxItemEntity.getName()")
  public void decreaseStock(PointBoxItemEntity pointBoxItemEntity, int purchaseQuantity) {
    pointBoxItemEntity.decreaseStock(purchaseQuantity);
    pointBoxItemRepository.save(pointBoxItemEntity);
  }
}
