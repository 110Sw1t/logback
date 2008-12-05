package ch.qos.logback.classic.pattern;

import junit.framework.TestCase;

import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;

public class MarkerConverterTest extends TestCase {
  
  LoggerContext lc;
  MarkerConverter converter;
  // use a different facotry for each test so that they are independent
  IMarkerFactory markerFactory = new BasicMarkerFactory();
  
  public void setUp() throws Exception {
    lc = new LoggerContext();
    converter = new MarkerConverter();
    converter.start();
  }
  
  public void tearDown() throws Exception {
    lc = null;
    converter.stop();
    converter = null;
  }

  public void testWithNullMarker() {
    String result = converter.convert(createLoggingEvent(null));
    assertEquals("", result);
  }
  
  public void testWithMarker() {
    String name = "test";
    Marker marker = markerFactory.getMarker(name);
    String result = converter.convert(createLoggingEvent(marker));
    assertEquals(name, result);
  }
  
  public void testWithOneChildMarker() {
    Marker marker = markerFactory.getMarker("test");
    marker.add(markerFactory.getMarker("child"));
    
    String result = converter.convert(createLoggingEvent(marker));
    
    assertEquals("test [ child ]", result);
  }
  
  public void testWithSeveralChildMarker() {
    Marker marker = markerFactory.getMarker("testParent");
    marker.add(markerFactory.getMarker("child1"));
    marker.add(markerFactory.getMarker("child2"));
    marker.add(markerFactory.getMarker("child3"));
    
    String result = converter.convert(createLoggingEvent(marker));
    
    assertEquals("testParent [ child1, child2, child3 ]", result);
  }
  
  private LoggingEvent createLoggingEvent(Marker marker) {
    LoggingEvent le = new LoggingEvent(this.getClass().getName(), lc.getLogger(LoggerContext.ROOT_NAME),
        Level.DEBUG, "test message", null, null);
    le.setMarker(marker);
    return le;
  }
}
