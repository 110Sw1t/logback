/**
 * LOGBack: the reliable, fast and flexible logging library for Java.
 *
 * Copyright (C) 1999-2006, QOS.ch
 *
 * This library is free software, you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.
 */
package ch.qos.logback.classic;

import static ch.qos.logback.classic.TestConstants.ISO_REGEX;
import static ch.qos.logback.classic.TestConstants.MAIN_REGEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.pattern.ConverterTest;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.parser.AbstractPatternLayoutBaseTest;

public class PatternLayoutTest extends AbstractPatternLayoutBaseTest {

  
  private PatternLayout pl = new PatternLayout();
  private LoggerContext lc = new LoggerContext();
  Logger logger = lc.getLogger(ConverterTest.class);
  LoggingEvent le;
  List optionList = new ArrayList();

  public PatternLayoutTest() {
    super();
    Exception ex = new Exception("Bogus exception");
    le = makeLoggingEvent(ex);
  }
 
  @Before
  public void setUp() {
    pl.setContext(lc);
  }

  LoggingEvent makeLoggingEvent(Exception ex) {
    return new LoggingEvent(ch.qos.logback.core.pattern.FormattingConverter.class
        .getName(), logger, Level.INFO, "Some message", ex, null);
  }

  @Override
  public LoggingEvent getEventObject() {
    return makeLoggingEvent(null);
  }
  
  public PatternLayoutBase getPatternLayoutBase() {
    return new PatternLayout();
  }

  @Test
  public void testOK() {
    pl.setPattern("%d %le [%t] %lo{30} - %m%n");
    pl.start();
    String val = pl.doLayout(getEventObject());
    // 2006-02-01 22:38:06,212 INFO [main] c.q.l.pattern.ConverterTest - Some message
    String regex = ISO_REGEX+" INFO "+MAIN_REGEX+" c.q.l.c.pattern.ConverterTest - Some message\\s*";

    assertTrue(val.matches(regex));
  }

  @Test
  public void testNoExeptionHandler() {
    pl.setPattern("%m%n");
    pl.start();
    String val = pl.doLayout(le);
    assertTrue(val.contains("java.lang.Exception: Bogus exception"));
  }

  @Test
  public void testCompositePattern() {
    pl.setPattern("%-56(%d %lo{20}) - %m%n");
    pl.start();
    String val = pl.doLayout(getEventObject());
    // 2008-03-18 21:55:54,250 c.q.l.c.pattern.ConverterTest          - Some message
    String regex = ISO_REGEX+" c.q.l.c.p.ConverterTest          - Some message\\s*";
    assertTrue(val.matches(regex));

  }
  
  @Test
  public void testNopExeptionHandler() {
    pl.setPattern("%nopex %m%n");
    pl.start();
    String val = pl.doLayout(le);
    assertTrue(!val.contains("java.lang.Exception: Bogus exception"));
  }
  
  @Test
  public void testWithParenthesis() {
    pl.setPattern("\\(%msg:%msg\\) %msg");
    pl.start();
    le = makeLoggingEvent(null);
    String val = pl.doLayout(le);
    //System.out.println("VAL == " + val);
    assertEquals("(Some message:Some message) Some message", val);
  }
  
  @Test
  public void testWithLettersComingFromLog4j() {
    //Letters: p = level and c = logger
    pl.setPattern("%d %p [%t] %c{30} - %m%n");
    pl.start();
    String val = pl.doLayout(getEventObject());
    // 2006-02-01 22:38:06,212 INFO [main] c.q.l.pattern.ConverterTest - Some message
    String regex = TestConstants.ISO_REGEX+" INFO "+MAIN_REGEX+" c.q.l.c.pattern.ConverterTest - Some message\\s*";
    assertTrue(val.matches(regex));
  }

  @Test
  public void contextNameTest() {
    pl.setPattern("%contextName");
    lc.setName("aValue");
    pl.start();
    String val = pl.doLayout(getEventObject());
    assertEquals("aValue", val);
  }
  
  @Override
  public Context getContext() {
    return lc;
  }
}
