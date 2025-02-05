package com.example.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/** 创建一个自定义的 Authentication 对象，这样可以在上下文中保存更多用户信息 */
public class UserPrincipal extends User {

  // 多保存一个userId，可以扩展更多信息，比如用户的性别、职级、手机号等
  private final Long userId;

  public UserPrincipal(
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      Long userId) {
    super(username, password, authorities);
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
