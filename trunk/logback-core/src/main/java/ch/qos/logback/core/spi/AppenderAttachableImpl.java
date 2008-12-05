/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 2000-2008, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.core.spi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ch.qos.logback.core.Appender;

/**
 * A ReentrantReadWriteLock based implementation of the
 * {@link AppenderAttachable} interface.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class AppenderAttachableImpl<E> implements AppenderAttachable<E> {

  final private List<Appender<E>> appenderList = new ArrayList<Appender<E>>();
  final private ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock r = rwLock.readLock();
  private final Lock w = rwLock.writeLock();

  /**
   * Attach an appender. If the appender is already in the list in won't be
   * added again.
   */
  public void addAppender(Appender<E> newAppender) {
    if (newAppender == null) {
      throw new IllegalArgumentException("Null argument disallowed");
    }
    try {
      w.lock();
      if (!appenderList.contains(newAppender)) {
        appenderList.add(newAppender);
      }
    } finally {
      w.unlock();
    }
  }

  /**
   * Call the <code>doAppend</code> method on all attached appenders.
   */
  public int appendLoopOnAppenders(E e) {
    int size = 0;
    try {
      r.lock();
      for (Appender<E> appender : appenderList) {
        appender.doAppend(e);
        size++;
      }
    } finally {
      r.unlock();
    }
    return size;
  }

  /**
   * Get all attached appenders as an Enumeration. If there are no attached
   * appenders <code>null</code> is returned.
   * 
   * @return Iterator An iterator of attached appenders.
   */
  public Iterator<Appender<E>> iteratorForAppenders() {
    List<Appender<E>> copy;
    try {
      r.lock();
      copy = new ArrayList<Appender<E>>(appenderList);
    } finally {
      r.unlock();
    }
    return copy.iterator();
  }

  /**
   * Look for an attached appender named as <code>name</code>.
   * 
   * <p> Return the appender with that name if in the list. Return null
   * otherwise.
   * 
   */
  public Appender<E> getAppender(String name) {
    if (name == null) {
      return null;
    }
    Appender<E> found = null;

    try {
      r.lock();
      for (Appender<E> appender : appenderList) {
        if (name.equals(appender.getName())) {
          found = appender;
          break;
        }
      }
    } finally {
      r.unlock();
    }
    return found;
  }

  /**
   * Returns <code>true</code> if the specified appender is in the list of
   * attached appenders, <code>false</code> otherwise.
   * 
   * @since 1.2
   */
  public boolean isAttached(Appender appender) {
    if (appender == null) {
      return false;
    }
    boolean attached = false;
    try {
      r.lock();
      for (Appender<E> a : appenderList) {
        if (a == appender) {
          attached = true;
          break;
        }
      }
    } finally {
      r.unlock();
    }
    return attached;
  }

  /**
   * Remove and stop all previously attached appenders.
   */
  public void detachAndStopAllAppenders() {
    try {
      w.lock();
      for (Appender<E> a : appenderList) {
        a.stop();
      }
      appenderList.clear();
    } finally {
      w.unlock();
    }
  }

  /**
   * Remove the appender passed as parameter form the list of attached
   * appenders.
   */
  public boolean detachAppender(Appender appender) {
    if (appender == null) {
      return false;
    }
    boolean result;
    try {
      w.lock();
      result = appenderList.remove(appender);
    } finally {
      w.unlock();
    }
    return result;
  }

  /**
   * Remove the appender with the name passed as parameter form the list of
   * appenders.
   */
  public boolean detachAppender(String name) {
    if (name == null) {
      return false;
    }
    boolean removed = false;
    try {
      w.lock();
      for (Appender<E> a : appenderList) {
        if (name.equals((a).getName())) {
          removed = appenderList.remove(a);
          break;
        }
      }
    } finally {
      w.unlock();
    }
    return removed;
  }
}
