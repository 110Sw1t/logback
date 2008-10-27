/** 
 * LOGBack: the reliable, fast and flexible logging library for Java.
 *
 * Copyright (C) 1999-2005, QOS.ch, LOGBack.com
 *
 * This library is free software, you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.
 */
package ch.qos.logback.classic.control;

import java.util.List;

import org.junit.Test;


public class ScenarioMakerTest {

//  public void test1() {
//    Scenario s = ScenarioMaker.makeTypeAScenario(10);
//    List actionList = s.getActionList();
//    for(int i = 0; i < actionList.size(); i++) {
//      System.out.println(actionList.get(i)) ;
//    }
//  }

  @Test
  public void testTypeB() {
      Scenario s = ScenarioMaker.makeTypeBScenario(30);
      List<ControlAction> actionList = s.getActionList();
      for(int i = 0; i < actionList.size(); i++) {
        //System.out.println(actionList.get(i)) ;
      }
    }

}