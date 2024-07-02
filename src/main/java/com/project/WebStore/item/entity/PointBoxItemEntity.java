package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.PointBoxItemType;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "point_box_item")
public class PointBoxItemEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;

  @Column(unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  private PointBoxItemType type;

  private int requiredPoint;
  private int stock;
  private LocalTime stockResetTime;
  private int dailyLimitCount;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;

  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  public static PointBoxItemEntity of(RegisterPointBoxItemDto.Request request, AdminEntity adminEntity) {
    return PointBoxItemEntity.builder()
        .adminEntity(adminEntity)
        .name(request.getName())
        .type(request.getType())
        .requiredPoint(request.getRequiredPoint())
        .stock(request.getStock())
        .stockResetTime(request.getStockResetTime())
        .dailyLimitCount(request.getDailyLimitCount())
        .startedAt(request.getStartedAt())
        .endedAt(request.getEndedAt())
        .status(ItemStatus.ACTIVE)
        .build();
  }
}
