package com.project.WebStore.item.service;

import static com.project.WebStore.error.ErrorCode.ACCESS_DENIED;
import static com.project.WebStore.error.ErrorCode.DUPLICATED_NAME;
import static com.project.WebStore.error.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.SELLING_ITEM_CANNOT_UPDATE;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.PointBoxItemDto;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import java.time.LocalDateTime;
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
public class PointBoxItemService {

  private final PointBoxItemRepository pointBoxItemRepository;
  private final AdminRepository adminRepository;

  @Transactional
  public RegisterPointBoxItemDto.Response register(RegisterPointBoxItemDto.Request request, UserDetails userDetails) {
    AdminEntity adminEntity = checkEmail(userDetails);
    checkItemName(request);

    PointBoxItemEntity pointBoxItemEntity = PointBoxItemEntity.create(request, adminEntity);
    pointBoxItemRepository.save(pointBoxItemEntity);

    return RegisterPointBoxItemDto.Response.from(pointBoxItemEntity);
  }

  public List<PointBoxItemDto> findAll() {
    List<PointBoxItemEntity> pointBoxItemEntities = pointBoxItemRepository.findAll();
    return pointBoxItemEntities.stream()
        .map(PointBoxItemDto::from)
        .toList();
  }

  @Transactional
  public UpdatePointBoxItemDto.Response update(Long id, UpdatePointBoxItemDto.Request request,
      UserDetails userDetails) {
    AdminEntity adminEntity = checkEmail(userDetails);
    PointBoxItemEntity pointBoxItemEntity = checkItem(id);
    checkSameAdmin(adminEntity, pointBoxItemEntity);
    checkStartedAtBeforeNow(pointBoxItemEntity);

    pointBoxItemEntity.updateEntity(request);
    pointBoxItemRepository.save(pointBoxItemEntity);

    return UpdatePointBoxItemDto.Response.from(pointBoxItemEntity);
  }

  @Transactional
  public void delete(Long id) {
    PointBoxItemEntity pointBoxItemEntity = checkItem(id);
    pointBoxItemEntity.changeStatus();
  }

  private AdminEntity checkEmail(UserDetails userDetails) {
    String email = userDetails.getUsername();
    return adminRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));
  }

  private void checkItemName(RegisterPointBoxItemDto.Request request) {
    if (pointBoxItemRepository.existsByName(request.getName())) {
      throw new WebStoreException(DUPLICATED_NAME);
    }
  }

  private PointBoxItemEntity checkItem(Long id) {
    return pointBoxItemRepository.findById(id)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private void checkSameAdmin(AdminEntity adminEntity, PointBoxItemEntity pointBoxItemEntity) {
    if (!adminEntity.getId().equals(pointBoxItemEntity.getAdminEntity().getId())) {
      throw new WebStoreException(ACCESS_DENIED);
    }
  }

  private void checkStartedAtBeforeNow(PointBoxItemEntity pointBoxItemEntity) {
    if (pointBoxItemEntity.getStartedAt().isBefore(LocalDateTime.now())) {
      throw new WebStoreException(SELLING_ITEM_CANNOT_UPDATE);
    }
  }
}
