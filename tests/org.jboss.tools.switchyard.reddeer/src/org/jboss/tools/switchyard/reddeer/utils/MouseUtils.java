package org.jboss.tools.switchyard.reddeer.utils;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;

/**
 * Class which provides various mouse operations.
 * 
 * @author apodhrad
 * 
 */
public class MouseUtils {

	public static void click(final int x, final int y) {
		mouseMove(x, y);
		mouseDown(x, y, 1);
		mouseUp(x, y, 1);
	}

	public static void doubleClick(final int x, final int y) {
		click(x, y);
		click(x, y);
	}

	public static void mouseMove(final int x, final int y) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.type = SWT.MouseMove;
				SWTUtils.display().post(event);
			}
		});
	}

	public static void mouseDown(final int x, final int y, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.button = button;
				event.type = SWT.MouseDown;
				SWTUtils.display().post(event);
			}
		});
	}

	public static void mouseUp(final int x, final int y, final int button) {
		asyncExec(new VoidResult() {
			public void run() {
				Event event = new Event();
				event.x = x;
				event.y = y;
				event.button = button;
				event.type = SWT.MouseUp;
				SWTUtils.display().post(event);
			}
		});
	}

}
