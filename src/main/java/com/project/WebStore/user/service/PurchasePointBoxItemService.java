package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.common.util.RandomUtils.getWeightedRandom;
import static com.project.WebStore.error.ErrorCode.INSUFFICIENT_STOCK;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.POINT_BOX_ITEM_TYPE_NOT_EXIST;
import static com.project.WebStore.error.ErrorCode.SALE_PERIOD_ENDED;
import static com.project.WebStore.error.ErrorCode.SOLD_OUT;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.entity.RandomPointEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.entity.PointHistoryEntity;
import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import com.project.WebStore.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;
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
  private final PointService pointService;

  @Transactional
  public PurchaseDto.Response purchasePointBoxItem(
      PointBoxItemEntity pointBoxItemEntity, UserEntity userEntity, PurchaseDto.Request request
  ) {
    log.info("purchasePointBoxItem 시작");

    int purchaseQuantity = request.getQuantity();

    log.info("구매가능한지 공통부분 검증");
    purchaseValidationCommonService.validatePurchaseCommon(userEntity, pointBoxItemEntity, purchaseQuantity);

    log.info("포인트 박스 아이템 구매가능 검증");
    validatePurchasePointBoxItem(pointBoxItemEntity, purchaseQuantity);

    log.info("유저 포인트 차감");
    pointService.decreasePoint(pointBoxItemEntity, userEntity, purchaseQuantity);

    log.info("아이템 구매 기록 저장");
    PurchaseHistoryEntity purchaseHistoryEntity =
        PurchaseHistoryEntity.createEntity(userEntity, pointBoxItemEntity, purchaseQuantity);
    purchaseHistoryRepository.save(purchaseHistoryEntity);

    log.info("아이템 재고 감소");
    stockService.decreaseStock(pointBoxItemEntity, purchaseQuantity);

    log.info("포인트 사용 기록 저장");
    PointHistoryEntity pointHistoryEntityForUse =
        PointHistoryEntity.createEntityForUse(userEntity, pointBoxItemEntity, purchaseQuantity,
        purchaseHistoryEntity.getPurchasedAt());
    pointHistoryRepository.save(pointHistoryEntityForUse);

    log.info("유저 포인트 적립(구매 즉시 적립)");
    int earnPoint = getEarnPoint(request, pointBoxItemEntity);
    userEntity.increasePoint(earnPoint);
    userRepository.save(userEntity);

    log.info("포인트 적립 기록 저장");
    PointHistoryEntity pointHistoryEntityForEarn =
        PointHistoryEntity.createEntityForEarn(userEntity, pointBoxItemEntity, earnPoint, purchaseQuantity);
    pointHistoryRepository.save(pointHistoryEntityForEarn);

    log.info("purchasePointBoxItem 종료");

    // 구매 결과 반환
    List<PointHistoryEntity> pointHistoryEntities = List.of(pointHistoryEntityForUse, pointHistoryEntityForEarn);
    return PurchaseDto.Response.from(pointHistoryEntities);
  }


  public PointBoxItemEntity getPointBoxItemEntity(Long id, ItemType type) {
    return pointBoxItemRepository.findByIdAndType(id, type)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
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

  private int getEarnPoint(PurchaseDto.Request request, PointBoxItemEntity pointBoxItemEntity) {
    int itemQuantity = request.getQuantity();

    if (request.getType() == FIXED_POINT_BOX_ITEM) {
      return calcEarnPoint(itemQuantity, () -> getFixedPoint(pointBoxItemEntity));
    } else if (request.getType() == RANDOM_POINT_BOX_ITEM) {
      return calcEarnPoint(itemQuantity, () -> getRandomPoint(pointBoxItemEntity));
    } else {
      throw new WebStoreException(POINT_BOX_ITEM_TYPE_NOT_EXIST);
    }
  }

  private int calcEarnPoint(int itemQuantity, Supplier<Integer> supplier) {
    return IntStream.range(0, itemQuantity)
        .map(i -> supplier.get())
        .sum();
  }

  private int getFixedPoint(PointBoxItemEntity pointBoxItemEntity) {
    Map<Integer, Double> map = new HashMap<>();
    pointBoxItemEntity.getFixedPointEntities()
        .forEach(fixedPointEntity -> map.put(fixedPointEntity.getPointAmount(), fixedPointEntity.getProbability()));
    return getWeightedRandom(map, ThreadLocalRandom.current());
  }

  private int getRandomPoint(PointBoxItemEntity pointBoxItemEntity) {
    RandomPointEntity randomPointEntity = pointBoxItemEntity.getRandomPointEntities().get(0);
    int minPoint = randomPointEntity.getMinPoint();
    int maxPoint = randomPointEntity.getMaxPoint();

    return ThreadLocalRandom.current().nextInt(minPoint, maxPoint + 1);
  }
}
