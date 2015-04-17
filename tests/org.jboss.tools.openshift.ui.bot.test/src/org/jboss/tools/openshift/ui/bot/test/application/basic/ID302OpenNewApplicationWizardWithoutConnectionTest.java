package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
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
		explorer.removeConnection(Datastore.USERNAME);
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
		
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, OpenShiftLabel.Others.OPENSHIFT_APP).activate();
		
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
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		if (!explorer.connectionExists(Datastore.USERNAME)) {
			explorer.openConnectionShell();
			explorer.connectToOpenShiftV2(Datastore.SERVER, Datastore.USERNAME,
					System.getProperty("user.pwd"), false, false);
		}
	}
}
