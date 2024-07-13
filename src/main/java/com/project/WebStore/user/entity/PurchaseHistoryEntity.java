package com.project.WebStore.user.entity;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.item.entity.ItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "purchase_history")
@ToString
public class PurchaseHistoryEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private Long itemId;
  private String itemName;
  private int quantity;
  private LocalDateTime purchasedAt;

  public static PurchaseHistoryEntity createEntity(UserEntity userEntity, ItemEntity itemEntity, int purchaseQuantity) {
    return PurchaseHistoryEntity.builder()
        .userEntity(userEntity)
        .itemId(itemEntity.getId())
        .itemName(itemEntity.getName())
        .quantity(purchaseQuantity)
        .purchasedAt(LocalDateTime.now())
        .build();
  }
}
