package com.project.WebStore.admin.dto;

import com.project.WebStore.admin.entity.AdminEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDto {
  private Long id;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static AdminDto fromEntity(AdminEntity adminEntity) {
    return AdminDto.builder()
        .id(adminEntity.getId())
        .email(adminEntity.getEmail())
        .password(adminEntity.getPassword())
        .createdAt(adminEntity.getCreatedAt())
        .updatedAt(adminEntity.getUpdatedAt())
        .build();
  }
}
