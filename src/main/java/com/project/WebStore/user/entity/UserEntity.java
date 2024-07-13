package com.project.WebStore.user.entity;

import static com.project.WebStore.error.ErrorCode.NOT_ENOUGH_POINT;

import com.project.WebStore.common.entity.BaseEntity;
import com.project.WebStore.error.exception.WebStoreException;
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

  public void decreasePoint(int requiredPoint) {
    if (this.point <= 0 || this.point < requiredPoint) {
      throw new WebStoreException(NOT_ENOUGH_POINT);
    }
    this.point -= requiredPoint;
  }

  public void increasePoint(int earnPoint) {
    this.point += earnPoint;
  }

  public void increaseCash(int earnCash) {
    this.cash += earnCash;
  }
}
