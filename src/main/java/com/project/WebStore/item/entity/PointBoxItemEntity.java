package com.project.WebStore.item.entity;

import static com.project.WebStore.common.type.PointBoxItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.PointBoxItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.ITEM_TYPE_NOT_FOUND;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.PointBoxItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.RandomPointDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
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

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FixedPointEntity> fixedPointEntities = new ArrayList<>();

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RandomPointEntity> randomPointEntities = new ArrayList<>();

  private int requiredPoint;
  private int stock;
  private LocalTime stockResetTime;
  private int dailyLimitCount;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;

  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  public static PointBoxItemEntity create(RegisterPointBoxItemDto.Request request, AdminEntity adminEntity) {
    PointBoxItemEntity pointBoxItemEntity = PointBoxItemEntity.builder()
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

    pointBoxItemEntity.addPoints(request);
    return pointBoxItemEntity;
  }

  private void addPoints(RegisterPointBoxItemDto.Request request) {
    if (FIXED_POINT_BOX_ITEM == this.getType()) {
      updateFixedPointEntities(request.getFixedPoints());
    } else if (RANDOM_POINT_BOX_ITEM == this.getType()) {
      updateRandomPointEntities(request.getRandomPoints());
    } else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }

  public void updateEntity(UpdatePointBoxItemDto.Request request) {
    this.name = request.getName();
    this.requiredPoint = request.getRequiredPoint();
    this.stock = request.getStock();
    this.stockResetTime = request.getStockResetTime();
    this.dailyLimitCount = request.getDailyLimitCount();
    this.startedAt = request.getStartedAt();
    this.endedAt = request.getEndedAt();

    updatePoints(request);
  }

  private void updatePoints(UpdatePointBoxItemDto.Request request) {
    if (FIXED_POINT_BOX_ITEM == this.getType()) {
      this.fixedPointEntities.clear();
      updateFixedPointEntities(request.getFixedPoints());
    } else if (RANDOM_POINT_BOX_ITEM == this.getType()) {
      this.randomPointEntities.clear();
      updateRandomPointEntities(request.getRandomPoints());
    } else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }

  private void updateFixedPointEntities(List<FixedPointDto> fixedPointDtos) {
    List<FixedPointEntity> fixedPointEntities = fixedPointDtos.stream()
        .map(fixedPointDto -> FixedPointEntity.builder()
            .pointBoxItemEntity(this)
            .pointAmount(fixedPointDto.getPointAmount())
            .probability(fixedPointDto.getProbability())
            .build())
        .toList();
    this.fixedPointEntities.addAll(fixedPointEntities);
  }

  private void updateRandomPointEntities(List<RandomPointDto> randomPointDtos) {
    List<RandomPointEntity> randomPointEntities = randomPointDtos.stream()
        .map(randomPointDto -> RandomPointEntity.builder()
            .pointBoxItemEntity(this)
            .minPoint(randomPointDto.getMinPoint())
            .maxPoint(randomPointDto.getMaxPoint())
            .build())
        .toList();
    this.randomPointEntities.addAll(randomPointEntities);
  }

  public void changeStatusToInactive() {
    this.status = ItemStatus.INACTIVE;
  }
}
