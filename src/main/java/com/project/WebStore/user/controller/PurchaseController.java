package com.project.WebStore.user.controller;

import com.project.WebStore.user.dto.PurchaseDto;
import com.project.WebStore.user.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/items")
public class PurchaseController {

  private final PurchaseService purchaseService;

  @PostMapping("/purchase/{id}")
  public ResponseEntity<?> purchaseItem(
      @PathVariable("id") Long id,
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestBody @Valid PurchaseDto.Request request) {
    return ResponseEntity.ok(purchaseService.purchase(id, userDetails, request));
  }
}
