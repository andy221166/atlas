package org.atlas.service.user.contract.repository;

import java.util.List;
import org.atlas.service.user.domain.Organization;

public interface OrganizationRepository {

  List<Organization> findAll();
}
