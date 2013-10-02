package org.jboss.tools.bpmn2.itests.reddeer;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;

public class EclipseHelper {
	/**
	 * Maximizes active shell
	 * 
	 * Taken from ui.bot.ext
	 */
	public static void maximizeActiveShell() {
		final Shell shell = (Shell) (new SWTBot().activeShell().widget);
		new SWTBot().getDisplay().syncExec(new Runnable() {

			public void run() {
				shell.setMaximized(true);

			}
		});
	}
}
