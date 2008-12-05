/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 2000-2008, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.core.rolling;

import java.io.File;
import java.io.IOException;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;

/**
 * <code>RollingFileAppender</code> extends {@link FileAppender} to backup the
 * log files depending on {@link RollingPolicy} and {@link TriggeringPolicy}.
 * <p>
 * 
 * For more information about this appender, please refer to the online manual
 * at http://logback.qos.ch/manual/appenders.html#RollingFileAppender
 * 
 * @author Heinz Richter
 * @author Ceki G&uuml;lc&uuml;
 */
public class RollingFileAppender<E> extends FileAppender<E> {
  File activeFileCache;
  TriggeringPolicy<E> triggeringPolicy;
  RollingPolicy rollingPolicy;

  /**
   * The default constructor simply calls its {@link FileAppender#FileAppender
   * parents constructor}.
   */
  public RollingFileAppender() {
  }

  public void start() {
    if (triggeringPolicy == null) {
      addWarn("No TriggeringPolicy was set for the RollingFileAppender named "
          + getName());
      addWarn("For more information, please visit http://logback.qos.ch/codes.html#rfa_no_tp");
      return;
    }

    if (rollingPolicy == null) {
      addError("No RollingPolicy was set for the RollingFileAppender named "
          + getName());
      addError("For more information, please visit http://logback.qos.ch/codes.html#rfa_no_rp");
      return;
    }
    
    if(isPrudent()) {
      if(rawFileProperty() !=  null) {
        addWarn("Setting \"File\" property to null on account of prudent mode");
        setFile(null);    
      }
      if(rollingPolicy.getCompressionMode() != CompressionMode.NONE) {
        addError("Compression is not supported in prudent mode. Aborting");
        return;
      }
    }

    activeFileCache = new File(getFile());
    addInfo("Active log file name: " + getFile());
    super.start();
  }

  @Override
  public String getFile() {
    return rollingPolicy.getActiveFileName();
  }

  /**
   * Implements the usual roll over behaviour.
   * 
   * <p>If <code>MaxBackupIndex</code> is positive, then files {<code>File.1</code>,
   * ..., <code>File.MaxBackupIndex -1</code>} are renamed to {<code>File.2</code>,
   * ..., <code>File.MaxBackupIndex</code>}. Moreover, <code>File</code> is
   * renamed <code>File.1</code> and closed. A new <code>File</code> is
   * created to receive further log output.
   * 
   * <p>If <code>MaxBackupIndex</code> is equal to zero, then the
   * <code>File</code> is truncated with no backup files created.
   * 
   */
  public void rollover() {
    // Note: synchronization at this point is unnecessary as the doAppend
    // is already synched

    //
    // make sure to close the hereto active log file! Renaming under windows
    // does not work for open files.
    this.closeWriter();

    // By default, the newly created file will be created in truncate mode.
    // (See the setFile() call a few lines below.)
    // FIXME don't change the append mode
    // this.append = false;
    
    try {
      rollingPolicy.rollover();
    } catch (RolloverFailure rf) {
      addWarn("RolloverFailure occurred. Deferring roll-over.");
      // we failed to roll-over, let us not truncate and risk data loss
      this.append = true;
    }

    try {
      // This will also close the file. This is OK since multiple
      // close operations are safe.
      this.openFile(rollingPolicy.getActiveFileName());
    } catch (IOException e) {
      addError("setFile(" + fileName + ", false) call failed.", e);
    }
  }

  /**
   * This method differentiates RollingFileAppender from its super class.
   */
  protected void subAppend(E event) {
    // The roll-over check must precede actual writing. This is the
    // only correct behavior for time driven triggers.
    if (triggeringPolicy.isTriggeringEvent(activeFileCache, event)) {
      rollover();
    }

    super.subAppend(event);
  }

  public RollingPolicy getRollingPolicy() {
    return rollingPolicy;
  }

  public TriggeringPolicy getTriggeringPolicy() {
    return triggeringPolicy;
  }

  /**
   * Sets the rolling policy. In case the 'policy' argument also implements
   * {@link TriggeringPolicy}, then the triggering policy for this appender is
   * automatically set to be the policy argument.
   * 
   * @param policy
   */
  @SuppressWarnings("unchecked")
  public void setRollingPolicy(RollingPolicy policy) {
    rollingPolicy = policy;
    if (rollingPolicy instanceof TriggeringPolicy) {
      triggeringPolicy = (TriggeringPolicy<E>) policy;
    }

  }

  public void setTriggeringPolicy(TriggeringPolicy<E> policy) {
    triggeringPolicy = policy;
    if (policy instanceof RollingPolicy) {
      rollingPolicy = (RollingPolicy) policy;
    }
  }
}
