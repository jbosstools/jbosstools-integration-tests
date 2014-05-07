package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Test create a new DIY application and try to restart it through a OpenShift
 * explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class RestartApplication {

	private final String DIY_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(OpenShiftLabel.AppType.DIY, DIY_APP, false, true, true);
	}

	@Test
	public void canRestartDYIApplicationViaExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		explorer.getApplication(DIY_APP).select();
	
		new ContextMenu(OpenShiftLabel.Labels.EXPLORER_RESTART_APP).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		// To be sure that page is loaded
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		assertTrue(explorer.verifyApplicationInBrowser(DIY_APP, "Welcome to OpenShift"));
	}

	@After
	public void deleteDIYApp() {
		new DeleteApplication(DIY_APP).perform();
	}
}
