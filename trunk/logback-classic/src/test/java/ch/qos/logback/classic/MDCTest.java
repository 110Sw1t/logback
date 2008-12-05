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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MDCTest {

  @Test
  public void test() throws InterruptedException {
    MDCTestThread threadA = new MDCTestThread("a");
    threadA.start();

    MDCTestThread threadB = new MDCTestThread("b");
    threadB.start();

    threadA.join();
    threadB.join();

    assertNull(threadA.x0);
    assertEquals("a", threadA.x1);
    assertNull(threadA.x2);

    assertNull(threadB.x0);
    assertEquals("b", threadB.x1);
    assertNull(threadB.x2);

  }

}
