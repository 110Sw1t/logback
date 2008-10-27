/**
 * LOGBack: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */

package ch.qos.logback.core.joran.action;

import java.util.Stack;

import org.xml.sax.Attributes;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.Pattern;
import ch.qos.logback.core.joran.spi.PropertySetter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;

/**
 * This action is responsible for tying together a parent object with a child
 * element for which there is no explicit rule.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class NestedComplexPropertyIA extends ImplicitAction {

  // actionDataStack contains ActionData instances
  // We use a stack of ActionData objects in order to support nested
  // elements which are handled by the same NestedComplexPropertyIA instance.
  // We push a ActionData instance in the isApplicable method (if the
  // action is applicable) and pop it in the end() method.
  // The XML well-formedness property will guarantee that a push will eventually
  // be followed by a corresponding pop.
  Stack<IADataForComplexProperty> actionDataStack = new Stack<IADataForComplexProperty>();

  public boolean isApplicable(Pattern pattern, Attributes attributes,
      InterpretationContext ec) {
    // System.out.println("in NestedComplexPropertyIA.isApplicable [" + pattern
    // + "]");
    String nestedElementTagName = pattern.peekLast();

    // calling ec.peekObject with an empty stack will throw an exception
    if (ec.isEmpty()) {
      return false;
    }

    Object o = ec.peekObject();
    PropertySetter parentBean = new PropertySetter(o);
    parentBean.setContext(context);

    AggregationType aggregationType = parentBean
        .computeAggregationType(nestedElementTagName);

    switch (aggregationType) {
    case NOT_FOUND:
    case AS_BASIC_PROPERTY:
    case AS_BASIC_PROPERTY_COLLECTION:
      return false;

      // we only push action data if NestComponentIA is applicable
    case AS_COMPLEX_PROPERTY_COLLECTION:
    case AS_COMPLEX_PROPERTY:
      // addInfo("was deemed applicable for " + pattern);
      IADataForComplexProperty ad = new IADataForComplexProperty(parentBean,
          aggregationType, nestedElementTagName);
      actionDataStack.push(ad);

      return true;
    default:
      addError("PropertySetter.canContainComponent returned " + aggregationType);
      return false;
    }
  }

  public void begin(InterpretationContext ec, String localName,
      Attributes attributes) {
    // LogLog.debug("in NestComponentIA begin method");
    // get the action data object pushed in isApplicable() method call
    IADataForComplexProperty actionData = (IADataForComplexProperty) actionDataStack
        .peek();

    String className = attributes.getValue(CLASS_ATTRIBUTE);

    // perform variable name substitution
    className = ec.subst(className);

    if (OptionHelper.isEmpty(className)) {
      Class clazz = actionData.parentBean
          .findUnequivocallyInstantiableClass(actionData);
      if (clazz != null) {
        className = clazz.getName();
      } else {
        actionData.inError = true;
        String errMsg = "No class name attribute in [" + localName + "]";
        addError(errMsg);

        return;
      }
    }

    try {
      // getLogger().debug(
      // "About to instantiate component [{}] of type [{}]", localName,
      // className);

      actionData.nestedComplexProperty = Loader.loadClass(className, context)
          .newInstance();

      // pass along the repository
      if (actionData.nestedComplexProperty instanceof ContextAware) {
        ((ContextAware) actionData.nestedComplexProperty)
            .setContext(this.context);
      }
      // getLogger().debug(
      addInfo("Pushing component [" + localName
          + "] on top of the object stack.");
      ec.pushObject(actionData.nestedComplexProperty);
    } catch (Exception oops) {
      actionData.inError = true;
      String msg = "Could not create component [" + localName + "] of type ["
          + className + "]";
      addError(msg, oops);
    }
  }

  public void end(InterpretationContext ec, String tagName) {

    // pop the action data object pushed in isApplicable() method call
    // we assume that each this begin
    IADataForComplexProperty actionData = (IADataForComplexProperty) actionDataStack
        .pop();

    if (actionData.inError) {
      return;
    }

    PropertySetter nestedBean = new PropertySetter(
        actionData.nestedComplexProperty);
    nestedBean.setContext(context);

    // have the nested element point to its parent if possible
    if (nestedBean.computeAggregationType("parent") == AggregationType.AS_COMPLEX_PROPERTY) {
      nestedBean.setComplexProperty("parent", actionData.parentBean.getObj());
    }
    // start the nested complex attribute if it implements LifeCycle
    if (actionData.nestedComplexProperty instanceof LifeCycle) {
      ((LifeCycle) actionData.nestedComplexProperty).start();
    }

    Object o = ec.peekObject();

    if (o != actionData.nestedComplexProperty) {
      addError("The object on the top the of the stack is not the component pushed earlier.");
    } else {
      // getLogger().debug("Removing component from the object stack");
      ec.popObject();

      // Now let us attach the component
      switch (actionData.aggregationType) {
      case AS_COMPLEX_PROPERTY:
        // addInfo("Setting ["+tagName+"}] to parent of type
        // ["+actionData.parentBean.getObjClass()+"]");

        actionData.parentBean.setComplexProperty(tagName,
            actionData.nestedComplexProperty);

        break;
      case AS_COMPLEX_PROPERTY_COLLECTION:
        // getLogger().debug(
        // "Adding [{}] to parent of type [{}]", tagName,
        // actionData.parentBean.getObjClass());
        actionData.parentBean.addComplexProperty(tagName,
            actionData.nestedComplexProperty);

        break;
      }
    }
  }

}
