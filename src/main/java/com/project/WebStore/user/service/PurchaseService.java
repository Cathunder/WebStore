package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.INACTIVE;
import static com.project.WebStore.common.type.ItemType.CASH_ITEM;
import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.DAILY_LIMIT_REACHED;
import static com.project.WebStore.error.ErrorCode.INSUFFICIENT_STOCK;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_TYPE_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.NOT_ENOUGH_POINT;
import static com.project.WebStore.error.ErrorCode.SALE_PERIOD_ENDED;
import static com.project.WebStore.error.ErrorCode.SOLD_OUT;
import static com.project.WebStore.error.ErrorCode.USER_NOT_FOUND;
import static com.project.WebStore.user.util.PurchaseUtils.getDecreasePoint;
import static com.project.WebStore.user.util.PurchaseUtils.getEarnCash;
import static com.project.WebStore.user.util.PurchaseUtils.getEarnPoint;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.ItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.CashItemRepository;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.entity.CashHistoryEntity;
import com.project.WebStore.user.entity.PointHistoryEntity;
import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.CashHistoryRepository;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import com.project.WebStore.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
  private final PointBoxItemRepository pointBoxItemRepository;
  private final CashItemRepository cashItemRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final CashHistoryRepository cashHistoryRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;

  @Transactional
  public PurchaseDto.Response purchase(Long itemId, UserDetails userDetails, PurchaseDto.Request request) {
    UserEntity userEntity = getUserEntity(userDetails);
    ItemType type = request.getType();
    int purchaseQuantity = request.getQuantity();

    if (type == FIXED_POINT_BOX_ITEM || type == RANDOM_POINT_BOX_ITEM) {
      return purchasePointBoxItem(itemId, type, userEntity, purchaseQuantity, request);
    }
    else if (type == CASH_ITEM) {
      return purchaseCashItem(itemId, userEntity, purchaseQuantity, request);
    }
    else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }


  private UserEntity getUserEntity(UserDetails userDetails) {
    String email = userDetails.getUsername();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(USER_NOT_FOUND));
  }

  private PointBoxItemEntity getPointBoxItemEntity(Long id, ItemType type) {
    return pointBoxItemRepository.findByIdAndType(id, type)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private CashItemEntity getCashItemEntity(Long id) {
    return cashItemRepository.findById(id)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private List<PurchaseHistoryEntity> getPurchaseHistoryEntities(Long userId, String itemName) {
    return purchaseHistoryRepository.findAllByUserIdAndItemNameAndDate(userId, itemName, LocalDate.now());
  }


  private void validatePurchaseCommon(UserEntity userEntity, ItemEntity itemEntity, int purchaseQuantity) {
    if (itemEntity.getStatus() == INACTIVE) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }

    if (userEntity.getPoint() < itemEntity.getRequiredPoint()) {
      throw new WebStoreException(NOT_ENOUGH_POINT);
    }

    List<PurchaseHistoryEntity> purchaseHistoryEntities = getPurchaseHistoryEntities(userEntity.getId(), itemEntity.getName());
    if (!purchaseHistoryEntities.isEmpty()) {
      int totalQuantityPurchasedToday = purchaseHistoryEntities.stream()
          .mapToInt(PurchaseHistoryEntity::getQuantity)
          .sum();

      if (totalQuantityPurchasedToday + purchaseQuantity > itemEntity.getDailyLimitCount()) {
        throw new WebStoreException(DAILY_LIMIT_REACHED);
      }
    }
  }

  private void validatePurchasePointBoxItem(PointBoxItemEntity pointBoxItemEntity, int purchaseQuantity) {
    if (pointBoxItemEntity.getStartedAt().isAfter(LocalDateTime.now())) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }

    if (pointBoxItemEntity.getEndedAt().isBefore(LocalDateTime.now())) {
      throw new WebStoreException(SALE_PERIOD_ENDED);
    }

    if (pointBoxItemEntity.getStock() <= 0) {
      throw new WebStoreException(SOLD_OUT);
    }

    if (pointBoxItemEntity.getStock() < purchaseQuantity) {
      throw new WebStoreException(INSUFFICIENT_STOCK);
    }
  }

  private PurchaseDto.Response purchasePointBoxItem(
      Long itemId, ItemType type, UserEntity userEntity, int purchaseQuantity, PurchaseDto.Request request) {
    PointBoxItemEntity pointBoxItemEntity = getPointBoxItemEntity(itemId, type);

    // 구매 가능 검증
    validatePurchaseCommon(userEntity, pointBoxItemEntity, purchaseQuantity);
    validatePurchasePointBoxItem(pointBoxItemEntity, purchaseQuantity);

    // 유저 포인트 차감
    userEntity.decreasePoint(getDecreasePoint(pointBoxItemEntity.getRequiredPoint(), purchaseQuantity));

    // 아이템 구매 기록 저장
    PurchaseHistoryEntity purchaseHistoryEntity =
        PurchaseHistoryEntity.createEntity(userEntity, pointBoxItemEntity, purchaseQuantity);
    purchaseHistoryRepository.save(purchaseHistoryEntity);

    // 아이템 재고 감소
    pointBoxItemEntity.decreaseStock(purchaseQuantity);

    // 포인트 사용 기록 저장
    PointHistoryEntity pointHistoryEntityForUse
        = PointHistoryEntity.createEntityForUse(userEntity, pointBoxItemEntity, purchaseQuantity,
        purchaseHistoryEntity.getPurchasedAt());
    pointHistoryRepository.save(pointHistoryEntityForUse);

    // 유저 포인트 적립(구매 즉시 적립)
    int earnPoint = getEarnPoint(request, pointBoxItemEntity);
    userEntity.increasePoint(earnPoint);

    // 포인트 적립 기록 저장
    PointHistoryEntity pointHistoryEntityForEarn
        = PointHistoryEntity.createEntityForEarn(userEntity, pointBoxItemEntity, earnPoint);
    pointHistoryRepository.save(pointHistoryEntityForEarn);

    // 구매 결과 반환
    List<PointHistoryEntity> pointHistoryEntities = List.of(pointHistoryEntityForUse, pointHistoryEntityForEarn);
    return PurchaseDto.Response.from(pointHistoryEntities);
  }

  private PurchaseDto.Response purchaseCashItem(Long itemId, UserEntity userEntity, int purchaseQuantity, PurchaseDto.Request request) {
    CashItemEntity cashItemEntity = getCashItemEntity(itemId);

    // 구매 가능 검증
    validatePurchaseCommon(userEntity, cashItemEntity, purchaseQuantity);

    // 유저 포인트 차감
    userEntity.decreasePoint(getDecreasePoint(cashItemEntity.getRequiredPoint(), purchaseQuantity));

    // 아이템 구매 기록 저장
    PurchaseHistoryEntity purchaseHistoryEntity =
        PurchaseHistoryEntity.createEntity(userEntity, cashItemEntity, purchaseQuantity);
    purchaseHistoryRepository.save(purchaseHistoryEntity);

    // 포인트 사용 기록 저장
    PointHistoryEntity pointHistoryEntityForUse
        = PointHistoryEntity.createEntityForUse(userEntity, cashItemEntity, purchaseQuantity, purchaseHistoryEntity.getPurchasedAt());
    pointHistoryRepository.save(pointHistoryEntityForUse);

    // 유저 캐시 적립(구매 즉시 적립)
    int earnCash = getEarnCash(request, cashItemEntity);
    userEntity.increaseCash(earnCash);

    // 캐시 적립 기록 저장
    CashHistoryEntity cashHistoryEntityForEarn
        = CashHistoryEntity.createEntityForEarn(userEntity, cashItemEntity, earnCash);
    cashHistoryRepository.save(cashHistoryEntityForEarn);

    // 구매 결과 반환
    return PurchaseDto.Response.from(pointHistoryEntityForUse, cashHistoryEntityForEarn);
  }
}
