package org.jboss.tools.bpel.reddeer.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 *
 */
public class ListElement {

	private Canvas canvas;

	public ListElement(String label) {
		canvas = (Canvas) Bot.get().widget(new ListElementWithLabel(label));
	}

	public void select() {
		new ClickControl(canvas).click();
	}

	private class ListElementWithLabel extends BaseMatcher<Widget> {

		private String label;

		public ListElementWithLabel(String label) {
			this.label = label;
		}

		@Override
		public boolean matches(Object item) {
			if (item.getClass().getSimpleName().equals("ListElement")) {
				return item.toString().equals(label);
			}
			return false;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("of class '").appendText(label).appendText("'");
		}

	}

	private class ClickControl extends AbstractSWTBotControl<Composite> {

		public ClickControl(Composite w) {
			super(w);
		}

		public ClickControl click() {
			waitForEnabled();
			notify(SWT.MouseDown);
			notify(SWT.MouseUp);
			return this;
		}
	}
}
