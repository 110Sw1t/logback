/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic.pattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;


public class DateConverter extends ClassicConverter {
  
  long lastTimestamp = -1;
  String timesmapStr = null;
  SimpleDateFormat simpleFormat = null;
  
  public void start() {
    
    String datePattern = getFirstOption();
    if(datePattern == null) {
      datePattern = CoreConstants.ISO8601_PATTERN;
    }
    
    if (datePattern.equals(CoreConstants.ISO8601_STR)) {
      datePattern = CoreConstants.ISO8601_PATTERN;
    } 
    
    try {
      simpleFormat = new SimpleDateFormat(datePattern);
      //maximumCacheValidity = CachedDateFormat.getMaximumCacheValidity(pattern);
    } catch (IllegalArgumentException e) {
      addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);
      // default to the ISO8601 format
      simpleFormat = new SimpleDateFormat(CoreConstants.ISO8601_PATTERN);
    }
    
    List optionList = getOptionList();
    
    // if the option list contains a TZ option, then set it.
    if (optionList != null && optionList.size() > 1) {
      TimeZone tz = TimeZone.getTimeZone((String) optionList.get(1));
      simpleFormat.setTimeZone(tz);
    }
  }
  
  public String convert(LoggingEvent le) {
    long timestamp = le.getTimeStamp();
    
    // if called multiple times within the same millisecond
    // return old value
    if(timestamp == lastTimestamp) {
      return timesmapStr;
    } else {
      lastTimestamp = timestamp;
      // SimpleDateFormat is not thread safe. However, since
      // the AppenderBase.doAppend is synchronized, we are should be
      // OK. See also http://jira.qos.ch/browse/LBCLASSIC-36
      timesmapStr = simpleFormat.format(new Date(timestamp));
      return timesmapStr;
    }
  }
}
