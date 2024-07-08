package com.project.WebStore.user.dto;

import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

  private String name;
  private int requiredPoint;
  private Integer stock;

  public static ItemDto from(PointBoxItemEntity pointBoxItemEntity) {
    return ItemDto.builder()
        .name(pointBoxItemEntity.getName())
        .requiredPoint(pointBoxItemEntity.getRequiredPoint())
        .stock(pointBoxItemEntity.getStock())
        .build();
  }

  public static ItemDto from(CashItemEntity cashItemEntity) {
    return ItemDto.builder()
        .name(cashItemEntity.getName())
        .requiredPoint(cashItemEntity.getRequiredPoint())
        .build();
  }
}
