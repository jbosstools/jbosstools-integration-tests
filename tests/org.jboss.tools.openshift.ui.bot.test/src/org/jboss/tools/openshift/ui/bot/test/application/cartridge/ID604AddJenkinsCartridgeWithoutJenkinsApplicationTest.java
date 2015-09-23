package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.junit.Test;

public class ID604AddJenkinsCartridgeWithoutJenkinsApplicationTest extends IDXXXCreateTestingApplication {

	private String jenkinsApplication = "jenkins" + System.currentTimeMillis();
	
	@Test
	public void testAddJenkinsCartridgeWithoutJenkinsApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		OpenShift2Application application = explorer.getOpenShift2Connection(Datastore.USERNAME, Datastore.SERVER).
				getDomain(Datastore.DOMAIN).getApplication(applicationName);
		application.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.JENKINS).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.JENKINS).setChecked(true);
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG);
		new PushButton(OpenShiftLabel.Button.APPLY).click();
		
		new DefaultShell("New Jenkins application");
		new DefaultText().setText(jenkinsApplication);
		
		new WaitUntil(new ButtonWithTextIsEnabled(new OkButton()), TimePeriod.NORMAL);
		
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
		new OkButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE).setFocus();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	
		new WaitUntil(new OpenShiftApplicationExists(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, 
				jenkinsApplication), TimePeriod.LONG);
		
		try {
			application.select();
			treeViewerHandler.getTreeItem(application.getTreeItem(), "Jenkins Client");
		} catch (JFaceLayerException ex) {
			fail("Jenkins cartridge has not been added into the application. " + application.
					getTreeItem().getItems().get(0).getText());
		}
		
		explorer.open();
		new DeleteUtils(Datastore.USERNAME, Datastore.SERVER, Datastore.DOMAIN, 
				jenkinsApplication, jenkinsApplication).deleteOpenShiftApplication();
	}
}
