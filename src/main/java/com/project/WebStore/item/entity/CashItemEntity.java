package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;

  @Column(unique = true)
  private String name;

  private int requiredPoint;
  private int dailyLimit;

  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus;
}
