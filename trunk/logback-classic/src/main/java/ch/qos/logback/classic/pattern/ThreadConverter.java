package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * Return the events thread (usually the current thread).
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class ThreadConverter extends ClassicConverter {

  public String convert(LoggingEvent event) {
    return event.getThreadName();
  }

}
