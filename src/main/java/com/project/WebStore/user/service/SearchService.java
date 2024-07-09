package com.project.WebStore.user.service;

import static com.project.WebStore.common.type.ItemStatus.ACTIVE;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.CashItemRepository;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.dto.ItemDetailsDto;
import com.project.WebStore.user.dto.ItemDto;
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

  // findAll
  public Page<Object> findAll(Pageable pageable) {
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
        .filter(pointBoxItemEntity -> pointBoxItemEntity.getStatus() == ACTIVE)
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

  // findOne
  public ItemDetailsDto.Response findOne(Long itemId, ItemDetailsDto.Request request) {
    switch (request.getType()) {
      case FIXED_POINT_BOX_ITEM:
      case RANDOM_POINT_BOX_ITEM:
        return ItemDetailsDto.Response.from(getPointBoxItemEntity(itemId, request));
      case CASH_ITEM:
        return ItemDetailsDto.Response.from(getCashItemEntity(itemId));
      default:
        throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }

  private PointBoxItemEntity getPointBoxItemEntity(Long itemId, ItemDetailsDto.Request request) {
    PointBoxItemEntity pointBoxItemEntity = pointBoxItemRepository.findById(itemId)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));

    if (pointBoxItemEntity.getStatus() == ACTIVE
        && pointBoxItemEntity.getType() == request.getType()) {
      return pointBoxItemEntity;
    } else {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }

  private CashItemEntity getCashItemEntity(Long itemId) {
    CashItemEntity cashItemEntity = cashItemRepository.findById(itemId)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));

    if (cashItemEntity.getStatus() == ACTIVE) {
      return cashItemEntity;
    } else {
      throw new WebStoreException(ITEM_NOT_FOUND);
    }
  }
}
