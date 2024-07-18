package com.project.WebStore.common.scheduler;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;

import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockInitializeScheduler {

  private final PointBoxItemRepository pointBoxItemRepository;

  @Transactional
  @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Seoul")
  public void resetStock() {
    List<PointBoxItemEntity> pointBoxItemEntities = pointBoxItemRepository.findAllByStatus(ACTIVE);
    for (PointBoxItemEntity entity : pointBoxItemEntities) {
      entity.initializeStock();
    }
    pointBoxItemRepository.saveAll(pointBoxItemEntities);
    log.info("초기화완료");
  }
}
