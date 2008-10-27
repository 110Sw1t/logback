/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.db;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.xml.sax.Attributes;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.PropertySetter;
import ch.qos.logback.core.util.OptionHelper;

/**
 * 
 * @author Ceki Gulcu
 *
 */
public class BindDataSourceToJNDIAction extends Action {
  
  static final String DATA_SOURCE_CLASS = "dataSourceClass";
  static final String URL = "url";
  static final String USER = "user";
  static final String PASSWORD = "password";

  /**
   * Instantiates an a data source and bind it to JNDI
   * Most of the required parameters are placed in the ec.substitutionProperties
   */
  public void begin(
      InterpretationContext ec, String localName, Attributes attributes) {
    String dsClassName = ec.getSubstitutionProperty(DATA_SOURCE_CLASS);

    if (OptionHelper.isEmpty(dsClassName)) {
      addWarn("dsClassName is a required parameter");
      ec.addError("dsClassName is a required parameter");

      return;
    }

    String urlStr = ec.getSubstitutionProperty(URL);
    String userStr = ec.getSubstitutionProperty(USER);
    String passwordStr = ec.getSubstitutionProperty(PASSWORD);

    try {
      DataSource ds =
        (DataSource) OptionHelper.instantiateByClassName(dsClassName, DataSource.class, context);

      PropertySetter setter = new PropertySetter(ds);
      setter.setContext(context);

      if (!OptionHelper.isEmpty(urlStr)) {
        setter.setProperty("url", urlStr);
      }

      if (!OptionHelper.isEmpty(userStr)) {
        setter.setProperty("user", userStr);
      }

      if (!OptionHelper.isEmpty(passwordStr)) {
        setter.setProperty("password", passwordStr);
      }

      Context ctx = new InitialContext();
      ctx.rebind("dataSource", ds);
    } catch (Exception oops) {
      addError(
        "Could not bind  datasource. Reported error follows.", oops);
      ec.addError("Could not not bind  datasource of type [" + dsClassName + "].");
    }
  }

  public void end(InterpretationContext ec, String name) {
  }
}
