package com.example.controller;

import com.example.constant.Common;
import com.example.security.JwtTokenProvider;
import com.example.security.UserPrincipal;
import com.example.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  @Operation(summary = "用户登录接口", description = "用户登录，返回accessToken和refreshToken")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // todo 这里可以补充 校验用户名+密码（或者邮箱+邮件验证码、短信+短信验证码等等）的逻辑
    UserPrincipal userDetails =
        (UserPrincipal) userService.loadUserByUsername(loginRequest.email());
    return ResponseEntity.ok(getJwtResponse(userDetails));
  }

  @Operation(summary = "刷新token接口", description = "用户通过refreshToken获取新的accessToken")
  @GetMapping("/refresh")
  public ResponseEntity<?> loginByRefreshToken(@RequestParam String refreshToken) {
    Claims claims = jwtTokenProvider.extractClaims(refreshToken);
    String tokenType = claims.get(Common.TYPE, String.class);

    // 如果不是 refresh token，拒绝访问资源
    if (!Common.REFRESH_TOKEN.equals(tokenType)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
    String email = claims.getSubject();
    UserPrincipal userPrincipal = (UserPrincipal) userService.loadUserByUsername(email);
    JwtResponse response = getJwtResponse(userPrincipal);
    return ResponseEntity.ok(response);
  }

  private JwtResponse getJwtResponse(UserPrincipal userDetails) {
    String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
    return new JwtResponse(accessToken, refreshToken);
  }

  record LoginRequest(String email, String code) {}

  record JwtResponse(String accessToken, String refreshToken) {}
}
