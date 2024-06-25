package com.project.WebStore.user.service;

import com.project.WebStore.security.JwtProvider;
import com.project.WebStore.user.dto.LoginUserDto;
import com.project.WebStore.user.dto.RegisterUserDto;
import com.project.WebStore.user.dto.UserDto;
import com.project.WebStore.user.entity.UserEntity;
import com.project.WebStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
  }

  @Transactional
  public UserDto register(RegisterUserDto.Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("동일한 이메일이 존재합니다.");
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

  @Transactional
  public LoginUserDto.Response signIn(LoginUserDto.Request request) {

    UserEntity userEntity = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    String accessToken = jwtProvider.generateAccessToken(userEntity);
    String refreshToken = jwtProvider.generateRefreshToken(userEntity);

    return LoginUserDto.Response.builder()
        .userId(userEntity.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
