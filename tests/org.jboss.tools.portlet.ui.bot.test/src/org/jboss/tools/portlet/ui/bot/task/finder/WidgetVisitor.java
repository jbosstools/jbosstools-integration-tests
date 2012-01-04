package org.jboss.tools.portlet.ui.bot.task.finder;

import org.eclipse.swt.widgets.Widget;

/**
 * Visitor for the {@link Widget} objects.
 * 
 * @author Lucia Jelinkova
 *
 */
public interface WidgetVisitor {

	void visit(Widget widget);
	
}
