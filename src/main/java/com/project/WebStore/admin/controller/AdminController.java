package com.project.WebStore.admin.controller;

import com.project.WebStore.admin.dto.AdminDto;
import com.project.WebStore.admin.dto.LoginAdminDto;
import com.project.WebStore.admin.dto.RegisterAdminDto;
import com.project.WebStore.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterAdminDto.Request request) {
    AdminDto adminDto = adminService.register(request);
    return ResponseEntity.ok(RegisterAdminDto.Response.fromDto(adminDto));
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(@RequestBody @Valid LoginAdminDto.Request request) {
    return ResponseEntity.ok(adminService.signIn(request));
  }
}
