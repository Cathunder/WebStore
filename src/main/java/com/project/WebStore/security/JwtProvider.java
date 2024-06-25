package com.project.WebStore.security;

import com.project.WebStore.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final SecretKey secretKey;

  @Value("${jwt.access-token-exp-time}")
  private long accessTokenExpTime;

  @Value("${jwt.refresh-token-exp-time}")
  private long refreshTokenExpTime;

  public JwtProvider(@Value("${jwt.secret-key}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
  }

  public String generateAccessToken(UserEntity userEntity) {
    return createToken(userEntity, accessTokenExpTime);
  }

  public String generateRefreshToken(UserEntity userEntity) {
    return createToken(userEntity, refreshTokenExpTime);
  }

  public String createToken(UserEntity userEntity, long expTime) {
    Date now = new Date();
    Date expDate = new Date(now.getTime() + expTime);

    return Jwts.builder()
        .setSubject(userEntity.getUsername())
        .claim("role", "ROLE_USER")
        .setIssuedAt(now)
        .setExpiration(expDate)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  public boolean validateToken(String token) {
    Claims claims = parseClaims(token);

    if (claims != null) {
      return !claims.getExpiration().before(new Date());
    }
    return false;
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      return null;
    }
  }

  public String getEmail(String token) {
    Claims claims = parseClaims(token);
    return claims != null ? claims.get("email", String.class) : null;
  }

  public List<String> getRoles(String token) {
    Claims claims = parseClaims(token);
    return claims != null ? claims.get("role", List.class) : Collections.emptyList();
  }
}
