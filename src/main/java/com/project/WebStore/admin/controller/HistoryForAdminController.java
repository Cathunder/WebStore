package com.project.WebStore.admin.controller;

import com.project.WebStore.admin.service.HistoryForAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/history")
public class HistoryForAdminController {

  private final HistoryForAdminService historyForAdminService;

  @GetMapping("/purchase/user/{userId}")
  public ResponseEntity<?> findUserPurchaseHistory(
      @PathVariable("userId") Long userId,
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForAdminService.findUserPurchaseHistory(userId, userDetails, pageable));
  }

  @GetMapping("/point/user/{userId}")
  public ResponseEntity<?> findUserPointHistory(
      @PathVariable("userId") Long userId,
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForAdminService.findUserPointHistory(userId, userDetails, pageable));
  }

  @GetMapping("/cash/user/{userId}")
  public ResponseEntity<?> findUserCashHistory(
      @PathVariable("userId") Long userId,
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForAdminService.findUserCashHistory(userId, userDetails, pageable));
  }
}
