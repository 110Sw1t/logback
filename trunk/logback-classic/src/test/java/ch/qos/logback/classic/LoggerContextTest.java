/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 2000-2008, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.core.status.StatusManager;

public class LoggerContextTest {
  LoggerContext lc;

  @Before
  public void setUp() throws Exception {
    Logger.instanceCount = 0;
    lc = new LoggerContext();
    lc.setName("x");
  }

  @Test
  public void testRootGetLogger() {
    Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
    assertEquals(Level.DEBUG, root.getLevel());
    assertEquals(Level.DEBUG, root.getEffectiveLevel());
  }

  @Test
  public void testLoggerX() {
    Logger x = lc.getLogger("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertNull(x.getLevel());
    assertEquals(Level.DEBUG, x.getEffectiveLevel());
  }

  @Test
  public void testNull() {
    try {
      lc.getLogger((String) null);
      fail("null should cause an exception");
    } catch (IllegalArgumentException e) {
    }
  }

  @Test
  public void testEmpty() {
    Logger empty = lc.getLogger("");
    LoggerTestHelper.assertNameEquals(empty, "");
    LoggerTestHelper.assertLevels(null, empty, Level.DEBUG);

    Logger dot = lc.getLogger(".");
    LoggerTestHelper.assertNameEquals(dot, ".");
    // LoggerTestHelper.assertNameEquals(dot.parent, "");
    // LoggerTestHelper.assertNameEquals(dot.parent.parent, "root");

    // assertNull(dot.parent.parent.parent);
    LoggerTestHelper.assertLevels(null, dot, Level.DEBUG);

    assertEquals(3, Logger.instanceCount);
  }

  @Test
  public void testDotDot() {
    Logger dotdot = lc.getLogger("..");
    assertEquals(4, Logger.instanceCount);
    LoggerTestHelper.assertNameEquals(dotdot, "..");
    // LoggerTestHelper.assertNameEquals(dotdot.parent, ".");
    // LoggerTestHelper.assertNameEquals(dotdot.parent.parent, "");
    // LoggerTestHelper.assertNameEquals(dotdot.parent.parent.parent, "root");
  }

  @Test
  public void testLoggerXY() {
    assertEquals(1, Logger.instanceCount);

    Logger xy = lc.getLogger("x.y");
    assertEquals(3, Logger.instanceCount);
    LoggerTestHelper.assertNameEquals(xy, "x.y");
    LoggerTestHelper.assertLevels(null, xy, Level.DEBUG);

    Logger x = lc.getLogger("x");
    assertEquals(3, Logger.instanceCount);

    Logger xy2 = lc.getLogger("x.y");
    assertEquals(xy, xy2);

    Logger x2 = lc.getLogger("x");
    assertEquals(x, x2);
    assertEquals(3, Logger.instanceCount);
  }

  @Test
  public void testLoggerMultipleChildren() {
    assertEquals(1, Logger.instanceCount);
    Logger xy0 = lc.getLogger("x.y0");
    LoggerTestHelper.assertNameEquals(xy0, "x.y0");

    Logger xy1 = lc.getLogger("x.y1");
    LoggerTestHelper.assertNameEquals(xy1, "x.y1");

    LoggerTestHelper.assertLevels(null, xy0, Level.DEBUG);
    LoggerTestHelper.assertLevels(null, xy1, Level.DEBUG);
    assertEquals(4, Logger.instanceCount);

    for (int i = 0; i < 100; i++) {
      Logger xy_i = lc.getLogger("x.y" + i);
      LoggerTestHelper.assertNameEquals(xy_i, "x.y" + i);
      LoggerTestHelper.assertLevels(null, xy_i, Level.DEBUG);
    }
    assertEquals(102, Logger.instanceCount);
  }

  @Test
  public void testMultiLevel() {
    Logger wxyz = lc.getLogger("w.x.y.z");
    LoggerTestHelper.assertNameEquals(wxyz, "w.x.y.z");
    LoggerTestHelper.assertLevels(null, wxyz, Level.DEBUG);

    Logger wx = lc.getLogger("w.x");
    wx.setLevel(Level.INFO);
    LoggerTestHelper.assertNameEquals(wx, "w.x");
    LoggerTestHelper.assertLevels(Level.INFO, wx, Level.INFO);
    LoggerTestHelper.assertLevels(null, lc.getLogger("w.x.y"), Level.INFO);
    LoggerTestHelper.assertLevels(null, wxyz, Level.INFO);
  }

  @Test
  public void testStatusWithUnconfiguredContext() {
    Logger logger = lc.getLogger(LoggerContextTest.class);

    for (int i = 0; i < 3; i++) {
      logger.debug("test");
    }

    logger = lc.getLogger("x.y.z");

    for (int i = 0; i < 3; i++) {
      logger.debug("test");
    }

    StatusManager sm = lc.getStatusManager();
    assertTrue("StatusManager has recieved too many messages",
        sm.getCount() == 1);
  }

  
  @Test
  public void resetTest() {

    Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
    Logger a = lc.getLogger("a");
    Logger ab = lc.getLogger("a.b");

    ab.setLevel(Level.WARN);
    root.setLevel(Level.INFO);
    lc.reset();
    assertEquals(Level.DEBUG, root.getEffectiveLevel());
    assertEquals(Level.DEBUG, a.getEffectiveLevel());
    assertEquals(Level.DEBUG, ab.getEffectiveLevel());
    
    assertEquals(Level.DEBUG, root.getLevel());    
    assertNull(a.getLevel());
    assertNull(ab.getLevel());

  }
}