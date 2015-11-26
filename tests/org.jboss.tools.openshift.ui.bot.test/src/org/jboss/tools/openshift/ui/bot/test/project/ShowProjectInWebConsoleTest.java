package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

public class ShowProjectInWebConsoleTest {

	@Test
	public void testShowProjectInWebConsole() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift3Connection().getProject().select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_WEB_CONSOLE).select();
		
		try {
			BrowserEditor browser = new BrowserEditor("OpenShift Web Console");
			assertTrue("Web console was not opened in context of a project", 
					browser.getPageURL().contains(DatastoreOS3.PROJECT1));
			browser.close();
		} catch (RedDeerException ex) {
			fail("Web Console for a project has not been opened");
		}
	}
	
}
