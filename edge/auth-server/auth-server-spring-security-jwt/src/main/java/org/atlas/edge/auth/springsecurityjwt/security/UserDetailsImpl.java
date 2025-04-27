package org.atlas.edge.auth.springsecurityjwt.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.atlas.domain.user.shared.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

@Data
public class UserDetailsImpl implements UserDetails {

  private Integer userId;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private String sessionId;

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

  public Role getRole() {
    List<String> roles = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    if (CollectionUtils.isEmpty(roles)) {
      return null;
    }
    return Role.valueOf(roles.get(0));
  }
}
