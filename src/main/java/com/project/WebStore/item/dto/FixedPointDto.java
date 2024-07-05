package com.project.WebStore.item.dto;

import com.project.WebStore.item.entity.FixedPointEntity;
import jakarta.validation.constraints.Max;
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
public class FixedPointDto {

  @NotNull(message = "제공할 포인트를 입력하세요.")
  @Min(value = 0, message = "제공할 포인트는 최소 0부터 입력이 가능합니다.")
  private int pointAmount;

  @NotNull(message = "제공할 포인트에 대한 확률을 입력하세요.")
  @Min(value = 0, message = "확률은 최소 0부터 시작해야합니다.")
  @Max(value = 1, message = "확률은 최대 1까지입니다.")
  private double probability;

  public static List<FixedPointDto> from(List<FixedPointEntity> fixedPointEntities) {
    return fixedPointEntities.stream()
        .map(
            fixedPointEntity -> FixedPointDto.builder()
                .pointAmount(fixedPointEntity.getPointAmount())
                .probability(fixedPointEntity.getProbability())
                .build())
        .toList();
  }
}
