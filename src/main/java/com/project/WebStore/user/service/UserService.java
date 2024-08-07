package com.project.WebStore.user.service;

import static com.project.WebStore.error.ErrorCode.DUPLICATED_EMAIL;
import static com.project.WebStore.error.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.PASSWORD_INCORRECT;

import com.project.WebStore.error.exception.WebStoreException;
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
    UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
    return UserDto.toUserDto(userEntity);
  }

  @Transactional
  public RegisterUserDto.Response register(RegisterUserDto.Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new WebStoreException(DUPLICATED_EMAIL);
    }

    UserEntity userEntity = UserEntity.createUserEntity(request, passwordEncoder);
    userRepository.save(userEntity);

    return RegisterUserDto.Response.builder()
        .userId(userEntity.getId())
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .nickname(userEntity.getNickname())
        .point(userEntity.getPoint())
        .cash(userEntity.getCash())
        .createdAt(userEntity.getCreatedAt())
        .build();
  }

  @Transactional
  public LoginUserDto.Response signIn(LoginUserDto.Request request) {

    UserEntity userEntity = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
      throw new WebStoreException(PASSWORD_INCORRECT);
    }

    UserDto userDto = UserDto.toUserDto(userEntity);
    String accessToken = jwtProvider.generateAccessToken(userDto);
    String refreshToken = jwtProvider.generateRefreshToken(userDto);

    return LoginUserDto.Response.builder()
        .userId(userEntity.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
