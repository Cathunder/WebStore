package com.project.WebStore.user.service;

import com.project.WebStore.user.dto.RegisterUserDto;
import com.project.WebStore.user.dto.UserDto;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserDto register(RegisterUserDto.Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("동일한 email이 존재합니다.");
    }

    UserEntity userEntity = UserEntity.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .point(1000)
        .cash(0)
        .build();

    return UserDto.fromEntity(userRepository.save(userEntity));
  }
}
