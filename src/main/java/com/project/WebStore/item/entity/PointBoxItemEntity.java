package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.PointBoxItemType;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.PERSIST)
  private List<FixedPointEntity> fixedPointEntities = new ArrayList<>();

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.PERSIST)
  private List<RandomPointEntity> randomPointEntities = new ArrayList<>();

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
        .fixedPointEntities(new ArrayList<>())
        .randomPointEntities(new ArrayList<>())
        .requiredPoint(request.getRequiredPoint())
        .stock(request.getStock())
        .stockResetTime(request.getStockResetTime())
        .dailyLimitCount(request.getDailyLimitCount())
        .startedAt(request.getStartedAt())
        .endedAt(request.getEndedAt())
        .status(ItemStatus.ACTIVE)
        .build();
  }

  public void addFixedPointEntities(List<FixedPointEntity> fixedPointEntities) {
    this.fixedPointEntities.addAll(fixedPointEntities);
  }

  public void addRandomPointEntities(List<RandomPointEntity> randomPointEntities) {
    this.randomPointEntities.addAll(randomPointEntities);
  }
}
