package com.project.WebStore.item.contoller;

import static com.project.WebStore.error.ErrorCode.ACCESS_DENIED;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.dto.RegisterPointBoxItemDto;
import com.project.WebStore.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    if (userDetails == null || userDetails.getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
      throw new WebStoreException(ACCESS_DENIED);
    }

    return ResponseEntity.ok(itemService.register(request, userDetails));
  }
}

