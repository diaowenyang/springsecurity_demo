package com.example.entity;

import java.util.Set;
import lombok.Data;

/** 用户信息类，包含他拥有的权限信息 */
@Data
public class User {
  private Long id;

  private String email;

  private Set<Permission> permissions;
}
