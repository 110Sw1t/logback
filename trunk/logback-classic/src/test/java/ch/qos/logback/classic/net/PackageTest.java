/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic.net;


import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PackageTest extends TestCase {
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new JUnit4TestAdapter(SyslogAppenderTest.class));
    suite.addTest(new JUnit4TestAdapter(DilutedSMTPAppenderTest.class));
    suite.addTest(new JUnit4TestAdapter(SocketAppenderTest.class));
    suite.addTestSuite(JMSTopicAppenderTest.class);
    return suite;
  }
}