package com.project.WebStore.admin.service;

import static com.project.WebStore.error.ErrorCode.ACCESS_DENIED;
import static com.project.WebStore.error.ErrorCode.USER_NOT_FOUND;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.user.dto.CashHistoryDto;
import com.project.WebStore.user.dto.PointHistoryDto;
import com.project.WebStore.user.dto.PurchaseHistoryDto;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.CashHistoryRepository;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryForAdminService {

  private final UserRepository userRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final CashHistoryRepository cashHistoryRepository;

  public Page<PurchaseHistoryDto> findUserPurchaseHistory(Long userId, UserDetails userDetails, Pageable pageable) {
    checkIsAdmin(userDetails);
    UserEntity userEntity = getUserEntity(userId);
    return purchaseHistoryRepository.findAllByUserId(userEntity.getId(), pageable).map(PurchaseHistoryDto::from);
  }

  public Page<PointHistoryDto> findUserPointHistory(Long userId, UserDetails userDetails, Pageable pageable) {
    checkIsAdmin(userDetails);
    UserEntity userEntity = getUserEntity(userId);
    return pointHistoryRepository.findAllByUserId(userEntity.getId(), pageable).map(PointHistoryDto::from);
  }

  public Page<CashHistoryDto> findUserCashHistory(Long userId, UserDetails userDetails, Pageable pageable) {
    checkIsAdmin(userDetails);
    UserEntity userEntity = getUserEntity(userId);
    return cashHistoryRepository.findAllByUserId(userEntity.getId(), pageable).map(CashHistoryDto::from);
  }

  private void checkIsAdmin(UserDetails userDetails) {
    boolean roleAdmin = userDetails.getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    if (!roleAdmin) {
      throw new WebStoreException(ACCESS_DENIED);
    }
  }

  private UserEntity getUserEntity(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new WebStoreException(USER_NOT_FOUND));
  }
}
