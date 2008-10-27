package ch.qos.logback.access.filter;

import ch.qos.logback.core.util.TimeUtil;
import junit.framework.TestCase;

public class StatsByDayTest extends TestCase {

  public StatsByDayTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
 public void testBasic() {
   // Tue Nov 21 18:05:36 CET 2006
    long now = 1164128736369L;
    StatsByDay statsByDay = new StatsByDay(now);
    
    int total = 0;
    // test fresh start
    statsByDay.update(now, 0);
    assertEquals(0, statsByDay.getLastCount());
    assertEquals(0, statsByDay.getAverage(), 0.01);

  
    total++;
    statsByDay.update(now, total);
    assertEquals(0, statsByDay.getLastCount());
    assertEquals(0.0, statsByDay.getAverage(), 0.01);

    long nextDay0 = TimeUtil.computeStartOfNextDay(now);
    nextDay0 += 99;
    
    // there should be one event the next day, avg should also be 1
    statsByDay.update(nextDay0, total);
    assertEquals(1.0, statsByDay.getLastCount(), 0.01);
    assertEquals(1.0, statsByDay.getAverage(), 0.01);

    total += 2;
    
    statsByDay.update(nextDay0, total);
    assertEquals(1, statsByDay.getLastCount());
    assertEquals(1.0, statsByDay.getAverage(), 0.01);

    long nextDay1 = TimeUtil.computeStartOfNextDay(nextDay0) + 6747;
    statsByDay.update(nextDay1, total);
    assertEquals(2, statsByDay.getLastCount());
    assertEquals(1.5, statsByDay.getAverage(), 0.01);

    nextDay1 += 4444;
    total += 4;
    
    statsByDay.update(nextDay1, total);
    // values should remain unchanged
    assertEquals(2, statsByDay.getLastCount());
    assertEquals(1.5, statsByDay.getAverage(), 0.01);

    
    long nextDay2 = TimeUtil.computeStartOfNextDay(nextDay1) + 11177;

    statsByDay.update(nextDay2, total);
    // values should remain unchanged
    assertEquals(4, statsByDay.getLastCount());
    assertEquals(7.0/3, statsByDay.getAverage(), 0.01);
  }

}
