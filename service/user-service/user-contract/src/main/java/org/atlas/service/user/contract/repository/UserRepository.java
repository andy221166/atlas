package org.atlas.service.user.contract.repository;

import java.util.List;
import java.util.Optional;
import org.atlas.service.user.domain.User;

public interface UserRepository {

  List<User> findByIdIn(List<Integer> ids);

  Optional<User> findById(Integer id);

  int insert(User user);

  int insertAll(List<User> users);

  int update(User user);

  int updateAll(List<User> users);

  int delete(Integer id);

  int deleteAll(List<Integer> ids);
}
