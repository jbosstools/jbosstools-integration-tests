package org.jboss.tools.portlet.ui.bot.task.debug;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.hamcrest.Matcher;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Finds all widgets recursively and writes debuggin information about each of them. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WriteAllWidgets extends AbstractSWTTask {

	private Matcher<? extends Widget> matcher;

	private Widget parentWidget;

	/**
	 * Finds all widgets for the active shell. 
	 */
	public WriteAllWidgets() {
		this(widgetOfType(Widget.class));
	}

	/**
	 * Finds all widgets meeting specified matcher's condition for the active shell. 
	 * 
	 * @param matcher
	 */
	public WriteAllWidgets(Matcher<? extends Widget> matcher) {
		this(null, matcher);
	}

	/**
	 * Finds all widgets starting from the specified widget meeting
	 * specified matcher condition. 
	 * 
	 * @param widget
	 * @param matcher
	 */
	public WriteAllWidgets(Widget widget, Matcher<? extends Widget> matcher) {
		super();
		this.matcher = matcher;
		this.parentWidget = widget;
	}

	@Override
	public void perform() {
		syncExec(new VoidResult() {

			@Override
			public void run() {
				System.out.println("List of current widgets");
				writeWidgetRecursive(getParentWidget(), new ArrayList<Widget>(), "");
				System.out.println("End of list");
			}
		});
	}

	private void writeWidgetRecursive(Widget widget, ArrayList<Widget> visited, String prefix){
		
		if (visited.contains(widget)){
			return;
		} 

		visited.add(widget);
		writeWidget(widget, prefix);
		
		if (widget instanceof Composite && ((Composite) widget).getChildren().length == 0){
			return;
		}
		
		List<? extends Widget> children = getBot().widgets(matcher, widget);
		for (Widget child : children){
			writeWidgetRecursive(child, visited, prefix + "---");
		}
	}
	
	private void writeWidget(Widget widget, String prefix){
		System.out.println(prefix + widget);
	}

	private Widget getParentWidget() {
		if (parentWidget == null){
			return getBot().activeShell().widget;
		}
		return parentWidget;
	}
}
