package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.INACTIVE;
import static com.project.WebStore.error.ErrorCode.DAILY_LIMIT_REACHED;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.NOT_ENOUGH_POINT;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.ItemEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseValidationCommonService {

  private final PurchaseHistoryRepository purchaseHistoryRepository;

  public void validatePurchaseCommon(UserEntity userEntity, ItemEntity itemEntity, int purchaseQuantity) {
    if (itemEntity.getStatus() == INACTIVE) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }

    if (userEntity.getPoint() < itemEntity.getRequiredPoint()) {
      throw new WebStoreException(NOT_ENOUGH_POINT);
    }

    Integer totalPurchasedQuantityToday = getTotalPurchasedQuantityToday(userEntity.getId(), itemEntity.getName());

    if (totalPurchasedQuantityToday == null) {
      totalPurchasedQuantityToday = 0;
    }

    if (totalPurchasedQuantityToday + purchaseQuantity > itemEntity.getDailyLimitCount()) {
      throw new WebStoreException(DAILY_LIMIT_REACHED);
    }
  }

  private Integer getTotalPurchasedQuantityToday(Long userId, String itemName) {
    return purchaseHistoryRepository.findTotalPurchasedQuantityToday(userId, itemName, LocalDate.now());
  }
}
