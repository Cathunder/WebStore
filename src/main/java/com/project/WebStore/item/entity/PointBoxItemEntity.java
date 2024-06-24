package com.project.WebStore.item.entity;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.common.type.ItemStatus;
import com.project.WebStore.common.type.ItemType;
import jakarta.persistence.Column;
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
@Entity(name = "point_box_item")
public class PointBoxItemEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private AdminEntity adminEntity;

  @Column(unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  private int requiredPoint;
  private int stock;
  private LocalDateTime resetTime;
  private int dailyLimit;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;

  @Enumerated(EnumType.STRING)
  private ItemStatus itemStatus;
}
