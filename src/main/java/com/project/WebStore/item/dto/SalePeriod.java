package com.project.WebStore.item.dto;

import com.project.WebStore.common.validation.ValidEndedAt;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@ValidEndedAt
public abstract class SalePeriod {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future(message = "판매 시작일은 현재시간보다 이후여야 합니다.")
  protected LocalDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future(message = "판매 종료일은 현재시간보다 이후여야 합니다.")
  protected LocalDateTime endedAt;
}
