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

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.jboss.tools.browsersim.matcher.AndMatcher;
import org.jboss.tools.browsersim.wait.AbstractWaitCondition;

/**
 * WidgetIsFound is general condition to find desired widget
 * @author Jiri Peterka
 * 
 * @param <T> widget class
 */
public class WidgetIsFound <T extends Widget> extends AbstractWaitCondition {

	private Control parent;
	private AndMatcher am;
	private int index;
	private Widget properWidget;
	
	/**
	 * Looks for widgets under given parent control with given index and matching matchers.
	 *
	 * @param <T> the generic type
	 * @param parent given parent control
	 * @param index given index
	 * @param matchers given matchers
	 */
	@SuppressWarnings("hiding")
	public <T extends Widget> WidgetIsFound(Control parent, int index, Matcher<?>... matchers) {
		if (parent == null) {
			this.parent = WidgetLookup.findParent();
		} else {
			this.parent = parent;
		}
		this.am=new AndMatcher(matchers);
		this.index=index;
	}

	/**
	 * Looks for first widget under given parent control matching matchers.
	 *
	 * @param <T> the generic type
	 * @param parent given parent control
	 * @param matchers given matchers
	 */	
	@SuppressWarnings("hiding")
	public <T extends Widget> WidgetIsFound(Control parent, Matcher<?>... matchers) {
		this(parent, 0, matchers);
	}

	/**
	 * Looks for first widget under default parent control matching matchers.
	 *
	 * @param <T> the generic type
	 * @param matchers given matchers
	 */		
	@SuppressWarnings("hiding")
	public <T extends Widget> WidgetIsFound(Matcher<?>... matchers) {		
		this(null,0, matchers);
	}

	/**
	 * Tests if given widget is found.
	 *
	 * @return true if widget is found, false otherwise
	 */
	public boolean test() {
		
		properWidget = WidgetLookup.activeWidget(parent, am, index);

		if(properWidget == null){
			return false;
		}
		return true;
	}

	/**
	 * Returns found widget.
	 *
	 * @return found widget
	 */
	public Widget getWidget(){
		setFocus();
		return properWidget;
	}
	
	/**
	 * Returns condition description.
	 *
	 * @return the string
	 */
	@Override
	public String description() {
		return "widget is found";
	}

	private void setFocus(){
		//if (RunningPlatform.isWindows() && properWidget instanceof Button &&
		//		((WidgetHandler.getStyle((Button)properWidget) & SWT.RADIO) != 0)){
			// do not set focus because it also select radio button on Windows
		//} else {
			WidgetHandler.setFocus(properWidget);
		//}
	}
}
