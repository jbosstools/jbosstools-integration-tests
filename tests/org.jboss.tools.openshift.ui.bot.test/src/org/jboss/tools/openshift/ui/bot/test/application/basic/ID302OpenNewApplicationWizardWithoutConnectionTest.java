package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of opening wizard via 2 ways without existing connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID302OpenNewApplicationWizardWithoutConnectionTest {

	@BeforeClass
	public static void removeConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).remove();
	}
	
	@Test
	public void testOpenWizardViaMenu() {
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			// try that there are widgets for credentials
			new LabeledText(OpenShiftLabel.TextLabels.PASSWORD);
			new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION);
			
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("New Application Wizard has not been opened with new connection page.");
		}	
		
	}
	
	@Test
	public void testOpenWizardViaCentral() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);

		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			// try that there are widgets for credentials
			new LabeledText(OpenShiftLabel.TextLabels.PASSWORD);
			new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION);
			
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("New Application Wizard has not been opened with new connection page.");
		}
	}
	
	@AfterClass
	public static void recreateConnection() {
		boolean certificateShown = System.getProperty("openshift.xserver") != null;
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		if (!explorer.connectionExists(DatastoreOS2.USERNAME)) {
			explorer.openConnectionShell();
			explorer.connectToOpenShift2(DatastoreOS2.SERVER, DatastoreOS2.USERNAME,
					System.getProperty("user.pwd"), false, false, certificateShown);
		}
	}
}
