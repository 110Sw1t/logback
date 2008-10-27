/**
 * LOGBack: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.joran.action.ext;

import org.xml.sax.Attributes;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;



public class IncAction extends Action {

  static public int  beginCount;
  static public int  endCount;
  static public int  errorCount;

  static public void reset() {
    beginCount = 0;
    endCount = 0;
    errorCount = 0;
  }
  /**
   * Instantiates an layout of the given class and sets its name.
   *
   */
  public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
    //System.out.println("IncAction Begin called");
    beginCount++;
    String val = attributes.getValue("increment");
    if(!"1".equals(val)) {
      errorCount++;
      throw new ActionException();
    }
  }

  /**
   * Once the children elements are also parsed, now is the time to activate
   * the appender options.
   */
  public void end(InterpretationContext ec, String name) {
    endCount++;
  }
}
