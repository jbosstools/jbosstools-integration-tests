package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of creating a scalable application
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID410CreateScalableApplicationTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	@Before
	public void createApplication() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, true, true, true);
	}
	
	@Test
	public void testCreateScalableApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.APPLICATION_DETAILS).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_DETAILS),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_DETAILS);

		for (TreeItem item: new DefaultTree().getAllItems()) {
			if (item.getCell(0).equals("Scalable")) {
				assertTrue("Application is not scalable, at least it is not shown "
						+ "in application properties.", item.getCell(1).equals("true"));
			}
		}
		
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_DETAILS),
				TimePeriod.LONG);
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		assertTrue("Application is not scalable, at least it is not shown in properties"
				+ " view.", propertiesView.getProperty("Scalable").getPropertyValue().equals("true"));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();;
	}
}
