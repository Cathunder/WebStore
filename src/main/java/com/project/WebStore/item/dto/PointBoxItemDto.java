package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointBoxItemDto {

  private Long id;
  private Long adminId;
  private String name;
  private ItemType type;
  private List<FixedPointDto> fixedPointDtos = new ArrayList<>();
  private List<RandomPointDto> randomPointDtos = new ArrayList<>();
  private int requiredPoint;
  private int stock;
  private String stockResetTime;
  private int dailyLimitCount;
  private String startedAt;
  private String endedAt;
  private ItemStatus status;

  public static PointBoxItemDto from(PointBoxItemEntity pointBoxItemEntity) {
    return PointBoxItemDto.builder()
        .id(pointBoxItemEntity.getId())
        .adminId(pointBoxItemEntity.getAdminEntity().getId())
        .name(pointBoxItemEntity.getName())
        .type(pointBoxItemEntity.getType())
        .fixedPointDtos(FixedPointDto.from(pointBoxItemEntity.getFixedPointEntities()))
        .randomPointDtos(RandomPointDto.from(pointBoxItemEntity.getRandomPointEntities()))
        .requiredPoint(pointBoxItemEntity.getRequiredPoint())
        .stock(pointBoxItemEntity.getStock())
        .stockResetTime(pointBoxItemEntity.getStockResetTime().format(DateTimeFormatter.ofPattern("HH시")))
        .dailyLimitCount(pointBoxItemEntity.getDailyLimitCount())
        .startedAt(pointBoxItemEntity.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")))
        .endedAt(pointBoxItemEntity.getEndedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")))
        .status(pointBoxItemEntity.getStatus())
        .build();
  }
}
