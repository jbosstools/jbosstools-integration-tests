package org.jboss.tools.portlet.ui.bot.task.debug;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.hamcrest.Matcher;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

public class WriteAllWidgets extends AbstractSWTTask {

	private Matcher<? extends Widget> matcher;

	private Widget widget;

	public WriteAllWidgets(Matcher<? extends Widget> matcher) {
		super();
		this.matcher = matcher;
	}

	public WriteAllWidgets(Widget widget, Matcher<? extends Widget> matcher) {
		super();
		this.matcher = matcher;
		this.widget = widget;
	}

	@Override
	public void perform() {
		final List<? extends Widget> widgets = getBot().widgets(matcher, getParentWidget());

		asyncExec(new VoidResult() {

			@Override
			public void run() {
				writeWidgets(widgets);
			}
		});
	}

	private Widget getParentWidget() {
		if (widget == null){
			return getBot().activeShell().widget;
		}
		return widget;
	}

	private void writeWidgets(List<? extends Widget> widgets){
		System.out.println("List of current widgets");
		for (Widget w : widgets){
			System.out.println(w);
		}
		System.out.println("End of list");
	}
}
