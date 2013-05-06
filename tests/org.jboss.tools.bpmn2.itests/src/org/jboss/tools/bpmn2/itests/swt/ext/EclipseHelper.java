package org.jboss.tools.bpmn2.itests.swt.ext;

import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.swt.util.Bot;

public class EclipseHelper {
	/**
	 * Maximizes active shell
	 * 
	 * Taken from ui.bot.ext
	 */
	public static void maximizeActiveShell() {
		final Shell shell = (Shell) (Bot.get().activeShell().widget);
		Bot.get().getDisplay().syncExec(new Runnable() {

			public void run() {
				shell.setMaximized(true);

			}
		});
	}
}
