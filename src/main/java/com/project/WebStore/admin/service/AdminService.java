package com.project.WebStore.admin.service;

import static com.project.WebStore.error.ErrorCode.DUPLICATED_EMAIL;
import static com.project.WebStore.error.ErrorCode.EMAIL_NOT_FOUND;
import static com.project.WebStore.error.ErrorCode.PASSWORD_INCORRECT;

import com.project.WebStore.admin.dto.AdminDto;
import com.project.WebStore.admin.dto.LoginAdminDto;
import com.project.WebStore.admin.dto.RegisterAdminDto;
import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
import com.project.WebStore.error.exception.WebStoreException;
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
    AdminEntity adminEntity = adminRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
    return AdminDto.toAdminDto(adminEntity);
  }

  @Transactional
  public RegisterAdminDto.Response register(RegisterAdminDto.Request request) {
    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new WebStoreException(DUPLICATED_EMAIL);
    }

    AdminEntity adminEntity = AdminEntity.createEntity(request, passwordEncoder);
    adminRepository.save(adminEntity);

    return RegisterAdminDto.Response.builder()
        .adminId(adminEntity.getId())
        .email(adminEntity.getEmail())
        .password(adminEntity.getPassword())
        .createdAt(adminEntity.getCreatedAt())
        .build();
  }

  @Transactional
  public LoginAdminDto.Response signIn(LoginAdminDto.Request request) {

    AdminEntity adminEntity = adminRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new WebStoreException(EMAIL_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), adminEntity.getPassword())) {
      throw new WebStoreException(PASSWORD_INCORRECT);
    }

    AdminDto adminDto = AdminDto.toAdminDto(adminEntity);
    String accessToken = jwtProvider.generateAccessToken(adminDto);
    String refreshToken = jwtProvider.generateRefreshToken(adminDto);

    return LoginAdminDto.Response.builder()
        .adminId(adminEntity.getId())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
