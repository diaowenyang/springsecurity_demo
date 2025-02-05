package com.example.util;

import com.example.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

/** 工具类，根据上下文获取用户信息 */
public class SecurityUtils {

  public static UserPrincipal getCurrentUser() {
    return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static Long getCurrentUserId() {
    return getCurrentUser().getUserId();
  }
}
