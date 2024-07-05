package com.project.WebStore.user.entity;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.PointBoxItemType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "purchase_item")
public class PurchaseItemEntity extends BaseEntity {

  private Long itemId;

  @Enumerated(EnumType.STRING)
  private PointBoxItemType pointBoxItemType;

  private int quantity;

  private LocalDateTime purchasedAt;
}
