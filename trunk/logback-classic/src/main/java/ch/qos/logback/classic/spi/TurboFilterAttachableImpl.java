/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic.spi;

import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Implementation of ClassicFilterAttachable.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
final public class TurboFilterAttachableImpl implements TurboFilterAttachable {

  TurboFilter headFilter;
  TurboFilter tailFilter;

  /**
   * Add a filter to end of the filter list.
   */
  public void addTurboFilter(TurboFilter newFilter) {
    if (headFilter == null) {
      headFilter = newFilter;
      tailFilter = newFilter;
    } else {
      tailFilter.setNext(newFilter);
      tailFilter = newFilter;
    }
  }

  /**
   * Get first filter in the chain.
   */
  public TurboFilter getFirstTurboFilter() {
    return headFilter;
  }

  /**
   * Clear the filter chain
   */
  public void clearAllTurboFilters() {
    TurboFilter f = headFilter;
    while (f != null) {
      final TurboFilter next = f.getNext();
      f.setNext(null);
      f = next;
    }
    f = null;
    headFilter = null;
    tailFilter = null;
  }

  /**
   * Loop through the filters in the chain. As soon as a filter decides on 
   * ACCEPT or DENY, then that value is returned. If all of the filters return
   * NEUTRAL, then  NEUTRAL is returned.
   */
  public final FilterReply getTurboFilterChainDecision(final Marker marker, final Logger logger,
      final Level level, final String format, final Object[] params, final Throwable t) {
    TurboFilter f = headFilter;

    
    while (f != null) {
      switch (f.decide(marker, logger,  level,  format, params,  t)) {
      case DENY:
        return FilterReply.DENY;

      case ACCEPT:
        return FilterReply.ACCEPT;

      case NEUTRAL:
        f = f.getNext();
      }
    }
    return FilterReply.NEUTRAL;
  }
}
