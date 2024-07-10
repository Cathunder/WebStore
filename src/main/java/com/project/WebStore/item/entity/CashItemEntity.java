package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.type.ItemStatus;
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
        .status(ItemStatus.ACTIVE)
        .build();
  }

  public void updateEntity(UpdateCashItemDto.Request request) {
    this.name = request.getName();
    this.cashAmount = request.getCashAmount();
    this.requiredPoint = request.getRequiredPoint();
    this.dailyLimitCount = request.getDailyLimitCount();
  }

  public void changeStatusToInactive() {
    this.status = ItemStatus.INACTIVE;
  }
}
