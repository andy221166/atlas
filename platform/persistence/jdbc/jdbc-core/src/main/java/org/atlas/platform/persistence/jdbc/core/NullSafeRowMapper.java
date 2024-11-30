package org.atlas.platform.persistence.jdbc.core;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class NullSafeRowMapper {

  private final ResultSet rs;

  public NullSafeRowMapper(ResultSet rs) {
    this.rs = rs;
  }

  public Integer getInt(String columnLabel) {
    try {
      int value = rs.getInt(columnLabel);
      return rs.wasNull() ? null : value;
    } catch (SQLException e) {
      return null;
    }
  }

  public Long getLong(String columnLabel) {
    try {
      long value = rs.getLong(columnLabel);
      return rs.wasNull() ? null : value;
    } catch (SQLException e) {
      return null;
    }
  }

  public String getString(String columnLabel) {
    try {
      String value = rs.getString(columnLabel);
      return rs.wasNull() ? null : value;
    } catch (SQLException e) {
      return null;
    }
  }

  public Boolean getBoolean(String columnLabel) {
    try {
      boolean value = rs.getBoolean(columnLabel);
      return rs.wasNull() ? null : value;
    } catch (SQLException e) {
      return null;
    }
  }

  public BigDecimal getBigDecimal(String columnLabel) {
    try {
      return rs.getBigDecimal(columnLabel);
    } catch (SQLException e) {
      return null;
    }
  }

  public Timestamp getTimestamp(String columnLabel) {
    try {
      return rs.getTimestamp(columnLabel);
    } catch (SQLException e) {
      return null;
    }
  }
}
