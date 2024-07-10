package com.project.WebStore.item.entity;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;
import static com.project.WebStore.common.type.ItemStatus.INACTIVE;
import static com.project.WebStore.error.ErrorCode.ALREADY_INACTIVE;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.RegisterCashItemDto.Request;
import com.project.WebStore.item.dto.UpdateCashItemDto;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "cash_item")
public class CashItemEntity extends ItemEntity {

  private int cashAmount;

  public static CashItemEntity create(Request request, AdminEntity adminEntity) {
    return CashItemEntity.builder()
        .adminEntity(adminEntity)
        .name(request.getName())
        .cashAmount(request.getCashAmount())
        .requiredPoint(request.getRequiredPoint())
        .dailyLimitCount(request.getDailyLimitCount())
        .status(ACTIVE)
        .build();
  }

  public void updateEntity(UpdateCashItemDto.Request request) {
    this.name = request.getName();
    this.cashAmount = request.getCashAmount();
    this.requiredPoint = request.getRequiredPoint();
    this.dailyLimitCount = request.getDailyLimitCount();
  }

  public void changeStatusToInactive() {
    if (this.status == INACTIVE) {
      throw new WebStoreException(ALREADY_INACTIVE);
    }
    this.status = INACTIVE;
  }
}
