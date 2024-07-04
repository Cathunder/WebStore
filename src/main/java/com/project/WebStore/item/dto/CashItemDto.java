package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.item.entity.CashItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashItemDto {
  private Long id;
  private Long adminId;
  private String name;
  private int requiredPoint;
  private int dailyLimitCount;
  private ItemStatus status;

  public static CashItemDto from(CashItemEntity cashItemEntity) {
    return CashItemDto.builder()
        .id(cashItemEntity.getId())
        .adminId(cashItemEntity.getAdminEntity().getId())
        .name(cashItemEntity.getName())
        .requiredPoint(cashItemEntity.getRequiredPoint())
        .dailyLimitCount(cashItemEntity.getDailyLimitCount())
        .status(cashItemEntity.getStatus())
        .build();
  }
}
