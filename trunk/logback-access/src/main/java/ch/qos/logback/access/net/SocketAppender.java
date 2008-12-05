/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

// Contributors: Dan MacDonald <dan@redknee.com>
package ch.qos.logback.access.net;

import java.net.InetAddress;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.core.net.SocketAppenderBase;

/**
 * Sends {@link AccessEvent} objects to a remote a log server, usually a
 * {@link SocketNode}.
 * 
 * For more information about this appender, please refer to the online manual at
 * http://logback.qos.ch/manual/appenders.html#AccessSocketAppender
 *  
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 * 
 */

public class SocketAppender extends SocketAppenderBase<AccessEvent> {
  
  public SocketAppender() {
  }

  /**
   * Connects to remote server at <code>address</code> and <code>port</code>.
   */
  public SocketAppender(InetAddress address, int port) {
    this.address = address;
    this.remoteHost = address.getHostName();
    this.port = port;
  }

  /**
   * Connects to remote server at <code>host</code> and <code>port</code>.
   */
  public SocketAppender(String host, int port) {
    this.port = port;
    this.address = getAddressByName(host);
    this.remoteHost = host;
  }
  
  @Override
  protected void postProcessEvent(AccessEvent event) {
    AccessEvent ae = (AccessEvent)event;
    ae.prepareForDeferredProcessing();
  }

}
