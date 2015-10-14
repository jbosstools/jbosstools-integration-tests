package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.Test;

public class ShowConnectionInWebConsoleTest {

	@Test
	public void testShowConnectionInWebConsole() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_CONSOLE).select();
		
		try {
			BrowserEditor browser = new BrowserEditor("OpenShift Web Console");
			browser.close();
		} catch (RedDeerException ex) {
			fail("Web Console for a project has not been opened");
		}
	}
}
