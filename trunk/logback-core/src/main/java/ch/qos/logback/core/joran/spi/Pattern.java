/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 2000-2008, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.joran.spi;

import java.util.ArrayList;

public class Pattern {

  // contains String instances
  ArrayList<String> partList = new ArrayList<String>();

  public Pattern() {
  }

  /**
   * Build a pattern from a string.
   * 
   * Note that "/x" is considered equivalent to "x" and to "x/"
   * 
   */
  public Pattern(String p) {
    this();

    if (p == null) {
      return;
    }

    int lastIndex = 0;

    // System.out.println("p is "+ p);
    while (true) {
      int k = p.indexOf('/', lastIndex);

      // System.out.println("k is "+ k);
      if (k == -1) {
        String lastPart = p.substring(lastIndex);
        if (lastPart != null && lastPart.length() > 0) {
          partList.add(p.substring(lastIndex));
        }
        break;
      } else {
        String c = p.substring(lastIndex, k);

        if (c.length() > 0) {
          partList.add(c);
        }

        lastIndex = k + 1;
      }
    }

    // System.out.println(components);
  }

  public Object clone() {
    Pattern p = new Pattern();
    p.partList.addAll(this.partList);
    return p;
  }

  public void push(String s) {
    partList.add(s);
  }

  public int size() {
    return partList.size();
  }

  public String get(int i) {
    return (String) partList.get(i);
  }

  public void pop() {
    if (!partList.isEmpty()) {
      partList.remove(partList.size() - 1);
    }
  }

  public String peekLast() {
    if (!partList.isEmpty()) {
      int size = partList.size();
      return (String) partList.get(size - 1);
    } else {
      return null;
    }
  }

  /**
   * Returns the number of "tail" components that this pattern has in common
   * with the pattern p passed as parameter. By "tail" components we mean the
   * components at the end of the pattern.
   */
  public int getTailMatchLength(Pattern p) {
    if (p == null) {
      return 0;
    }

    int lSize = this.partList.size();
    int rSize = p.partList.size();

    // no match possible for empty sets
    if ((lSize == 0) || (rSize == 0)) {
      return 0;
    }

    int minLen = (lSize <= rSize) ? lSize : rSize;
    int match = 0;

    // loop from the end to the front
    for (int i = 1; i <= minLen; i++) {
      String l = (String) this.partList.get(lSize - i);
      String r = (String) p.partList.get(rSize - i);

      if (l.equals(r)) {
        match++;
      } else {
        break;
      }
    }

    return match;
  }

  /**
   * Returns the number of "prefix" components that this pattern has in common
   * with the pattern p passed as parameter. By "prefix" components we mean the
   * components at the beginning of the pattern.
   */
  public int getPrefixMatchLength(Pattern p) {
    if (p == null) {
      return 0;
    }

    int lSize = this.partList.size();
    int rSize = p.partList.size();

    // no match possible for empty sets
    if ((lSize == 0) || (rSize == 0)) {
      return 0;
    }

    int minLen = (lSize <= rSize) ? lSize : rSize;
    int match = 0;

    for (int i = 0; i < minLen; i++) {
      String l = (String) this.partList.get(i);
      String r = (String) p.partList.get(i);

      // if (l.equals(r) || "*".equals(l) || "*".equals(r)) {
      if (l.equals(r)) {
        match++;
      } else {
        break;
      }
    }

    return match;
  }

  @Override
  public boolean equals(Object o) {
    // System.out.println("in equals:" +this+ " vs. " + o);
    if ((o == null) || !(o instanceof Pattern)) {
      return false;
    }

    // System.out.println("both are Patterns");
    Pattern r = (Pattern) o;

    if (r.size() != size()) {
      return false;
    }

    // System.out.println("both are size compatible");
    int len = size();

    for (int i = 0; i < len; i++) {
      if (!(get(i).equals(r.get(i)))) {
        return false;
      }
    }

    // if everything matches, then the twp patterns are equal
    return true;
  }

  @Override
  public int hashCode() {
    int hc = 0;
    int len = size();

    for (int i = 0; i < len; i++) {
      hc ^= get(i).hashCode();

      // System.out.println("i = "+i+", hc="+hc);
    }

    return hc;
  }

  @Override
  public String toString() {
    int size = partList.size();
    String result = "";
    for (int i = 0; i < size; i++) {
      result += "[" + partList.get(i) + "]";
    }
    return result;
  }
}
