package org.atlas.service.user.contract.repository;

import java.util.List;
import org.atlas.service.user.domain.Group;

public interface GroupRepository {

  List<Group> findAll();
}
