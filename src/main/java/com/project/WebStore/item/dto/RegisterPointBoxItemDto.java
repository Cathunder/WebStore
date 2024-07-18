package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.common.validation.ValidItemTypeAndPoints;
import com.project.WebStore.common.validation.ValidPointBoxItemType;
import com.project.WebStore.common.validation.ValidProbabilitySum;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegisterPointBoxItemDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ValidProbabilitySum
  @ValidItemTypeAndPoints
  public static class Request extends SalePeriod implements PointBoxItemRequest {

    @NotBlank(message = "아이템명을 입력하세요.")
    private String name;

    @NotNull(message = "박스형을 입력하세요.")
    @ValidPointBoxItemType
    private ItemType type;

    @Valid
    private List<FixedPointDto> fixedPointDtos;

    @Valid
    private List<RandomPointDto> randomPointDtos;

    @NotNull(message = "구매시 필요한 포인트를 입력하세요.")
    @Min(value = 0, message = "구매시 필요한 포인트는 0 이상이어야 합니다.")
    private int requiredPoint;

    @NotNull(message = "재고를 입력하세요.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private int stock;

    @NotNull(message = "매일 초기화되는 재고 수량을 입력하세요.")
    @Min(value = 0, message = "매일 초기화되는 재고수량은 0 이상이어야 합니다.")
    private int initialStock;

    @NotNull(message = "일일 구매 제한 개수를 입력하세요.")
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
    private ItemType type;
    private List<FixedPointDto> fixedPointDtos;
    private List<RandomPointDto> randomPointDtos;
    private int requiredPoint;
    private int stock;
    private int initialStock;
    private int dailyLimitCount;
    private String startedAt;
    private String endedAt;
    private ItemStatus status;

    public static Response from(PointBoxItemEntity pointBoxItemEntity) {
      return Response.builder()
          .id(pointBoxItemEntity.getId())
          .adminId(pointBoxItemEntity.getAdminEntity().getId())
          .name(pointBoxItemEntity.getName())
          .type(pointBoxItemEntity.getType())
          .fixedPointDtos(FixedPointDto.from(pointBoxItemEntity.getFixedPointEntities()))
          .randomPointDtos(RandomPointDto.from(pointBoxItemEntity.getRandomPointEntities()))
          .requiredPoint(pointBoxItemEntity.getRequiredPoint())
          .stock(pointBoxItemEntity.getStock())
          .initialStock(pointBoxItemEntity.getInitialStock())
          .dailyLimitCount(pointBoxItemEntity.getDailyLimitCount())
          .startedAt(pointBoxItemEntity.getStartedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")))
          .endedAt(pointBoxItemEntity.getEndedAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")))
          .status(pointBoxItemEntity.getStatus())
          .build();
    }
  }
}
