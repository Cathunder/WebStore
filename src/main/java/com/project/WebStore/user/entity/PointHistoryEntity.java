package com.project.WebStore.user.entity;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.HistoryType;
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
@Entity(name = "point_history")
public class PointHistoryEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String itemName;

  @Enumerated(EnumType.STRING)
  private HistoryType type;

  private int amount;
  private LocalDateTime transactionAt;
}
