package com.project.WebStore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException
  {
    String token = extractTokenFromRequest(request);

    if (token != null && jwtProvider.validateToken(token)) {
      String email = jwtProvider.getEmail(token);
      List<String> roles = jwtProvider.getRoles(token);

      Collection<GrantedAuthority> authorities = roles.stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(email, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(TOKEN_HEADER);
    if (token != null && token.startsWith(TOKEN_PREFIX)) {
      return token.substring(7);
    }

    return null;
  }
}
