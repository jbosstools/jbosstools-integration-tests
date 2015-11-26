package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

public class ID603AddDownloadableEmbeddableCartridgeTest extends IDXXXCreateTestingApplication {

	// Downloadable embeddable cartridge URL
	private String URL = "http://cartreflect-claytondev.rhcloud.com/reflect?github=ncdc/openshift-foreman-cartridge";
	
	@Test
	public void testAddDownloadableEmbeddableCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		application.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.DOWNLOADABLE_CARTRIDGE).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.DOWNLOADABLE_CARTRIDGE).setChecked(true);
		
		assertFalse("Finish button should be disable if downloadable cartridge is selected but URL is not filled.",
				new FinishButton().isEnabled());
		
		new LabeledText(OpenShiftLabel.TextLabels.EMBEDDED_CARTRIDGE_URL).setText(URL);
		
		try {
			new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Finish button should be enabled after filling downloadable cartridge's URL.");
		}
		
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE).setFocus();
		new OkButton().click();
	
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		application.select();
		application.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			treeViewerHandler.getTreeItem(application.getTreeItem(), "Foreman");
		} catch (JFaceLayerException ex) {
			fail("Downloadable embedded cartridge has not been added successfully.");
		}
	}
	
}
