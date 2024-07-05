package com.project.WebStore.item.service;

import static com.project.WebStore.error.ErrorCode.ACCESS_DENIED;
import static com.project.WebStore.error.ErrorCode.DUPLICATED_NAME;
import static com.project.WebStore.error.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.ITEM_NOT_FOUND;

import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.CashItemDto;
import com.project.WebStore.item.dto.RegisterCashItemDto;
import com.project.WebStore.item.dto.UpdateCashItemDto;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.repository.CashItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashItemService {

  private final CashItemRepository cashItemRepository;
  private final AdminRepository adminRepository;

  @Transactional
  public RegisterCashItemDto.Response register(RegisterCashItemDto.Request request, UserDetails userDetails) {
    AdminEntity adminEntity = getAdmin(userDetails);
    checkItemName(request);

    CashItemEntity cashItemEntity = CashItemEntity.create(request, adminEntity);
    cashItemRepository.save(cashItemEntity);

    return RegisterCashItemDto.Response.from(cashItemEntity);
  }

  public Page<CashItemDto> findAll(Pageable pageable) {
    Page<CashItemEntity> page = cashItemRepository.findAll(pageable);
    return page.map(CashItemDto::from);
  }

  @Transactional
  public UpdateCashItemDto.Response update(Long id, UpdateCashItemDto.Request request, UserDetails userDetails) {
    AdminEntity adminEntity = getAdmin(userDetails);
    CashItemEntity cashItemEntity = getItem(id);
    checkSameAdmin(adminEntity, cashItemEntity);

    cashItemEntity.updateEntity(request);
    cashItemRepository.save(cashItemEntity);

    return UpdateCashItemDto.Response.from(cashItemEntity);
  }

  @Transactional
  public void delete(Long id, UserDetails userDetails) {
    AdminEntity adminEntity = getAdmin(userDetails);
    CashItemEntity cashItemEntity = getItem(id);
    checkSameAdmin(adminEntity, cashItemEntity);
    cashItemEntity.changeStatusToInactive();
  }

  private AdminEntity getAdmin(UserDetails userDetails) {
    String email = userDetails.getUsername();
    return adminRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));
  }

  private void checkItemName(RegisterCashItemDto.Request request) {
    if (cashItemRepository.existsByName(request.getName())) {
      throw new WebStoreException(DUPLICATED_NAME);
    }
  }

  private CashItemEntity getItem(Long id) {
    return cashItemRepository.findById(id)
        .orElseThrow(() -> new WebStoreException(ITEM_NOT_FOUND));
  }

  private void checkSameAdmin(AdminEntity adminEntity, CashItemEntity cashItemEntity) {
    if (!adminEntity.getId().equals(cashItemEntity.getAdminEntity().getId())) {
      throw new WebStoreException(ACCESS_DENIED);
    }
  }
}
