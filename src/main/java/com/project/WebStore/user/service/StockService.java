package com.project.WebStore.user.service;

import static com.project.WebStore.error.ErrorCode.INTERRUPTED_LOCK;
import static com.project.WebStore.error.ErrorCode.NOT_ACQUIRE_LOCK;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final RedissonClient redissonClient;
  private final TransactionSynchronizationService transactionSynchronizationService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void decreaseStock(PointBoxItemEntity pointBoxItemEntity, int purchaseQuantity) {
    String lockKey = "id-" + pointBoxItemEntity.getId() + " type-" + pointBoxItemEntity.getType();
    RLock lock = redissonClient.getLock(lockKey);

    try {
      if (lock.tryLock(5, 3, TimeUnit.SECONDS)) {
        log.info("락 획득: {}", lockKey);
        try {
          transactionSynchronizationService.registerLock(lock);
          log.info("락 등록 완료: {}", lockKey);
          pointBoxItemEntity.decreaseStock(purchaseQuantity);
          pointBoxItemRepository.save(pointBoxItemEntity);
        } catch (Exception e) {
          log.info("락 획득 중 에러발생: {}", lockKey);
          lock.unlock();
          throw e;
        }
      } else {
        log.info("락 획득 실패");
        throw new WebStoreException(NOT_ACQUIRE_LOCK);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.info("InterruptedException");
      throw new WebStoreException(INTERRUPTED_LOCK);
    }
  }
}
