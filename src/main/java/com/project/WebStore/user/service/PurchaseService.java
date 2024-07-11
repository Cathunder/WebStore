package com.project.WebStore.user.service;

import com.project.WebStore.item.repository.CashItemRepository;
import com.project.WebStore.item.repository.PointBoxItemRepository;
import com.project.WebStore.user.repository.CashHistoryRepository;
import com.project.WebStore.user.repository.PointHistoryRepository;
import com.project.WebStore.user.repository.PurchaseHistoryRepository;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {

  private final UserRepository userRepository;
  private final PointBoxItemRepository pointBoxItemRepository;
  private final CashItemRepository cashItemRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final CashHistoryRepository cashHistoryRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
}
