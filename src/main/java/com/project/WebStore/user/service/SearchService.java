package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;
import static com.project.WebStore.common.type.ItemType.CASH_ITEM;
import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.CLASS_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_TYPE_NOT_FOUND;

import com.project.WebStore.common.type.ItemType;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.ItemEntity;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final CashItemRepository cashItemRepository;

  // findAllItems
  public Page<ItemDto> findAllItems(Pageable pageable) {
    List<ItemDto> pointBoxItemDtos = findActiveItemDtos(PointBoxItemEntity.class, pageable);
    List<ItemDto> cashItemDtos = findActiveItemDtos(CashItemEntity.class, pageable);

    List<ItemDto> concatItemDtos = Stream.concat(pointBoxItemDtos.stream(), cashItemDtos.stream())
        .collect(Collectors.toList());
    List<ItemDto> contents = getContents(pageable, concatItemDtos);

    return new PageImpl<>(contents, pageable, concatItemDtos.size());
  }

  private <T extends ItemEntity> List<ItemDto> findActiveItemDtos(Class<T> itemEntity, Pageable pageable) {
    if (itemEntity.equals(PointBoxItemEntity.class)) {
      return pointBoxItemRepository.findAll(pageable).stream()
          .filter(pointBoxItemEntity -> pointBoxItemEntity.getStatus() == ACTIVE
                  && pointBoxItemEntity.getStartedAt().isBefore(LocalDateTime.now())) // 현재시간 기준 판매중인 포인트박스만 검색
          .map(ItemDto::from)
          .toList();
    } else if (itemEntity.equals(CashItemEntity.class)) {
      return cashItemRepository.findAll(pageable).stream()
          .filter(cashItemEntity -> cashItemEntity.getStatus() == ACTIVE)
          .map(ItemDto::from)
          .toList();
    } else {
      throw new WebStoreException(CLASS_NOT_FOUND);
    }
  }

  private List<ItemDto> getContents(Pageable pageable, List<ItemDto> itemDtos) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), itemDtos.size());
    return itemDtos.subList(start, end);
  }

  // getItemDetails
  public ItemDetailsDto.Response getItemDetails(Long itemId, ItemDetailsDto.Request request) {
    ItemEntity itemEntity = selectType(itemId, request.getType());
    itemEntity.checkStatus();
    return itemEntity.toResponse();
  }

  private ItemEntity selectType(Long itemId, ItemType type) {
    if (type == FIXED_POINT_BOX_ITEM || type == RANDOM_POINT_BOX_ITEM) {
      return getItemEntity(itemId, pointBoxItemRepository);
    } else if (type == CASH_ITEM) {
      return getItemEntity(itemId, cashItemRepository);
    } else {
      throw new WebStoreException(ITEM_TYPE_NOT_FOUND);
    }
  }

  private <T extends ItemEntity> T getItemEntity(Long itemId, JpaRepository<T, Long> repository) {
    return repository.findById(itemId)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }
}
