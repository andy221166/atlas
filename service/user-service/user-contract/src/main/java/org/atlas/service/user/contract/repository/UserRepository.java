package org.atlas.service.user.contract.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.service.user.domain.User;

public interface UserRepository {

  List<User> findByIdIn(List<Integer> ids);

  Optional<User> findById(Integer id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  void insert(User user);
}
