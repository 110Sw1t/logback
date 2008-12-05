/**
 * LOGBack: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2007, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.joran.action;



import org.xml.sax.Attributes;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.OptionHelper;

abstract public class AbstractLayoutAction<E> extends Action {
  Layout<E> layout;
  boolean inError = false;

  /**
   * Instantiates an layout of the given class and sets its name.
   *
   */
  @SuppressWarnings("unchecked")
  public void begin(InterpretationContext ec, String name, Attributes attributes) {
    // Let us forget about previous errors (in this object)
    inError = false;

    String className = attributes.getValue(CLASS_ATTRIBUTE);
    try {
      layout = (Layout<E>)
        OptionHelper.instantiateByClassName(
          className, ch.qos.logback.core.Layout.class, context);
      
        layout.setContext(this.context);
        //getLogger().debug("Pushing layout on top of the object stack.");
        ec.pushObject(layout);        
    } catch (Exception oops) {
      inError = true;
      addError("Could not create layout of type " + className + "].", oops);
    }
  }

  /**
   * Is the layout of the desired type?
   * @param layout
   * @return true if the layout is of the correct type
   */
  //abstract protected boolean isOfCorrectType(Layout layout);
  
  /**
   * Once the children elements are also parsed, now is the time to activate
   * the appender options.
   */
  @SuppressWarnings("unchecked")
  public void end(InterpretationContext ec, String e) {
    if (inError) {
      return;
    }

    if (layout instanceof LifeCycle) {
      ((LifeCycle) layout).start();
    }

    Object o = ec.peekObject();

    if (o != layout) {
      addWarn(
        "The object on the top the of the stack is not the layout pushed earlier.");
    } else {
      ec.popObject();

      try {
        //getLogger().debug(
        //  "About to set the layout of the containing appender.");
        Appender<E> appender = (Appender<E>) ec.peekObject();
        appender.setLayout(layout);
      } catch (Exception ex) {
        addError(
          "Could not set the layout for containing appender.", ex);
      }
    }
  }

  public void finish(InterpretationContext ec) {
  }
}
