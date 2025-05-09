package org.atlas.edge.auth.springsecurityjwt.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import org.atlas.domain.auth.entity.UserEntity;
import org.atlas.domain.user.shared.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserDetailsImpl implements UserDetails {

  private Integer userId;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private String sessionId;

  public UserDetailsImpl(UserEntity userEntity) {
    this.userId = userEntity.getUserId();
    this.username = userEntity.getUsername();
    this.password = userEntity.getPassword();
    this.authorities = Collections.singletonList(
        new SimpleGrantedAuthority(userEntity.getRole().name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Set<Role> getRoles() {
    return authorities.stream()
        .map(authority -> Role.valueOf(authority.getAuthority()))
        .collect(Collectors.toSet());
  }
}
