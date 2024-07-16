package com.project.WebStore.user.entity;

import static com.project.WebStore.common.type.HistoryType.EARN;
import static com.project.WebStore.common.type.HistoryType.USE;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.HistoryType;
import com.project.WebStore.item.entity.ItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "point_history")
public class PointHistoryEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String itemName;
  private int itemQuantity;

  @Enumerated(EnumType.STRING)
  private HistoryType type;

  private int pointAmount;
  private LocalDateTime transactionAt;

  public static PointHistoryEntity createEntityForUse(
      UserEntity userEntity, ItemEntity itemEntity, int purchaseQuantity, LocalDateTime purchasedAt
  ) {
    return PointHistoryEntity.builder()
        .userEntity(userEntity)
        .itemName(itemEntity.getName())
        .itemQuantity(purchaseQuantity)
        .type(USE)
        .pointAmount(itemEntity.getRequiredPoint() * purchaseQuantity)
        .transactionAt(purchasedAt)
        .build();
  }

  public static PointHistoryEntity createEntityForEarn(
      UserEntity userEntity, PointBoxItemEntity pointBoxItemEntity, int earnPoint, int purchaseQuantity
  ) {
    return PointHistoryEntity.builder()
        .userEntity(userEntity)
        .itemName(pointBoxItemEntity.getName())
        .itemQuantity(purchaseQuantity)
        .type(EARN)
        .pointAmount(earnPoint)
        .transactionAt(LocalDateTime.now())
        .build();
  }
}
