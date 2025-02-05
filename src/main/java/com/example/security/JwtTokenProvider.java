package com.example.security;

import com.example.constant.Common;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-validity}")
  private long accessTokenValidity;

  @Value("${jwt.refresh-token-validity}")
  private long refreshTokenValidity;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  /** 生成 accessToken */
  public String generateAccessToken(UserPrincipal userDetails) {
    return generateToken(userDetails, accessTokenValidity, Common.ACCESS_TOKEN);
  }

  /** 生成 refreshToken */
  public String generateRefreshToken(UserPrincipal userDetails) {
    return generateToken(userDetails, refreshTokenValidity, Common.REFRESH_TOKEN);
  }

  private String generateToken(UserPrincipal userDetails, long validity, String type) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(Common.TYPE, type);
    claims.put(Common.USER_ID, userDetails.getUserId());
    if (Common.ACCESS_TOKEN.equals(type)) {
      // refresh_token 不需要给权限
      claims.put("authorities", userDetails.getAuthorities());
    }

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
        .signWith(getSigningKey())
        .compact();
  }

  /** 根据token获取用户登录名 */
  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public Claims extractClaims(String token) {
    return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
  }
}
