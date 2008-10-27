package ch.qos.logback.access.spi;

import java.util.Iterator;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;

/**
 * This class is a context that can be used
 * by access to provide the basic functionnalities of a context
 * to its components, mainly SocketServer.
 *
 * @author S&eacute;bastien Pennec
 */
public class AccessContext extends ContextBase implements AppenderAttachable<AccessEvent>, FilterAttachable {

  AppenderAttachableImpl<AccessEvent> aai = new AppenderAttachableImpl<AccessEvent>();
  FilterAttachableImpl fai = new FilterAttachableImpl();
  
  public void callAppenders(AccessEvent event) {
    aai.appendLoopOnAppenders(event);
  }
  
  public void addAppender(Appender<AccessEvent> newAppender) {
    aai.addAppender(newAppender);
  }

  public void detachAndStopAllAppenders() {
    aai.detachAndStopAllAppenders();
  }

  public boolean detachAppender(Appender appender) {
    return aai.detachAppender(appender);
  }

  public boolean detachAppender(String name) {
    return aai.detachAppender(name);
  }

  public Appender<AccessEvent> getAppender(String name) {
    return aai.getAppender(name);
  }

  public boolean isAttached(Appender appender) {
    return aai.isAttached(appender);
  }

  public Iterator<Appender<AccessEvent>> iteratorForAppenders() {
    return aai.iteratorForAppenders();
  }

  public void addFilter(Filter newFilter) {
   fai.addFilter(newFilter); 
  }

  public void clearAllFilters() {
    fai.clearAllFilters();
  }

  public FilterReply getFilterChainDecision(Object event) {
    return fai.getFilterChainDecision(event);
  }

  public Filter getFirstFilter() {
    return fai.getFirstFilter();
  }
}
