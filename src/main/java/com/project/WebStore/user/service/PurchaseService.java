package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemType.CASH_ITEM;
import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.ITEM_TYPE_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.USER_NOT_FOUND;

import com.project.WebStore.common.redis.DistributedLock;
import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {

  private final UserRepository userRepository;
  private final PurchasePointBoxItemService purchasePointBoxItemService;
  private final PurchaseCashItemService purchaseCashItemService;

  @DistributedLock(key = "'id-' + #itemId + '  type-' + #request.type")
  @Transactional
  public PurchaseDto.Response purchase(Long itemId, UserDetails userDetails, PurchaseDto.Request request) {
    UserEntity userEntity = getUserEntity(userDetails);
    ItemType type = request.getType();

    if (type == FIXED_POINT_BOX_ITEM || type == RANDOM_POINT_BOX_ITEM) {
      PointBoxItemEntity pointBoxItemEntity = purchasePointBoxItemService.getPointBoxItemEntity(itemId, type);
      return purchasePointBoxItemService.purchasePointBoxItem(pointBoxItemEntity, userEntity, request);
    } else if (type == CASH_ITEM) {
      CashItemEntity cashItemEntity = purchaseCashItemService.getCashItemEntity(itemId);
      return purchaseCashItemService.purchaseCashItem(cashItemEntity, userEntity, request);
    } else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }

  private UserEntity getUserEntity(UserDetails userDetails) {
    String email = userDetails.getUsername();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(USER_NOT_FOUND));
  }
}
