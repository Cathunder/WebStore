package com.project.WebStore.user.dto;

import com.project.WebStore.user.entity.PurchaseHistoryEntity;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseHistoryDto {

  private Long userId;
  private Long itemId;
  private String itemName;
  private int quantity;
  private String purchasedAt;

  public static PurchaseHistoryDto from(PurchaseHistoryEntity purchaseHistoryEntity) {
    return PurchaseHistoryDto.builder()
        .userId(purchaseHistoryEntity.getUserEntity().getId())
        .itemId(purchaseHistoryEntity.getItemId())
        .itemName(purchaseHistoryEntity.getItemName())
        .quantity(purchaseHistoryEntity.getQuantity())
        .purchasedAt(purchaseHistoryEntity.getPurchasedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
        .build();
  }
}
