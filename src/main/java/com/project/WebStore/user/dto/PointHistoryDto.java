package com.project.WebStore.user.dto;

import com.project.WebStore.common.type.HistoryType;
import com.project.WebStore.user.entity.PointHistoryEntity;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDto {
  private int pointAmount;
  private HistoryType type;
  private String transactionAt;

  public static PointHistoryDto from(PointHistoryEntity pointHistoryEntity) {
    return PointHistoryDto.builder()
        .pointAmount(pointHistoryEntity.getPointAmount())
        .type(pointHistoryEntity.getType())
        .transactionAt(pointHistoryEntity.getTransactionAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
        .build();
  }
}
