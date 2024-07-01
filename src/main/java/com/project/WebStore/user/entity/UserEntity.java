package com.project.WebStore.user.entity;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.user.dto.RegisterUserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class UserEntity extends BaseEntity {

  @Column(unique = true)
  private String email;

  private String password;
  private String nickname;
  private int point;
  private int cash;

  public static UserEntity createUserEntity(RegisterUserDto.Request request, PasswordEncoder passwordEncoder) {
    return UserEntity.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .point(1000)
        .cash(0)
        .build();
  }
}
