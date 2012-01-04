package org.jboss.tools.portlet.ui.bot.task.finder;

import org.eclipse.swt.widgets.Widget;

/**
 * Wraps {@link Widget} so that it can be used
 * in Visitor pattern. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class VisitableWidget {
	
	private Widget widget;
	
	public VisitableWidget(Widget w) {
		this.widget = w;
	}

	public void accept(WidgetVisitor visitor){
		visitor.visit(widget);
	}
}
