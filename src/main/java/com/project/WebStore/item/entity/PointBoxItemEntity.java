package com.project.WebStore.item.entity;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;
import static com.project.WebStore.common.type.ItemStatus.INACTIVE;
import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.ALREADY_INACTIVE;
import static com.project.WebStore.error.ErrorCode.INSUFFICIENT_STOCK;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_TYPE_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.SOLD_OUT;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.FixedPointDto;
import com.project.WebStore.item.dto.RandomPointDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
import com.project.WebStore.user.dto.ItemDetailsDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "point_box_item")
public class PointBoxItemEntity extends ItemEntity {

  @Enumerated(EnumType.STRING)
  private ItemType type;

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FixedPointEntity> fixedPointEntities = new ArrayList<>();

  @OneToMany(mappedBy = "pointBoxItemEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RandomPointEntity> randomPointEntities = new ArrayList<>();

  private int stock;
  private int initialStock;

  private LocalDateTime startedAt;
  private LocalDateTime endedAt;

  @Override
  public void checkStatus() {
    if (this.getStatus() == INACTIVE || this.getStartedAt().isAfter(LocalDateTime.now())) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }

  @Override
  public ItemDetailsDto.Response toResponse() {
    return ItemDetailsDto.Response.from(this);
  }

  public static PointBoxItemEntity create(RegisterPointBoxItemDto.Request request, AdminEntity adminEntity) {
    PointBoxItemEntity pointBoxItemEntity = PointBoxItemEntity.builder()
        .adminEntity(adminEntity)
        .name(request.getName())
        .type(request.getType())
        .fixedPointEntities(new ArrayList<>())
        .randomPointEntities(new ArrayList<>())
        .requiredPoint(request.getRequiredPoint())
        .stock(request.getStock())
        .initialStock(request.getInitialStock())
        .dailyLimitCount(request.getDailyLimitCount())
        .startedAt(request.getStartedAt())
        .endedAt(request.getEndedAt())
        .status(ACTIVE)
        .build();

    pointBoxItemEntity.addPoints(request);
    return pointBoxItemEntity;
  }

  private void addPoints(RegisterPointBoxItemDto.Request request) {
    if (FIXED_POINT_BOX_ITEM == this.getType()) {
      updateFixedPointEntities(request.getFixedPointDtos());
    } else if (RANDOM_POINT_BOX_ITEM == this.getType()) {
      updateRandomPointEntities(request.getRandomPointDtos());
    } else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }

  public void updateEntity(UpdatePointBoxItemDto.Request request) {
    this.name = request.getName();
    this.requiredPoint = request.getRequiredPoint();
    this.stock = request.getStock();
    this.initialStock = request.getInitialStock();
    this.dailyLimitCount = request.getDailyLimitCount();
    this.startedAt = request.getStartedAt();
    this.endedAt = request.getEndedAt();

    updatePoints(request);
  }

  private void updatePoints(UpdatePointBoxItemDto.Request request) {
    if (FIXED_POINT_BOX_ITEM == this.getType()) {
      this.fixedPointEntities.clear();
      updateFixedPointEntities(request.getFixedPointDtos());
    } else if (RANDOM_POINT_BOX_ITEM == this.getType()) {
      this.randomPointEntities.clear();
      updateRandomPointEntities(request.getRandomPointDtos());
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
        .collect(Collectors.toList());
    this.fixedPointEntities.addAll(fixedPointEntities);
  }

  private void updateRandomPointEntities(List<RandomPointDto> randomPointDtos) {
    List<RandomPointEntity> randomPointEntities = randomPointDtos.stream()
        .map(randomPointDto -> RandomPointEntity.builder()
            .pointBoxItemEntity(this)
            .minPoint(randomPointDto.getMinPoint())
            .maxPoint(randomPointDto.getMaxPoint())
            .build())
        .collect(Collectors.toList());
    this.randomPointEntities.addAll(randomPointEntities);
  }

  public void changeStatusToInactive() {
    if (this.status == INACTIVE) {
      throw new WebStoreException(ALREADY_INACTIVE);
    }
    this.status = INACTIVE;
  }

  public void decreaseStock(int purchaseQuantity) {
    if (this.stock <= 0) {
      throw new WebStoreException(SOLD_OUT);
    }

    if (this.stock < purchaseQuantity) {
      throw new WebStoreException(INSUFFICIENT_STOCK);
    }

    this.stock -= purchaseQuantity;
  }

  public void initializeStock() {
    this.stock = initialStock;
  }
}
