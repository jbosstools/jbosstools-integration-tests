package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.FirstWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.domain.ID201NewDomainTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID152AddExistingSSHKeyTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test capabilities of using last selected connection in New application wizard.
 * 
 * @author mlabuda@redhat.com
 *
 */ 
public class ID306PreselectLastUsedConnectionTest {
	
	
	@BeforeClass
	public static void setUpSecondAccount() {
		ID201NewDomainTest.createDomain(DatastoreOS2.X_USERNAME, DatastoreOS2.X_SERVER, DatastoreOS2.X_DOMAIN);		
		ID152AddExistingSSHKeyTest.addExistingSSHKey(DatastoreOS2.X_USERNAME, DatastoreOS2.X_SERVER);
	}
	
	@Ignore("Ignored due to JBIDE-18082")
	@Test
	public void testPreselectLastUsedConnectionFromExplorer() {
		preselectConnectionViaExplorer(DatastoreOS2.USERNAME, DatastoreOS2.DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.USERNAME);
		
		preselectConnectionViaExplorer(DatastoreOS2.X_USERNAME, DatastoreOS2.X_DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.X_USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.X_USERNAME);
		
		preselectConnectionViaExplorer(DatastoreOS2.USERNAME, DatastoreOS2.DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.USERNAME);
	}
	
	@Test
	@Ignore("Preselection work flow has changed. Test need big changes at stability fixes.")
	public void testPreselectLastUsedConnectionFromMenu() {
		preselectConnectionViaMenu(DatastoreOS2.SERVER, DatastoreOS2.USERNAME, DatastoreOS2.DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.USERNAME);
		
		preselectConnectionViaMenu(DatastoreOS2.X_SERVER, DatastoreOS2.X_USERNAME, DatastoreOS2.X_DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.X_USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.X_USERNAME);
		
		preselectConnectionViaMenu(DatastoreOS2.SERVER, DatastoreOS2.USERNAME, DatastoreOS2.DOMAIN);
		verifyPreselectedConnectionViaCentral(DatastoreOS2.USERNAME);
		verifyPreselectedConnectionViaMenu(DatastoreOS2.USERNAME);
	}
	
	private void preselectConnectionViaMenu(String server, String username, String domain) {
		new WorkbenchShell().setFocus();
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).setSelection(
				username + " - " + server);
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new CancelButton()), TimePeriod.LONG);
		
		new CancelButton().click();
	}
	
	private void preselectConnectionViaExplorer(String username, String domain) {
		new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN).
				openWizardFromExplorer();
		
		new FirstWizardPage().createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.DIY);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new LabeledText("Name:").setText("appName");
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();

		new WaitUntil(new WidgetIsEnabled(new CancelButton()), TimePeriod.LONG);
		
		new CancelButton().click();
	}
	
	private void verifyPreselectedConnectionViaMenu(String username) {
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
	
		assertTrue("Connection was not preselected correctly.",
				new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).
					getText().contains(username));
		
		new CancelButton().click();	
	}
	
	private void verifyPreselectedConnectionViaCentral(String username) {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);	
		
		assertTrue("Connection was not preselected correctly.",
				new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).
					getText().contains(username));
		
		new CancelButton().click();
	}	
}		
