package org.jboss.tools.teiid.reddeer.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.jboss.reddeer.swt.util.Bot;

public class TeiidStyledText extends SWTBotStyledText {

	public TeiidStyledText(StyledText styledText) throws WidgetNotFoundException {
		super(styledText);
	}

	public TeiidStyledText(int index) {
		super(Bot.get().styledText(index).widget);
	}

	/**
	 * Mouse click on current caret position
	 */
	public void mouseClickOnCaret() {

		UIThreadRunnable.syncExec(new VoidResult() {

			@Override
			public void run() {
				widget.forceFocus();

				int caret_x = widget.getCaret().getLocation().x;
				int caret_y = widget.getCaret().getLocation().y;

				// Move mouse
				Event event = new Event();
				event.type = SWT.MouseMove;
				event.x = widget.toDisplay(caret_x, caret_y).x;
				event.y = widget.toDisplay(caret_x, caret_y).y;
				widget.getDisplay().post(event);

				// Mouse down
				event = new Event();
				event.type = SWT.MouseDown;
				event.button = 1;
				widget.getDisplay().post(event);
				// Mouse Up
				event = new Event();
				event.type = SWT.MouseUp;
				event.button = 1;
				widget.getDisplay().post(event);

			}
		});
	}
}
