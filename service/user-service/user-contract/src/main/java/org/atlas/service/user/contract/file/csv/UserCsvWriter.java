package org.atlas.service.user.contract.file.csv;

import java.util.List;
import org.atlas.service.user.domain.User;

public interface UserCsvWriter {

  byte[] write(List<User> users) throws Exception;
}
