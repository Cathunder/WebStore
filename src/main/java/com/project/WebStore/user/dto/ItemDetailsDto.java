package com.project.WebStore.user.dto;

import static com.project.WebStore.common.type.PointBoxItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.PointBoxItemType.RANDOM_POINT_BOX_ITEM;

import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.RandomPointDto;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDetailsDto {
  private String name;
  private List<Integer> fixedPointsAmount;
  private List<Integer> randomPointsMinMax;
  private int requiredPoint;
  private int stock;
  private int dailyLimitCount;
  private String startedAt;
  private String endedAt;

  public static ItemDetailsDto from(PointBoxItemEntity pointBoxItemEntity) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");

    ItemDetailsDtoBuilder builder = ItemDetailsDto.builder()
        .name(pointBoxItemEntity.getName())
        .requiredPoint(pointBoxItemEntity.getRequiredPoint())
        .stock(pointBoxItemEntity.getStock())
        .dailyLimitCount(pointBoxItemEntity.getDailyLimitCount())
        .startedAt(pointBoxItemEntity.getStartedAt().format(formatter))
        .endedAt(pointBoxItemEntity.getEndedAt().format(formatter));

    if (pointBoxItemEntity.getType() == FIXED_POINT_BOX_ITEM) {
      builder.fixedPointsAmount(getFixedPointsAmount(pointBoxItemEntity));
    } else if (pointBoxItemEntity.getType() == RANDOM_POINT_BOX_ITEM) {
      builder.randomPointsMinMax(getRandomPointsMinMax(pointBoxItemEntity));
    }

    return builder.build();
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
