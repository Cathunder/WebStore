package com.project.WebStore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.WebStore.error.ErrorCode;
import com.project.WebStore.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    ErrorResponse errorResponse = ErrorResponse.of(
        errorCode.getStatus().value(),
        errorCode.getMessage()
    );

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
