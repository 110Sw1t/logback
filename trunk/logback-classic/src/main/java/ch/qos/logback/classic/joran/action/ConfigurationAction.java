/**
 * LOGBack: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.classic.joran.action;

import org.xml.sax.Attributes;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.StatusPrinter;



public class ConfigurationAction extends Action {
  static final String INTERNAL_DEBUG_ATTR = "debug";
  boolean debugMode = false;

  public void begin(InterpretationContext ec, String name, Attributes attributes) {
    String debugAttrib = attributes.getValue(INTERNAL_DEBUG_ATTR);

    if (
      (debugAttrib == null) || debugAttrib.equals("")
        || debugAttrib.equals("false") || debugAttrib.equals("null")) {
      addInfo(INTERNAL_DEBUG_ATTR + " attribute not set");
    } else {
      //LoggerContext loggerContext = (LoggerContext) context;
      //ConfiguratorBase.attachTemporaryConsoleAppender(context);
 
      debugMode = true;
    }
    
    // the context is turbo filter attachable, so it is pushed on top of the stack
    ec.pushObject(getContext());
  }

  public void end(InterpretationContext ec, String name) {
    if (debugMode) {
      addInfo("End of configuration.");
      LoggerContext loggerContext = (LoggerContext) context;
      StatusPrinter.print(loggerContext);
      
      //LoggerContext loggerContext = (LoggerContext) context;
      //ConfiguratorBase.detachTemporaryConsoleAppender(repository, errorList);
    }
    ec.popObject();
  }
}
