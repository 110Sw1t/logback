/**
 * LOGBack: the reliable, fast and flexible logging library for Java.
 *
 * Copyright (C) 1999-2006, QOS.ch
 *
 * This library is free software, you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.
 */
package ch.qos.logback.core.status;

import java.util.List;

/**
 * Internal error messages (statii) are managed by instances of this interface.
 * 
 * @author Ceki Gulcu
 */
public interface StatusManager {

  /**
   * Add a new status message.
   * 
   * @param status
   */
  public void add(Status status);

  /**
   * Obtain a copy of the status list maintained by this StatusManager.
   * 
   * @return
   */
  public List<Status> getCopyOfStatusList();

  /**
   * Return the highest level of all the statii.
   * 
   * @return
   */
  public int getLevel();

  /**
   * Return the number of status entries.
   * 
   * @return
   */
  public int getCount();

  public void add(StatusListener listener);

  public void remove(StatusListener listener);

  /**
   * Obtain a copy of the status listener list maintained by this StatusManager
   * 
   * @return
   */
  public List<StatusListener> getCopyOfStatusListenerList();

}
