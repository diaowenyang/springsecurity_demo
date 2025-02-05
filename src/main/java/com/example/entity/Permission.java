package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 模拟权限类 */
@Data
@AllArgsConstructor
public class Permission {
  private Long id;

  private String name;

  private String expression;
}
