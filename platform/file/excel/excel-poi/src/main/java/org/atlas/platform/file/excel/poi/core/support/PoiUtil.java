package org.atlas.platform.file.excel.poi.core.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PoiUtil {

  public static boolean isNotEmptyRow(Row row) {
    for (Cell cell : row) {
      if (cell.getCellType() != CellType.BLANK) {
        return true;
      }
    }
    return false;
  }
}
