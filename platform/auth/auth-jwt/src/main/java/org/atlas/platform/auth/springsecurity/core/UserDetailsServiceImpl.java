package org.atlas.platform.auth.springsecurity.core;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.atlas.service.user.contract.repository.UserRepository;
import org.atlas.service.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(this::map)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format("User %s not found", username)));
  }

  public UserDetailsImpl map(User user) {
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.setUserId(user.getId());
    userDetails.setUsername(user.getUsername());
    userDetails.setPassword(user.getPassword());
    userDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    return userDetails;
  }
}
