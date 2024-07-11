package com.project.WebStore.item.dto;

import com.project.WebStore.common.type.ItemType;
import java.util.List;

public interface PointBoxItemRequest {
  ItemType getType();
  List<FixedPointDto> getFixedPointDtos();
  List<RandomPointDto> getRandomPointDtos();
}
