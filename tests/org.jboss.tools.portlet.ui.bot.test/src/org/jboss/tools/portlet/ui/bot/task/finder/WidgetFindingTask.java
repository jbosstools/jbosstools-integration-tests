package org.jboss.tools.portlet.ui.bot.task.finder;

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
 * 
 * Finds all widgets recursively and delegates the concrete action
 * to be performed on the visitor (see GOF Visitor pattern)
 * 
 * @author Lucia Jelinkova
 *
 */
public class WidgetFindingTask extends AbstractSWTTask {

	private Matcher<? extends Widget> matcher;

	private Widget parentWidget;
	
	private WidgetVisitor visitor;
	
	/**
	 * Finds all widgets for the active shell. 
	 */
	public WidgetFindingTask(WidgetVisitor visitor) {
		this(null, visitor);
	}

	/**
	 * Finds all widgets meeting specified matcher's condition for the active shell. 
	 * 
	 * @param matcher
	 */
	public WidgetFindingTask(Matcher<? extends Widget> matcher, WidgetVisitor visitor) {
		this(null, matcher, visitor);
	}

	/**
	 * Finds all widgets starting from the specified widget meeting
	 * specified matcher condition. 
	 * 
	 * @param widget
	 * @param matcher
	 */
	public WidgetFindingTask(Widget widget, Matcher<? extends Widget> matcher, WidgetVisitor visitor) {
		super();
		this.matcher = matcher;
		this.parentWidget = widget;
		this.visitor = visitor;
	}

	@Override
	public void perform() {
		syncExec(new VoidResult() {

			@Override
			public void run() {
				findWidgetRecursive(getParentWidget(), new ArrayList<Widget>());
			}
		});
	}

	private void findWidgetRecursive(Widget widget, ArrayList<Widget> visited){
		
		if (visited.contains(widget)){
			return;
		} 

		visited.add(widget);
		visitWidget(widget);
		
		if (widget instanceof Composite && ((Composite) widget).getChildren().length == 0){
			return;
		}
		
		List<? extends Widget> children = getBot().widgets(getMatcher(), widget);
		for (Widget child : children){
			findWidgetRecursive(child, visited);
		}
	}
	
	private void visitWidget(Widget widget){
		new VisitableWidget(widget).accept(visitor);
	}
	
	public Matcher<? extends Widget> getMatcher() {
		if (matcher == null){
			matcher = widgetOfType(Widget.class);
		}
		return matcher;
	}

	private Widget getParentWidget() {
		if (parentWidget == null){
			parentWidget = getBot().activeShell().widget;
		}
		return parentWidget;
	}
}
