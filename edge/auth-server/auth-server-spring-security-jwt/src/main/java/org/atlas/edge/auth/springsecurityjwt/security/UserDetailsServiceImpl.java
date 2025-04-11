package org.atlas.edge.auth.springsecurityjwt.security;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.auth.entity.AuthUserEntity;
import org.atlas.domain.auth.repository.AuthUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AuthUserRepository authUserRepository;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    return authUserRepository.findByIdentifier(identifier)
        .map(this::map)
        .orElseThrow(() -> new UsernameNotFoundException(
            String.format("User with identifier %s not found", identifier)));
  }

  public UserDetailsImpl map(AuthUserEntity authUserEntity) {
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.setUserId(authUserEntity.getUserId());
    userDetails.setUsername(authUserEntity.getUsername());
    userDetails.setPassword(authUserEntity.getPassword());
    userDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(authUserEntity.getRole().name())));
    return userDetails;
  }
}
