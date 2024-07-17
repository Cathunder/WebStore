package com.project.WebStore.user.service;

import static com.project.WebStore.error.ErrorCode.INSUFFICIENT_STOCK;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.SALE_PERIOD_ENDED;
import static com.project.WebStore.error.ErrorCode.SOLD_OUT;
import static com.project.WebStore.user.util.PurchaseUtils.getDecreasePoint;
import static com.project.WebStore.user.util.PurchaseUtils.getEarnPoint;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.entity.PointHistoryEntity;
import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import com.project.WebStore.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchasePointBoxItemService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
  private final PurchaseValidationCommonService purchaseValidationCommonService;
  private final UserRepository userRepository;
  private final StockService stockService;

  public PointBoxItemEntity getPointBoxItemEntity(Long id, ItemType type) {
    return pointBoxItemRepository.findByIdAndType(id, type)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  public void validatePurchasePointBoxItem(PointBoxItemEntity pointBoxItemEntity, int purchaseQuantity) {
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

  @Transactional
  public PurchaseDto.Response purchasePointBoxItem(
      PointBoxItemEntity pointBoxItemEntity, UserEntity userEntity, PurchaseDto.Request request
  ) {
    log.info("purchasePointBoxItem 시작");

    int purchaseQuantity = request.getQuantity();

    // 구매가능한지 공통부분 검증
    purchaseValidationCommonService.validatePurchaseCommon(userEntity, pointBoxItemEntity, purchaseQuantity);

    // 포인트 박스 아이템 구매가능 검증
    validatePurchasePointBoxItem(pointBoxItemEntity, purchaseQuantity);

    // 유저 포인트 차감
    userEntity.decreasePoint(getDecreasePoint(pointBoxItemEntity.getRequiredPoint(), purchaseQuantity));
    userRepository.save(userEntity);

    // 아이템 구매 기록 저장
    PurchaseHistoryEntity purchaseHistoryEntity =
        PurchaseHistoryEntity.createEntity(userEntity, pointBoxItemEntity, purchaseQuantity);
    purchaseHistoryRepository.save(purchaseHistoryEntity);

    // 아이템 재고 감소
    log.info("아이템 재고 감소");
    stockService.decreaseStock(pointBoxItemEntity, purchaseQuantity);

    // 포인트 사용 기록 저장
    PointHistoryEntity pointHistoryEntityForUse
        = PointHistoryEntity.createEntityForUse(userEntity, pointBoxItemEntity, purchaseQuantity,
        purchaseHistoryEntity.getPurchasedAt());
    pointHistoryRepository.save(pointHistoryEntityForUse);

    // 유저 포인트 적립(구매 즉시 적립)
    int earnPoint = getEarnPoint(request, pointBoxItemEntity);
    userEntity.increasePoint(earnPoint);
    userRepository.save(userEntity);

    // 포인트 적립 기록 저장
    PointHistoryEntity pointHistoryEntityForEarn
        = PointHistoryEntity.createEntityForEarn(userEntity, pointBoxItemEntity, earnPoint, purchaseQuantity);
    pointHistoryRepository.save(pointHistoryEntityForEarn);

    log.info("purchasePointBoxItem 종료");

    // 구매 결과 반환
    List<PointHistoryEntity> pointHistoryEntities = List.of(pointHistoryEntityForUse, pointHistoryEntityForEarn);
    return PurchaseDto.Response.from(pointHistoryEntities);
  }
}
