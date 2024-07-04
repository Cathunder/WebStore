package com.project.WebStore.item.contoller;

import com.project.WebStore.item.dto.RegisterCashItemDto;
import com.project.WebStore.item.dto.UpdateCashItemDto;
import com.project.WebStore.item.service.CashItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class CashItemController {

  private final CashItemService cashItemService;

  @PostMapping("/cash")
  public ResponseEntity<?> register(
      @RequestBody @Valid RegisterCashItemDto.Request request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(cashItemService.register(request, userDetails));
  }

  @GetMapping("/cash")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(cashItemService.findAll());
  }

  @PatchMapping("/cash/{id}")
  public ResponseEntity<?> update(
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateCashItemDto.Request request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(cashItemService.update(id, request, userDetails));
  }

  @DeleteMapping("/cash/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    cashItemService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
