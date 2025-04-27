package org.atlas.edge.auth.springsecurityjwt.mapper;

import java.util.Collections;
import lombok.experimental.UtilityClass;
import org.atlas.domain.auth.entity.UserEntity;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@UtilityClass
public class AuthMapper {

  public static UserDetailsImpl map(UserEntity userEntity) {
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.setUserId(userEntity.getUserId());
    userDetails.setUsername(userEntity.getUsername());
    userDetails.setPassword(userEntity.getPassword());
    userDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().name())));
    return userDetails;
  }
}
