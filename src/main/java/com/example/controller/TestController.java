package com.example.controller;

import com.example.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Slf4j
@AllArgsConstructor
public class TestController {

  private final JsonUtil jsonUtil;

  @GetMapping("/protected/uri1")
  @PreAuthorize("hasAuthority('aa')")
  public ResponseEntity<?> protectedResource1() {
    return ResponseEntity.ok("测试是否有权访问");
  }

  @GetMapping("/protected/uri2")
  @PreAuthorize("hasAuthority('cc')")
  public ResponseEntity<?> protectedResource2() {
    return ResponseEntity.ok("测试是否有权访问");
  }
}
