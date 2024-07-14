package com.project.WebStore.user.controller;

import com.project.WebStore.user.service.HistoryService;
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
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping("/purchase")
  public ResponseEntity<?> findAllPurchaseHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyService.findAllPurchaseHistory(userDetails, pageable));
  }

  @GetMapping("/point")
  public ResponseEntity<?> findAllPointHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyService.findAllPointHistory(userDetails, pageable));
  }

  @GetMapping("/cash")
  public ResponseEntity<?> findAllCashHistory(
      @AuthenticationPrincipal UserDetails userDetails,
      Pageable pageable) {
    return ResponseEntity.ok(historyService.findAllCashHistory(userDetails, pageable));
  }
}
