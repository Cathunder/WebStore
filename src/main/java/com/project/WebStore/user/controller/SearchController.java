package com.project.WebStore.user.controller;

import com.project.WebStore.user.dto.ItemDetailsDto;
import com.project.WebStore.user.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/items")
public class SearchController {

  private final SearchService searchService;

  @GetMapping("")
  public ResponseEntity<?> findAll(Pageable pageable) {
    return ResponseEntity.ok(searchService.findAll(pageable));
  }

  @GetMapping("/details")
  public ResponseEntity<?> findOne(@RequestBody @Valid ItemDetailsDto.Request request) {
    return ResponseEntity.ok(searchService.findOne(request));
  }
}
