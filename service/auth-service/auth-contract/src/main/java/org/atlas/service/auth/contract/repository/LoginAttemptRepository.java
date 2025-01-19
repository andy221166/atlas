package org.atlas.service.auth.contract.repository;

import java.util.Optional;
import org.atlas.service.auth.domain.LoginAttempt;

public interface LoginAttemptRepository {

  Optional<LoginAttempt> findLastLoginAttempt(String username);

  int insert(LoginAttempt loginAttempt);
}
