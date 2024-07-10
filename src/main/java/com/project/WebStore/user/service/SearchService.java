package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;
import static com.project.WebStore.common.type.ItemType.CASH_ITEM;
import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.CashItemRepository;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.dto.ItemDetailsDto;
import com.project.WebStore.user.dto.ItemDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final CashItemRepository cashItemRepository;

  // findAllItems
  public Page<Object> findAllItems(Pageable pageable) {
    List<ItemDto> pointBoxItemDtos = getPointBoxItemDtos(pageable);
    List<ItemDto> cashItemDtos = getCashItemDtos(pageable);

    List<Object> itemDtos = Stream.concat(pointBoxItemDtos.stream(), cashItemDtos.stream())
        .collect(Collectors.toList());

    List<Object> contents = getContents(pageable, itemDtos);

    return new PageImpl<>(contents, pageable, itemDtos.size());
  }

  private List<ItemDto> getPointBoxItemDtos(Pageable pageable) {
    Page<PointBoxItemEntity> pointBoxItemEntitiesPage = pointBoxItemRepository.findAll(pageable);
    return pointBoxItemEntitiesPage.stream()
        .filter(pointBoxItemEntity -> pointBoxItemEntity.getStatus() == ACTIVE
            && pointBoxItemEntity.getStartedAt().isBefore(LocalDateTime.now())) // 현재시간 기준 판매중인 포인트박스만 검색
        .map(ItemDto::from)
        .toList();
  }

  private List<ItemDto> getCashItemDtos(Pageable pageable) {
    Page<CashItemEntity> cashItemEntitiesPage = cashItemRepository.findAll(pageable);
    return cashItemEntitiesPage.stream()
        .filter(cashItemEntity -> cashItemEntity.getStatus() == ACTIVE)
        .map(ItemDto::from)
        .toList();
  }

  private List<Object> getContents(Pageable pageable, List<Object> itemDtos) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), itemDtos.size());
    return itemDtos.subList(start, end);
  }

  // getItemDetails
  public ItemDetailsDto.Response getItemDetails(Long itemId, ItemDetailsDto.Request request) {
    ItemType type = request.getType();

    if (type == FIXED_POINT_BOX_ITEM || type == RANDOM_POINT_BOX_ITEM) {
      PointBoxItemEntity pointBoxItemEntity = getPointBoxItemEntity(itemId);
      checkValidation(pointBoxItemEntity, type);
      return ItemDetailsDto.Response.from(pointBoxItemEntity);
    } else if(type == CASH_ITEM) {
      CashItemEntity cashItemEntity = getCashItemEntity(itemId);
      checkValidation(cashItemEntity);
      return ItemDetailsDto.Response.from(cashItemEntity);
    } else {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }

  private PointBoxItemEntity getPointBoxItemEntity(Long itemId) {
    return pointBoxItemRepository.findById(itemId)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private void checkValidation(PointBoxItemEntity pointBoxItemEntity, ItemType type) {
    if (pointBoxItemEntity.getStatus() != ACTIVE
        || pointBoxItemEntity.getType() != type
        || pointBoxItemEntity.getStartedAt().isAfter(LocalDateTime.now())
    ) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }

  private CashItemEntity getCashItemEntity(Long itemId) {
    return cashItemRepository.findById(itemId)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private void checkValidation(CashItemEntity cashItemEntity) {
    if (cashItemEntity.getStatus() != ACTIVE) {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }
}
