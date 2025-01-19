package org.atlas.service.user.contract.file.excel;

import java.util.List;
import org.atlas.service.user.domain.User;

public interface UserExcelWriter {

  byte[] write(List<User> users, String sheetName) throws Exception;
}
