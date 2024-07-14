package com.project.WebStore.user.dto;

import com.project.WebStore.common.type.HistoryType;
import com.project.WebStore.user.entity.CashHistoryEntity;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashHistoryDto {

  private Long userId;
  private String itemName;
  private int itemQuantity;
  private int cashAmount;
  private HistoryType type;
  private String transactionAt;

  public static CashHistoryDto from(CashHistoryEntity cashHistoryEntity) {
    return CashHistoryDto.builder()
        .userId(cashHistoryEntity.getUserEntity().getId())
        .itemName(cashHistoryEntity.getItemName())
        .itemQuantity(cashHistoryEntity.getItemQuantity())
        .cashAmount(cashHistoryEntity.getCashAmount())
        .type(cashHistoryEntity.getType())
        .transactionAt(cashHistoryEntity.getTransactionAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
        .build();
  }
}
