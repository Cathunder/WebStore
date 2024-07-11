package com.project.WebStore.user.dto;

import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.ItemEntity;
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

  private Long id;
  private String name;
  private int requiredPoint;
  private Integer stock;

  public static ItemDto from(PointBoxItemEntity pointBoxItemEntity) {
    return commonBuilder(pointBoxItemEntity)
        .stock(pointBoxItemEntity.getStock())
        .build();
  }

  public static ItemDto from(CashItemEntity cashItemEntity) {
    return commonBuilder(cashItemEntity)
        .build();
  }

  public static ItemDtoBuilder commonBuilder(ItemEntity itemEntity) {
    return ItemDto.builder()
        .id(itemEntity.getId())
        .name(itemEntity.getName())
        .requiredPoint(itemEntity.getRequiredPoint());
  }
}
