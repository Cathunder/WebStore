package com.project.WebStore.item.service;

import static com.project.WebStore.common.type.PointBoxItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.PointBoxItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.DUPLICATED_NAME;
import static com.project.WebStore.error.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.POINT_BOX_ITEM_TYPE_NOT_EXIST;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.PointBoxItemDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.entity.FixedPointEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.entity.RandomPointEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final AdminRepository adminRepository;

  @Transactional
  public RegisterPointBoxItemDto.Response register(RegisterPointBoxItemDto.Request request, UserDetails userDetails) {

    String email = userDetails.getUsername();
    AdminEntity adminEntity = adminRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));

    if (pointBoxItemRepository.existsByName(request.getName())) {
      throw new WebStoreException(DUPLICATED_NAME);
    }

    PointBoxItemEntity pointBoxItemEntity = PointBoxItemEntity.of(request, adminEntity);
    savePointsAndBox(request, pointBoxItemEntity);

    return RegisterPointBoxItemDto.Response.from(pointBoxItemEntity);
  }

  @Transactional
  protected void savePointsAndBox(RegisterPointBoxItemDto.Request request, PointBoxItemEntity pointBoxItemEntity) {
    if (FIXED_POINT_BOX_ITEM.equals(request.getType())) {
      List<FixedPointEntity> fixedPointEntities = request.getFixedPoints().stream()
          .map(fixedPoint -> FixedPointEntity.builder()
              .pointBoxItemEntity(pointBoxItemEntity)
              .pointAmount(fixedPoint.getPointAmount())
              .probability(fixedPoint.getProbability())
              .build())
          .toList();

      pointBoxItemEntity.addFixedPointEntities(fixedPointEntities);

    } else if (RANDOM_POINT_BOX_ITEM.equals(request.getType())) {
      List<RandomPointEntity> randomPointEntities = request.getRandomPoints().stream()
          .map(randomPoint -> RandomPointEntity.builder()
              .pointBoxItemEntity(pointBoxItemEntity)
              .minPoint(randomPoint.getMinPoint())
              .maxPoint(randomPoint.getMaxPoint())
              .build())
          .toList();

      pointBoxItemEntity.addRandomPointEntities(randomPointEntities);

    } else {
      throw new WebStoreException(POINT_BOX_ITEM_TYPE_NOT_EXIST);
    }

    pointBoxItemRepository.save(pointBoxItemEntity);
  }

  public List<PointBoxItemDto> findAll() {
    List<PointBoxItemEntity> pointBoxItemEntities = pointBoxItemRepository.findAll();
    return pointBoxItemEntities.stream()
        .map(PointBoxItemDto::from)
        .toList();
  }
}
