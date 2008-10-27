/**
 * Logback: the generic, reliable, fast and flexible logging framework for Java.
 * 
 * Copyright (C) 2000-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.core.joran;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.xml.sax.InputSource;

import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.event.SaxEventRecorder;
import ch.qos.logback.core.joran.spi.EventPlayer;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.joran.spi.SimpleRuleStore;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class GenericConfigurator extends ContextAwareBase {

  protected Interpreter interpreter;

  final public void doConfigure(URL url) throws JoranException {
    try {
      InputStream in = url.openStream();
      doConfigure(in);
      in.close();
    } catch (IOException ioe) {
      String errMsg = "Could not open URL [" + url + "].";
      addError(errMsg, ioe);
      throw new JoranException(errMsg, ioe);
    }
  }

  final public void doConfigure(String filename) throws JoranException {
    doConfigure(new File(filename));
  }

  final public void doConfigure(File file) throws JoranException {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      doConfigure(fis);
    } catch (IOException ioe) {
      String errMsg = "Could not open [" + file.getName() + "].";
      addError(errMsg, ioe);
      throw new JoranException(errMsg, ioe);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (java.io.IOException ioe) {
          String errMsg = "Could not close [" + file.getName() + "].";
          addError(errMsg, ioe);
          throw new JoranException(errMsg, ioe);
        }
      }
    }
  }

  final public void doConfigure(InputStream inputStream) throws JoranException {
    doConfigure(new InputSource(inputStream));
  }

  abstract protected void addInstanceRules(RuleStore rs);

  abstract protected void addImplicitRules(Interpreter interpreter);

  protected void buildInterpreter() {
    RuleStore rs = new SimpleRuleStore(context);
    addInstanceRules(rs);
    this.interpreter = new Interpreter(context, rs);
    InterpretationContext ec = interpreter.getInterpretationContext();
    ec.setContext(context);
    addImplicitRules(interpreter);
  }

  final public void doConfigure(final InputSource inputSource)
      throws JoranException {
    SaxEventRecorder recorder = new SaxEventRecorder();
    recorder.setContext(context);
    recorder.recordEvents(inputSource);
    buildInterpreter();
    // disallow simultaneous configurations of the same context
    synchronized (context) {
      interpreter.play(recorder.saxEventList);
    }
  }

  public void doConfigure(final List<SaxEvent> eventList)
      throws JoranException {
    buildInterpreter();
    EventPlayer player = new EventPlayer(interpreter);
    player.play(eventList);
  }
}
