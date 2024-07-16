package com.project.WebStore.admin.entity;

import com.project.WebStore.admin.dto.RegisterAdminDto;
import com.project.WebStore.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity(name = "admin")
public class AdminEntity extends BaseEntity {

  @Column(unique = true)
  private String email;
  private String password;

  public static AdminEntity createEntity(RegisterAdminDto.Request request, PasswordEncoder passwordEncoder) {
    return AdminEntity.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
  }
}
