package com.project.WebStore.item.contoller;

import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.dto.UpdatePointBoxItemDto;
import com.project.WebStore.item.service.ItemService;
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
public class ItemController {

  private final ItemService itemService;

  @PostMapping("/point-box")
  public ResponseEntity<?> register(
      @RequestBody @Valid RegisterPointBoxItemDto.Request request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(itemService.register(request, userDetails));
  }

  @GetMapping("/point-box")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(itemService.findAll());
  }

  @PatchMapping("/point-box/{id}")
  public ResponseEntity<?> update(
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdatePointBoxItemDto.Request request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(itemService.update(id, request, userDetails));
  }

  @DeleteMapping("/point-box/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    itemService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

