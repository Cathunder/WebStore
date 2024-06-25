package com.project.WebStore.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegisterUserDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Request {

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "이메일 형식에 맞지 않습니다.") // RFC 5322
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요.")
    @Size(max = 50, message = "닉네임은 최대 50자 이하이어야 합니다.")
    private String nickname;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Response {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private int point;
    private int cash;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static Response fromDto(UserDto userDto) {
      return Response.builder()
          .id(userDto.getId())
          .email(userDto.getEmail())
          .password(userDto.getPassword())
          .nickname(userDto.getNickname())
          .point(userDto.getPoint())
          .cash(userDto.getCash())
          .createdAt(userDto.getCreatedAt())
          .build();
    }
  }
}
