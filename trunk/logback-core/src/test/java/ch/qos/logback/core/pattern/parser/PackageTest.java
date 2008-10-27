/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.pattern.parser;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PackageTest extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(TokenStreamTest.class);
    suite.addTestSuite(OptionTokenizerTest.class);
    suite.addTestSuite(ParserTest.class);
    suite.addTestSuite(FormatInfoTest.class);
    suite.addTestSuite(CompilerTest.class);
    suite.addTest(new JUnit4TestAdapter(SamplePatternLayoutTest.class));
    return suite;
  }
}
