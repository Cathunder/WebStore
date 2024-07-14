package com.project.WebStore.user.service;

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
public class HistoryForUserService {

  private final UserRepository userRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final CashHistoryRepository cashHistoryRepository;

  public Page<PurchaseHistoryDto> findAllPurchaseHistory(UserDetails userDetails, Pageable pageable) {
    Long id = getUserId(userDetails);
    return purchaseHistoryRepository.findAllByUserId(id, pageable)
        .map(PurchaseHistoryDto::from);
  }

  public Page<PointHistoryDto> findAllPointHistory(UserDetails userDetails, Pageable pageable) {
    Long id = getUserId(userDetails);
    return pointHistoryRepository.findAllByUserId(id, pageable)
        .map(PointHistoryDto::from);
  }

  public Page<CashHistoryDto> findAllCashHistory(UserDetails userDetails, Pageable pageable) {
    Long id = getUserId(userDetails);
    return cashHistoryRepository.findAllByUserId(id, pageable)
        .map(CashHistoryDto::from);
  }

  private Long getUserId(UserDetails userDetails) {
    String email = userDetails.getUsername();
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new WebStoreException(USER_NOT_FOUND));
    return userEntity.getId();
  }
}
