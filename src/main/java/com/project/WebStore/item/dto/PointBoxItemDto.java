package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.PointBoxItemType;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private PointBoxItemType type;
  private List<FixedPointDto> fixedPointDtos = new ArrayList<>();
  private List<RandomPointDto> randomPointDtos = new ArrayList<>();
  private int requiredPoint;
  private int stock;
  private LocalTime stockResetTime;
  private int dailyLimitCount;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
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
        .stockResetTime(pointBoxItemEntity.getStockResetTime())
        .dailyLimitCount(pointBoxItemEntity.getDailyLimitCount())
        .startedAt(pointBoxItemEntity.getStartedAt())
        .endedAt(pointBoxItemEntity.getEndedAt())
        .status(pointBoxItemEntity.getStatus())
        .build();
  }
}
