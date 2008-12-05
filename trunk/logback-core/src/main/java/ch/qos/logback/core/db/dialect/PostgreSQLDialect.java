/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.db.dialect;


/**
 * 
 * @author ceki
 *
 */
public class PostgreSQLDialect
       implements SQLDialect {
  public static final String SELECT_CURRVAL = "SELECT currval('logging_event_id_seq')";

  public String getSelectInsertId() {
    return SELECT_CURRVAL;
  }
}
