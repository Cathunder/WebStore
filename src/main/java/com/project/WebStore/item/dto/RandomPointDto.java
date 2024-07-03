package com.project.WebStore.item.dto;

import com.project.WebStore.common.validation.ValidMaxPoint;
import com.project.WebStore.item.entity.RandomPointEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidMaxPoint
public class RandomPointDto {

  @NotNull(message = "최소 포인트를 입력하세요.")
  @Min(value = 0, message = "최소 포인트는 0부터 입력이 가능합니다.")
  private int minPoint;

  @NotNull(message = "최대 포인트를 입력하세요.")
  private int maxPoint;

  public static List<RandomPointDto> from(List<RandomPointEntity> randomPointEntities) {
    return randomPointEntities.stream()
        .map(
            randomPointEntity -> RandomPointDto.builder()
                .minPoint(randomPointEntity.getMinPoint())
                .maxPoint(randomPointEntity.getMaxPoint())
                .build())
        .toList();
  }
}
