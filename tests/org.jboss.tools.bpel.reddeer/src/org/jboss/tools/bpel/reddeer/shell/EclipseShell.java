package org.jboss.tools.bpel.reddeer.shell;

import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 *
 */
public class EclipseShell extends DefaultShell {

	public void maximize() {
		Bot.get().getDisplay().syncExec(new Runnable() {

			public void run() {
				shell.widget.setMaximized(true);

			}
		});
	}
}
