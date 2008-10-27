/**
 * Logback: the reliable, fast and flexible logging library for Java.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package chapter3.implicit;

import org.xml.sax.Attributes;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.InterpretationContext;



/**
 * No operation (NOP) action that does strictly nothing. 
 *  
 * @author Ceki G&uuml;lc&uuml;
 */
public class NOPAction extends Action {
  
  public void begin(InterpretationContext ec, String name, Attributes attributes) {
  }


  public void end(InterpretationContext ec, String name) {
  }
}
