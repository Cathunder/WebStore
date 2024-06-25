package com.project.WebStore.user.dto;

import com.project.WebStore.user.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private Long id;
  private String email;
  private String password;
  private String nickname;
  private int point;
  private int cash;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static UserDto fromEntity(UserEntity userEntity) {
    return UserDto.builder()
        .id(userEntity.getId())
        .email(userEntity.getEmail())
        .password(userEntity.getPassword())
        .nickname(userEntity.getNickname())
        .point(userEntity.getPoint())
        .cash(userEntity.getCash())
        .createdAt(userEntity.getCreatedAt())
        .build();
  }
}
