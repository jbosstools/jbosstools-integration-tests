/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.widgets;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.matcher.AndMatcher;
import org.jboss.tools.browsersim.matcher.ClassMatcher;
import org.jboss.tools.browsersim.matcher.MatcherBuilder;
import org.jboss.tools.browsersim.ui.BrowserSim;
import org.jboss.tools.browsersim.ui.skin.DeviceComposite;
import org.jboss.tools.browsersim.wait.ShellIsAvailable;
import org.jboss.tools.browsersim.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.WaitUntil;
import org.jboss.tools.browsersim.wait.WaitWhile;

public class WidgetLookup {
	
	public static Control getBrowsersimControl(){
		Shell trShell = getTruncatedShell();
		if(trShell != null){
			new PushButton("Truncate (recommended)").click();
			new WaitWhile(new ShellIsAvailable(trShell));
		}
		return RDDisplay.syncExec(new ResultRunnable<Control>() {

			@Override
			public Control run() {
				Shell s = RDDisplay.getDisplay().getActiveShell();
				if(s == null){
					s = RDDisplay.getDisplay().getShells()[0];
				}
				for(Control c: s.getChildren()){
					if(c instanceof DeviceComposite){
						((Shell)c.getParent()).setMinimized(false);
						((Shell)c.getParent()).forceActive();
						c.setFocus();
						return c;
					}
				}
				return null;
			}
		});
	}
	
	public static Shell getTruncatedShell(){
		return RDDisplay.syncExec(new ResultRunnable<Shell>() {

			@Override
			public Shell run() {
				for(Shell s: RDDisplay.getDisplay().getShells()){
					if(s.getText().equals("Device size will be truncated")){
						return s;
					}
			
				}
				return null;
			}
		});
	}
	
	public static IBrowser getBrowsersimBrowser(){
		getBrowsersimControl();
		return BrowserSim.getInstances().get(0).getBrowser();
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Widget> T activeWidget(Control refComposite, Matcher matcher, int index) {
		T widget = (T)findControl(refComposite, matcher, true, index);
		return widget;
	}
	
	private static <T extends Widget> T findControl(final Widget parentWidget, 
			final Matcher<T> matcher, final boolean recursive, final int index) {
		T ret = RDDisplay.syncExec(new ResultRunnable<T>() {

			@Override
			public T run() {
				return findControlUI(parentWidget, matcher, recursive, new Index(index));
			}
		});
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Widget> T findControlUI(final Widget parentWidget, final Matcher<T> matcher, final boolean recursive, Index index) {
		if ((parentWidget == null) || parentWidget.isDisposed() || !visible(parentWidget)) {
			return null;
		}

		if (matcher.matches(parentWidget))
			try {
				T control = (T) parentWidget;
				if(index.isFirst()) {
					return control;
				} else {
					index.passed();
				}
			} catch (ClassCastException exception) {
				throw new IllegalArgumentException("The specified matcher should only match against is declared type.", exception);
			}
		if (recursive) {
			List<Widget> children = WidgetResolver.getInstance().getChildren(parentWidget);
			return findControlUI(children, matcher, recursive, index);
		}
		return null;
	}
	
	private static <T extends Widget> T findControlUI(final List<Widget> widgets, final Matcher<T> matcher, final boolean recursive, Index index) {
		for (Widget w : widgets) {
			T control = findControlUI(w, matcher, recursive, index);
			if(control != null) {
				return control;
			}
		}
		return null;
	}
	
	private static class Index {
		private int value;

		public Index(int index) {
			value = index;
		}

		public boolean isFirst() {
			return value <= 0;
		}

		public void passed() {
			value--;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static <T extends Widget> T activeWidget(Control refComposite, Class<T> clazz, int index, Matcher... matchers) {				
		return activeWidget(refComposite, clazz, index, TimePeriod.SHORT , matchers);
	}
	
	@SuppressWarnings({ "rawtypes","unchecked" })
	public static <T extends Widget> T activeWidget(Control refComposite, Class<T> clazz, int index, TimePeriod timePeriod, Matcher... matchers) {				
		ClassMatcher cm = new ClassMatcher(clazz);
		if(matchers == null){
			List<Matcher<?>> list= new ArrayList<Matcher<?>>();
			matchers = list.toArray(new Matcher[list.size()]);
		}
		Matcher[] allMatchers = MatcherBuilder.getInstance().addMatcher(matchers, cm);
		AndMatcher am  = new AndMatcher(allMatchers);

		Control parentControl = getParentControl(refComposite);
		WidgetIsFound found = new WidgetIsFound(parentControl, index, am.getMatchers());
		new WaitUntil(found, timePeriod);
		return (T)found.getWidget();
	}
	
	private static Control getParentControl(Control refComposite){
		if (refComposite == null){
			return findParent();
		}
		return refComposite;
	}
	
	/**
	 * Finds out whether widget is visible or not.
	 * 
	 * @param w widget to resolve
	 * @return true if widget is visible, false otherwise
	 */
	private static boolean visible(Widget w) {
		return !((w instanceof Control) && !((Control) w).getVisible());
	}
	
	/**
	 * Finds parent control of active widget .
	 *
	 * @return parent control of active widget  or throws an exception if null
	 */
	public static Control findParent(){
		Control parent = ShellLookup.getCurrentActiveShell();

		if (parent == null){
			throw new IllegalArgumentException("Unable to determine active parent");
		}

		return parent;
	}
	
}
