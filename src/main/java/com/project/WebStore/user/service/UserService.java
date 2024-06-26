package com.project.WebStore.user.service;

import static com.project.WebStore.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.project.WebStore.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.exception.ErrorCode.PASSWORD_INCORRECT;

import com.project.WebStore.exception.WebStoreException;
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
      throw new WebStoreException(DUPLICATED_EMAIL);
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
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
      throw new WebStoreException(PASSWORD_INCORRECT);
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
