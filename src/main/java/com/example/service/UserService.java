package com.example.service;

import com.example.entity.Permission;
import com.example.entity.User;
import com.example.security.UserPrincipal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    // todo 模拟从数据库获取到用户信息
    User user = new User();
    user.setId(1L);
    user.setEmail("wen@gmail.com");

    // todo 模拟从数据库获取权限信息
    Permission p1 = new Permission(1L, "aa", "aa desc");

    Permission p2 = new Permission(2L, "bb", "bb desc");

    user.setPermissions(Set.of(p1, p2));

    // 将用户的权限转换为 GrantedAuthority 集合
    Set<SimpleGrantedAuthority> authorities =
        user.getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toSet());

    UserPrincipal userPrincipal =
        new UserPrincipal(
            user.getEmail(),
            "", // 空密码，因为我们用验证码登录
            authorities,
            user.getId());

    // 返回自定义的 Authentication 对象
    return userPrincipal;
  }
}
