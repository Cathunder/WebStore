package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.item.dto.RegisterCashItemDto.Request;
import com.project.WebStore.item.dto.UpdateCashItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "cash_item")
public class CashItemEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;

  @Column(unique = true)
  private String name;

  private int requiredPoint;
  private int dailyLimitCount;

  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  public static CashItemEntity create(Request request, AdminEntity adminEntity) {
    return CashItemEntity.builder()
        .adminEntity(adminEntity)
        .name(request.getName())
        .requiredPoint(request.getRequiredPoint())
        .dailyLimitCount(request.getDailyLimitCount())
        .status(ItemStatus.ACTIVE)
        .build();
  }

  public void updateEntity(UpdateCashItemDto.Request request) {
    this.name = request.getName();
    this.requiredPoint = request.getRequiredPoint();
    this.dailyLimitCount = request.getDailyLimitCount();
  }

  public void changeStatusToInactive() {
    this.status = ItemStatus.INACTIVE;
  }
}
