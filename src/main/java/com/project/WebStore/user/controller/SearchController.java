package com.project.WebStore.user.controller;

import com.project.WebStore.user.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{id}")
  public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
    return ResponseEntity.ok(searchService.findOne(id));
  }
}
