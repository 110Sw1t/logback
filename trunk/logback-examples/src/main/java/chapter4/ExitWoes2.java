/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package chapter4;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.WriterAppender;
import ch.qos.logback.core.layout.EchoLayout;
import ch.qos.logback.core.util.StatusPrinter;

public class ExitWoes2 {

  public static void main(String[] args) throws Exception {
    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    lc.reset();//this is to cancel default-config.
    WriterAppender<LoggingEvent> writerAppender = new WriterAppender<LoggingEvent>();
    writerAppender.setContext(lc);
    writerAppender.setLayout(new EchoLayout<LoggingEvent>());

    OutputStream os = new FileOutputStream("exitWoes2.log");
    writerAppender.setWriter(new OutputStreamWriter(os));
    writerAppender.setImmediateFlush(false);
    writerAppender.start();
    Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
    root.addAppender(writerAppender);

    Logger logger = lc.getLogger(ExitWoes2.class);

    logger.debug("Hello world.");
    
    lc.stop();
    
    StatusPrinter.print(lc);
  }
}