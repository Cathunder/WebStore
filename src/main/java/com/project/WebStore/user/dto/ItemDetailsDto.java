package com.project.WebStore.user.dto;

import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.POINT_BOX_ITEM_TYPE_NOT_EXIST;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.common.validation.ValidItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.RandomPointDto;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import jakarta.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ItemDetailsDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {
    @NotNull(message = "아이템 타입을 입력하세요.")
    @ValidItemType
    private ItemType type;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String name;
    private List<Integer> fixedPointsAmount;
    private List<Integer> randomPointsMinMax;
    private Integer cashAmount;
    private Integer requiredPoint;
    private Integer stock;
    private Integer dailyLimitCount;
    private String startedAt;
    private String endedAt;

    public static Response from(PointBoxItemEntity pointBoxItemEntity) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");
      ItemType type = pointBoxItemEntity.getType();

      ResponseBuilder responseBuilder = getResponseBuilder(pointBoxItemEntity);

      if (type == FIXED_POINT_BOX_ITEM) {
        responseBuilder.fixedPointsAmount(getFixedPointsAmount(pointBoxItemEntity));
      } else if (type == RANDOM_POINT_BOX_ITEM) {
        responseBuilder.randomPointsMinMax(getRandomPointsMinMax(pointBoxItemEntity));
      } else {
        throw new WebStoreException(POINT_BOX_ITEM_TYPE_NOT_EXIST);
      }

      responseBuilder.stock(pointBoxItemEntity.getStock())
          .startedAt(pointBoxItemEntity.getStartedAt().format(formatter))
          .endedAt(pointBoxItemEntity.getEndedAt().format(formatter));

      return responseBuilder.build();
    }

    public static Response from(CashItemEntity cashItemEntity) {
      ResponseBuilder responseBuilder = Response.builder()
          .name(cashItemEntity.getName())
          .cashAmount(cashItemEntity.getCashAmount())
          .requiredPoint(cashItemEntity.getRequiredPoint())
          .dailyLimitCount(cashItemEntity.getDailyLimitCount());

      return responseBuilder.build();
    }

    private static ResponseBuilder getResponseBuilder(PointBoxItemEntity pointBoxItemEntity) {
      return Response.builder()
          .name(pointBoxItemEntity.getName())
          .requiredPoint(pointBoxItemEntity.getRequiredPoint())
          .dailyLimitCount(pointBoxItemEntity.getDailyLimitCount());
    }

    private static List<Integer> getFixedPointsAmount(PointBoxItemEntity pointBoxItemEntity) {
      return FixedPointDto.from(pointBoxItemEntity.getFixedPointEntities()).stream()
          .map(FixedPointDto::getPointAmount)
          .toList();
    }

    private static List<Integer> getRandomPointsMinMax(PointBoxItemEntity pointBoxItemEntity) {
      return RandomPointDto.from(pointBoxItemEntity.getRandomPointEntities()).stream()
          .flatMap(randomPointDto -> Stream.of(randomPointDto.getMinPoint(), randomPointDto.getMaxPoint()))
          .toList();
    }
  }
}
