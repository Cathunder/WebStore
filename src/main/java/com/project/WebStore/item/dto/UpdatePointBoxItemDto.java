package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.PointBoxItemType;
import com.project.WebStore.common.validation.ValidEndedAt;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class UpdatePointBoxItemDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ValidEndedAt
  public static class Request {

    private String name;

    @Valid
    private List<FixedPointDto> fixedPoints;

    @Valid
    private List<RandomPointDto> randomPoints;

    @Min(value = 0, message = "구매시 필요한 포인트는 0 이상이어야 합니다.")
    private int requiredPoint;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private int stock;

    @DateTimeFormat(iso = ISO.TIME)
    private LocalTime stockResetTime;

    @Min(value = 1, message = "일일 구매 제한 개수는 1 이상이어야 합니다.")
    private int dailyLimitCount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future(message = "판매 시작일은 현재 시간 이후여야 합니다.")
    private LocalDateTime startedAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endedAt;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private Long id;
    private Long adminId;
    private String name;
    private PointBoxItemType type;
    private List<FixedPointDto> fixedPoints;
    private List<RandomPointDto> randomPoints;
    private int requiredPoint;
    private int stock;
    private LocalTime stockResetTime;
    private int dailyLimitCount;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private ItemStatus status;

    public static UpdatePointBoxItemDto.Response from(PointBoxItemEntity pointBoxItemEntity) {
      return Response.builder()
          .id(pointBoxItemEntity.getId())
          .adminId(pointBoxItemEntity.getAdminEntity().getId())
          .name(pointBoxItemEntity.getName())
          .type(pointBoxItemEntity.getType())
          .fixedPoints(FixedPointDto.from(pointBoxItemEntity.getFixedPointEntities()))
          .randomPoints(RandomPointDto.from(pointBoxItemEntity.getRandomPointEntities()))
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
}
