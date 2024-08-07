package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.item.entity.CashItemEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateCashItemDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {

    @NotBlank(message = "아이템명을 입력하세요.")
    private String name;

    @NotNull(message = "제공되는 캐시를 입력하세요.")
    @Min(value = 0, message = "제공되는 캐시는 0 이상이어야 합니다.")
    private int cashAmount;

    @Min(value = 0, message = "구매시 필요한 포인트는 0 이상이어야 합니다.")
    private int requiredPoint;

    @Min(value = 1, message = "일일 구매 제한 개수는 1 이상이어야 합니다.")
    private int dailyLimitCount;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private Long id;
    private Long adminId;
    private String name;
    private int cashAmount;
    private int requiredPoint;
    private int dailyLimitCount;
    private ItemStatus status;

    public static UpdateCashItemDto.Response from(CashItemEntity cashItemEntity) {
      return UpdateCashItemDto.Response.builder()
          .id(cashItemEntity.getId())
          .adminId(cashItemEntity.getAdminEntity().getId())
          .name(cashItemEntity.getName())
          .cashAmount(cashItemEntity.getCashAmount())
          .requiredPoint(cashItemEntity.getRequiredPoint())
          .dailyLimitCount(cashItemEntity.getDailyLimitCount())
          .status(cashItemEntity.getStatus())
          .build();
    }
  }
}
