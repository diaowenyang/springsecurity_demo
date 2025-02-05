package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.security.JwtTokenProvider;
import com.example.security.UserPrincipal;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TestControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtTokenProvider jwtTokenProvider;

  @Test
  public void testAccessProtectedEndpoint_WithPermission() throws Exception {
    // 创建一个具有所需权限的测试用户
    UserPrincipal userDetails =
        new UserPrincipal(
            "test@example.com",
            "",
            Collections.singletonList(new SimpleGrantedAuthority("base:bindOrgRole")),
            1L);

    // 生成带权限的 token
    String token = jwtTokenProvider.generateAccessToken(userDetails);
    log.info("token : {}", token);

    // 测试访问受保护的接口
    mockMvc
        .perform(get("/api/test/protected").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  public void testAccessProtectedEndpoint_WithoutPermission() throws Exception {
    // 创建一个没有所需权限的测试用户
    UserPrincipal userDetails =
        new UserPrincipal("test@example.com", "", Collections.emptyList(), 1L);

    // 生成不带权限的 token
    String token = jwtTokenProvider.generateAccessToken(userDetails);

    // 测试访问受保护的接口
    mockMvc
        .perform(get("/api/test/protected").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testAccessProtectedEndpoint_WithoutToken() throws Exception {
    // 测试不带 token 访问受保护的接口
    mockMvc.perform(get("/api/test/protected")).andExpect(status().isUnauthorized());
  }
}
