package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.INACTIVE;
import static com.project.WebStore.error.ErrorCode.DAILY_LIMIT_REACHED;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.NOT_ENOUGH_POINT;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.ItemEntity;
import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import java.time.LocalDate;
import java.util.List;
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

    List<PurchaseHistoryEntity> purchaseHistoryEntities = getPurchaseHistoryEntities(userEntity.getId(),
        itemEntity.getName());
    if (!purchaseHistoryEntities.isEmpty()) {
      int totalQuantityPurchasedToday = purchaseHistoryEntities.stream()
          .mapToInt(PurchaseHistoryEntity::getQuantity)
          .sum();

      if (totalQuantityPurchasedToday + purchaseQuantity > itemEntity.getDailyLimitCount()) {
        throw new WebStoreException(DAILY_LIMIT_REACHED);
      }
    }
  }

  private List<PurchaseHistoryEntity> getPurchaseHistoryEntities(Long userId, String itemName) {
    return purchaseHistoryRepository.findAllByUserIdAndItemNameAndDate(userId, itemName, LocalDate.now());
  }
}
