package ch.qos.logback.classic;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

public class StringListAppender extends AppenderBase<LoggingEvent> {

  Layout<LoggingEvent> layout;
  public List<String> strList = new ArrayList<String>();

  public void start() {
    strList.clear();
    
    if (layout == null || !layout.isStarted()) {
      return;
    }
    super.start();
  }

  public void stop() {
    super.stop();
  }

  @Override
  protected void append(LoggingEvent eventObject) {
    LoggingEvent le = (LoggingEvent) eventObject;
    String res = layout.doLayout(le);
    strList.add(res);
  }

  public Layout<LoggingEvent> getLayout() {
    return layout;
  }

  public void setLayout(Layout<LoggingEvent> layout) {
    this.layout = layout;
  }

}
