package com.project.WebStore.admin.service;

import static com.project.WebStore.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.project.WebStore.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.exception.ErrorCode.PASSWORD_INCORRECT;

import com.project.WebStore.admin.dto.AdminDto;
import com.project.WebStore.admin.dto.LoginAdminDto;
import com.project.WebStore.admin.dto.RegisterAdminDto;
import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
import com.project.WebStore.exception.WebStoreException;
import com.project.WebStore.security.JwtProvider;
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
public class AdminService implements UserDetailsService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return adminRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
  }

  @Transactional
  public AdminDto register(RegisterAdminDto.Request request) {
    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new WebStoreException(DUPLICATED_EMAIL);
    }

    AdminEntity adminEntity = AdminEntity.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    return AdminDto.fromEntity(adminRepository.save(adminEntity));
  }

  @Transactional
  public LoginAdminDto.Response signIn(LoginAdminDto.Request request) {

    AdminEntity adminEntity = adminRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), adminEntity.getPassword())) {
      throw new WebStoreException(PASSWORD_INCORRECT);
    }

    String accessToken = jwtProvider.generateAccessToken(adminEntity);
    String refreshToken = jwtProvider.generateRefreshToken(adminEntity);

    return LoginAdminDto.Response.builder()
        .adminId(adminEntity.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
