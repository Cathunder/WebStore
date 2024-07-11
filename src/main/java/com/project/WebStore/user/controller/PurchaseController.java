package com.project.WebStore.user.controller;

import com.project.WebStore.user.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/items")
public class PurchaseController {

  private final PurchaseService purchaseService;

}
