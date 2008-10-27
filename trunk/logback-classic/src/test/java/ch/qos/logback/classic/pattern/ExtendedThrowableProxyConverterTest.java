package ch.qos.logback.classic.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;

public class ExtendedThrowableProxyConverterTest {

  LoggerContext lc = new LoggerContext();
  ExtendedThrowableProxyConverter etpc = new ExtendedThrowableProxyConverter();
  StringWriter sw = new StringWriter();
  PrintWriter pw = new PrintWriter(sw);

  @Before
  public void setUp() throws Exception {
    etpc.setContext(lc);
    etpc.start();
  }

  @After
  public void tearDown() throws Exception {
  }

  private LoggingEvent createLoggingEvent(Throwable t) {
    LoggingEvent le = new LoggingEvent(this.getClass().getName(), lc
        .getLogger(LoggerContext.ROOT_NAME), Level.DEBUG, "test message", t,
        null);
    return le;
  }

  @Test
  public void integration() {
    PatternLayout pl = new PatternLayout();
    pl.setContext(lc);
    pl.setPattern("%m%n");
    pl.start();
    LoggingEvent e = createLoggingEvent(new Exception("x"));
    String res = pl.doLayout(e);

    // make sure that at least some package data was output
    Pattern p = Pattern.compile(" \\[junit.*\\]");
    Matcher m = p.matcher(res);
    int i = 0;
    while(m.find()) {
      i++;
    }
    assertTrue(i+ " should be larger than 5", i > 5);
  }

  @Test
  public void smoke() {
    Exception t = new Exception("smoke");
    verify(t);
  }

  @Test
  public void nested() {
    Throwable t = makeNestedException(1);
    verify(t);
  }

  void verify(Throwable t) {
    t.printStackTrace(pw);

    LoggingEvent le = createLoggingEvent(t);
    String result = etpc.convert(le);
    result = result.replace("common frames omitted", "more");
    result = result.replaceAll(" \\[.*\\]", "");
    assertEquals(sw.toString(), result);
  }

  Throwable makeNestedException(int level) {
    if (level == 0) {
      return new Exception("nesting level=" + level);
    }
    Throwable cause = makeNestedException(level - 1);
    return new Exception("nesting level =" + level, cause);
  }
}
