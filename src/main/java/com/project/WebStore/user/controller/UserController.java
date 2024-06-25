package com.project.WebStore.user.controller;

import com.project.WebStore.user.dto.RegisterUserDto;
import com.project.WebStore.user.dto.UserDto;
import com.project.WebStore.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto.Request request) {
    UserDto userDto = userService.register(request);
    return ResponseEntity.ok(RegisterUserDto.Response.fromDto(userDto));
  }
}
