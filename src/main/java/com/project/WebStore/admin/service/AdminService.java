package com.project.WebStore.admin.service;

import com.project.WebStore.admin.dto.AdminDto;
import com.project.WebStore.admin.dto.LoginAdminDto;
import com.project.WebStore.admin.dto.RegisterAdminDto;
import com.project.WebStore.admin.entity.AdminEntity;
import com.project.WebStore.admin.repository.AdminRepository;
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
      throw new RuntimeException("동일한 이메일이 존재합니다.");
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
        .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(request.getPassword(), adminEntity.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
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
