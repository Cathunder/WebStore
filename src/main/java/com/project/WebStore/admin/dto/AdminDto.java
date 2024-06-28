package com.project.WebStore.admin.dto;

import com.project.WebStore.admin.entity.AdminEntity;
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
public class AdminDto implements UserDetails {
  private Long id;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static AdminDto toAdminDto(AdminEntity adminEntity) {
    return AdminDto.builder()
        .id(adminEntity.getId())
        .email(adminEntity.getEmail())
        .password(adminEntity.getPassword())
        .createdAt(adminEntity.getCreatedAt())
        .updatedAt(adminEntity.getUpdatedAt())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
