package com.project.WebStore.user.service;

import com.project.WebStore.common.redis.DistributedLock;
import com.project.WebStore.item.entity.ItemEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserRepository userRepository;

  @DistributedLock(key = "'userEmail-' + #userEntity.getEmail()")
  public void decreasePoint(ItemEntity itemEntity, UserEntity userEntity, int purchaseQuantity) {
    int decreasePoint = itemEntity.getRequiredPoint() * purchaseQuantity;

    userEntity.decreasePoint(decreasePoint);
    userRepository.save(userEntity);
  }
}
