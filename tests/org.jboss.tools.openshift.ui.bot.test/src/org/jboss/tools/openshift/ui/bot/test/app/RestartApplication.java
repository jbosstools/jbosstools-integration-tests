package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestartApplication extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
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
		
		connection.getItems().get(0).expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).getItem(DYI_APP + " " + OpenShiftLabel.AppType.DIY).select();
		new ContextMenu(OpenShiftLabel.Labels.EXPLORER_RESTART_APP).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).getItem(DYI_APP + " " + OpenShiftLabel.AppType.DIY).select();
		new ContextMenu("Show in Web Browser").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		// TODO showed in web browser correctly
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY);
	}
}
