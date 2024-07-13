package com.project.WebStore.user.entity;

import static com.project.WebStore.common.type.HistoryType.EARN;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.HistoryType;
import com.project.WebStore.item.entity.CashItemEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "cash_history")
public class CashHistoryEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String itemName;

  @Enumerated(EnumType.STRING)
  private HistoryType type;

  private int cashAmount;
  private LocalDateTime transactionAt;

  public static CashHistoryEntity createEntityForEarn(UserEntity userEntity, CashItemEntity cashItemEntity, int earnCash) {
    return CashHistoryEntity.builder()
        .userEntity(userEntity)
        .itemName(cashItemEntity.getName())
        .type(EARN)
        .cashAmount(earnCash)
        .transactionAt(LocalDateTime.now())
        .build();
  }
}
