package ch.qos.logback.access.pattern;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.core.util.OptionHelper;


public class RequestHeaderConverter extends AccessConverter {

  String key;

  public void start() {
    key = getFirstOption();
    if (OptionHelper.isEmpty(key)) {
      addWarn("Missing key for the requested header. Defaulting to all keys.");
      key = null;
    } 
    super.start();
  }

  public String convert(AccessEvent accessEvent) {
    if(!isStarted()) {
      return "INACTIVE_HEADER_CONV";
    }
    
    if(key != null) {
      return accessEvent.getRequestHeader(key);
    } else {
      return accessEvent.getRequestHeaderMap().toString();
    }
  }

}
