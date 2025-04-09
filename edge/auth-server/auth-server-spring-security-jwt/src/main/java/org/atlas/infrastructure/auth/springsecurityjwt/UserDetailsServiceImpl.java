package org.atlas.infrastructure.auth.springsecurityjwt;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.domain.user.repository.UserRepository;
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

  public UserDetailsImpl map(UserEntity userEntity) {
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.setUserId(userEntity.getId());
    userDetails.setUsername(userEntity.getUsername());
    userDetails.setPassword(userEntity.getPassword());
    userDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().name())));
    return userDetails;
  }
}
