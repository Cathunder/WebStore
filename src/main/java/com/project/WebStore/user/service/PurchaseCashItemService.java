package com.project.WebStore.user.service;

import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.user.util.PurchaseUtils.getDecreasePoint;
import static com.project.WebStore.user.util.PurchaseUtils.getEarnCash;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.repository.CashItemRepository;
import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.entity.CashHistoryEntity;
import com.project.WebStore.user.entity.PointHistoryEntity;
import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.CashHistoryRepository;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseCashItemService {

  private final CashItemRepository cashItemRepository;
  private final CashHistoryRepository cashHistoryRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
  private final PurchaseValidationCommonService purchaseValidationCommonService;

  public CashItemEntity getCashItemEntity(Long id) {
    return cashItemRepository.findById(id)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  @Transactional
  public PurchaseDto.Response purchaseCashItem(CashItemEntity cashItemEntity, UserEntity userEntity, PurchaseDto.Request request) {
    int purchaseQuantity = request.getQuantity();

    // 구매가능한지 공통부분 검증
    purchaseValidationCommonService.validatePurchaseCommon(userEntity, cashItemEntity, purchaseQuantity);

    // 유저 포인트 차감
    userEntity.decreasePoint(getDecreasePoint(cashItemEntity.getRequiredPoint(), purchaseQuantity));

    // 아이템 구매 기록 저장
    PurchaseHistoryEntity purchaseHistoryEntity =
        PurchaseHistoryEntity.createEntity(userEntity, cashItemEntity, purchaseQuantity);
    purchaseHistoryRepository.save(purchaseHistoryEntity);

    // 포인트 사용 기록 저장
    PointHistoryEntity pointHistoryEntityForUse
        = PointHistoryEntity.createEntityForUse(userEntity, cashItemEntity, purchaseQuantity,
        purchaseHistoryEntity.getPurchasedAt());
    pointHistoryRepository.save(pointHistoryEntityForUse);

    // 유저 캐시 적립(구매 즉시 적립)
    int earnCash = getEarnCash(request, cashItemEntity);
    userEntity.increaseCash(earnCash);

    // 캐시 적립 기록 저장
    CashHistoryEntity cashHistoryEntityForEarn
        = CashHistoryEntity.createEntityForEarn(userEntity, cashItemEntity, earnCash, purchaseQuantity);
    cashHistoryRepository.save(cashHistoryEntityForEarn);

    // 구매 결과 반환
    return PurchaseDto.Response.from(pointHistoryEntityForUse, cashHistoryEntityForEarn);
  }
}
