package com.project.WebStore.item.entity;

import com.project.WebStore.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "random_point")
public class RandomPointEntity extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "point_box_item_id")
  private PointBoxItemEntity pointBoxItemEntity;

  private int minPoint;
  private int maxPoint;
}
