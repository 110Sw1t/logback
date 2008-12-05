/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.access.html;

import java.util.Map;

import ch.qos.logback.access.PatternLayout;
import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.core.html.HTMLLayoutBase;
import ch.qos.logback.core.html.NOPThrowableRenderer;
import ch.qos.logback.core.pattern.Converter;
import static  ch.qos.logback.core.CoreConstants.LINE_SEPARATOR;

/**
 * 
 * HTMLLayout outputs events in an HTML table. 
 * <p>
 * The content of the table columns are specified using a conversion pattern. 
 * See {@link ch.qos.logback.access.PatternLayout} for documentation on the
 * available patterns.
 * <p>
 * For more information about this layout, please refer to the online manual at
 * http://logback.qos.ch/manual/layouts.html#AccessHTMLLayout
 * 
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 */
public class HTMLLayout extends HTMLLayoutBase<AccessEvent> {

  /**
   * Default pattern string for log output.
   */
  static final String DEFAULT_CONVERSION_PATTERN = "%h%l%u%t%r%s%b";

  /**
   * Constructs a PatternLayout using the DEFAULT_LAYOUT_PATTERN.
   * 
   */
  public HTMLLayout() {
    pattern = DEFAULT_CONVERSION_PATTERN;
    throwableRenderer = new NOPThrowableRenderer();
    cssBuilder = new DefaultCssBuilder();  
  }
  
  @Override
  protected Map<String, String> getDefaultConverterMap() {
    return PatternLayout.defaultConverterMap;
  }

  public String doLayout(AccessEvent event) {
    StringBuilder buf = new StringBuilder();
    startNewTableIfLimitReached(buf);

    boolean odd = true;
    if (((counter++) & 1) == 0) {
      odd = false;
    }

    buf.append(LINE_SEPARATOR);
    buf.append("<tr class=\"");
    if (odd) {
      buf.append(" odd\">");
    } else {
      buf.append(" even\">");
    }
    buf.append(LINE_SEPARATOR);

    Converter<AccessEvent> c = head;
    while (c != null) {
      appendEventToBuffer(buf, c, event);
      c = c.getNext();
    }
    buf.append("</tr>");
    buf.append(LINE_SEPARATOR);

    return buf.toString();
  }

  private void appendEventToBuffer(StringBuilder buf, Converter<AccessEvent> c,
      AccessEvent event) {
    buf.append("<td class=\"");
    buf.append(computeConverterName(c));
    buf.append("\">");
    buf.append(c.convert(event));
    buf.append("</td>");
    buf.append(LINE_SEPARATOR);
  }
}
