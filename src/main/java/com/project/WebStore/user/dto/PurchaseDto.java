package com.project.WebStore.user.dto;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.common.validation.ValidItemType;
import com.project.WebStore.user.entity.CashHistoryEntity;
import com.project.WebStore.user.entity.PointHistoryEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PurchaseDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    @NotNull(message = "아이템형을 입력하세요.")
    @ValidItemType
    private ItemType type;

    @NotNull(message = "구매수량을 입력하세요.")
    @Min(1)
    private int quantity;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private List<PointHistoryDto> pointHistoryDtos;
    private List<CashHistoryDto> cashHistoryDtos;

    public static Response from(List<PointHistoryEntity> pointHistoryEntities) {
      List<PointHistoryDto> pointHistoryDtos = pointHistoryEntities.stream()
          .map(PointHistoryDto::from)
          .toList();

      return Response.builder()
          .pointHistoryDtos(pointHistoryDtos)
          .build();
    }

    public static Response from(PointHistoryEntity pointHistoryEntity, CashHistoryEntity cashHistoryEntity) {
      PointHistoryDto pointHistoryDto = PointHistoryDto.from(pointHistoryEntity);
      CashHistoryDto cashHistoryDto = CashHistoryDto.from(cashHistoryEntity);

      return Response.builder()
          .pointHistoryDtos(List.of(pointHistoryDto))
          .cashHistoryDtos(List.of(cashHistoryDto))
          .build();
    }
  }
}
