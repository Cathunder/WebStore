package com.project.WebStore.user.dto;

import com.project.WebStore.user.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements UserDetails {
  private Long id;
  private String email;
  private String password;
  private String nickname;
  private int point;
  private int cash;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static UserDto toUserDto(UserEntity userEntity) {
    return UserDto.builder()
        .id(userEntity.getId())
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .nickname(userEntity.getNickname())
        .point(userEntity.getPoint())
        .cash(userEntity.getCash())
        .createdAt(userEntity.getCreatedAt())
        .updatedAt(userEntity.getUpdatedAt())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
