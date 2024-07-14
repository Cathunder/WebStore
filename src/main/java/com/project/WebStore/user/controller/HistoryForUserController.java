package com.project.WebStore.user.controller;

import com.project.WebStore.user.service.HistoryForUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/history")
public class HistoryForUserController {

  private final HistoryForUserService historyForUserService;

  @GetMapping("/purchase")
  public ResponseEntity<?> findAllPurchaseHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForUserService.findAllPurchaseHistory(userDetails, pageable));
  }

  @GetMapping("/point")
  public ResponseEntity<?> findAllPointHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForUserService.findAllPointHistory(userDetails, pageable));
  }

  @GetMapping("/cash")
  public ResponseEntity<?> findAllCashHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyForUserService.findAllCashHistory(userDetails, pageable));
  }
}
