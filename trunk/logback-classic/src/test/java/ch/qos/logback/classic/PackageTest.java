/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic;

import junit.framework.*;

public class PackageTest extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(LoggerContextTest.class);
    suite.addTest(new JUnit4TestAdapter(LoggerPerfTest.class));
    suite.addTest(new JUnit4TestAdapter(DynamicLoggerContextTest.class));
    suite.addTest(new JUnit4TestAdapter(PatternLayoutTest.class));
    suite.addTestSuite(BasicLoggerTest.class);
    suite.addTestSuite(MessageFormattingTest.class);
    suite.addTestSuite(MDCTest.class);
    suite.addTestSuite(TurboFilteringInLoggerTest.class);
    return suite;
  }
}