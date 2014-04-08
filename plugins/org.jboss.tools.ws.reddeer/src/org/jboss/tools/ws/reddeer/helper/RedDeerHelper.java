package org.jboss.tools.ws.reddeer.helper;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;

/**
 * Red Deer Helper
 * 
 * @author Radoslav Rabara
 */
public class RedDeerHelper {
	
	/**
	 * Do a click on the specified column
	 * 
	 * Workaround for REDDEER issue 557
	 * 
	 * @see https://github.com/jboss-reddeer/reddeer/issues/557
	 */
	public static void click(final TreeItem treeItem, final int column) {
		org.eclipse.swt.widgets.TreeItem swtTreeItem = Display.syncExec(new ResultRunnable<org.eclipse.swt.widgets.TreeItem>(){
			@Override
			public org.eclipse.swt.widgets.TreeItem run() {
				return treeItem.getSWTWidget();
			}
		});
		SWTBotTreeItem ti = new SWTBotTreeItem(swtTreeItem);
		ti.click(column);
	}
}
