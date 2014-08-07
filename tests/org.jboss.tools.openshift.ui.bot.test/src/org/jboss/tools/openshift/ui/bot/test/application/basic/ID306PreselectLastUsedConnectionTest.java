package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.openshift.ui.bot.test.domain.ID201NewDomainTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID152AddExistingSSHKeyTest;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.internal.FirstWizardPage;
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
		ID201NewDomainTest.createDomain(Datastore.X_USERNAME, Datastore.X_DOMAIN);		
		ID152AddExistingSSHKeyTest.addExistingSSHKey(Datastore.X_USERNAME);
	}
	
	@Ignore // failing due to JBIDE-18082
	@Test
	public void testPreselectLastUsedConnectionFromExplorer() {
		preselectConnectionViaExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.USERNAME);
		
		preselectConnectionViaExplorer(Datastore.X_USERNAME, Datastore.X_DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.X_USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.X_USERNAME);
		
		preselectConnectionViaExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.USERNAME);
	}
	
	@Test
	public void testPreselectLastUsedConnectionFromMenu() {
		preselectConnectionViaMenu(Datastore.SERVER, Datastore.USERNAME, Datastore.DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.USERNAME);
		
		preselectConnectionViaMenu(Datastore.X_SERVER, Datastore.X_USERNAME, Datastore.X_DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.X_USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.X_USERNAME);
		
		preselectConnectionViaMenu(Datastore.SERVER, Datastore.USERNAME, Datastore.DOMAIN);
		verifyPreselectedConnectionViaCentral(Datastore.USERNAME);
		verifyPreselectedConnectionViaMenu(Datastore.USERNAME);
	}
	
	private void preselectConnectionViaMenu(String server, String username, String domain) {
		new WorkbenchShell().setFocus();
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).setSelection(
				username + " - " + "https://" + server);
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		new WaitUntil(new ButtonWithTextIsActive(new CancelButton()), TimePeriod.LONG);
		
		new CancelButton().click();
	}
	
	private void preselectConnectionViaExplorer(String username, String domain) {
		OpenNewApplicationWizard.openWizardFromExplorer(username, domain);
		
		new FirstWizardPage().createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.DIY);
		
		new NextButton().click();
		
		new WaitUntil(new ButtonWithTextIsActive(new BackButton()), TimePeriod.LONG);
		
		new LabeledText("Name:").setText("appName");
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();

		new WaitUntil(new ButtonWithTextIsActive(new CancelButton()), TimePeriod.LONG);
		
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
		
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, OpenShiftLabel.Others.OPENSHIFT_APP).activate();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);	
		
		assertTrue("Connection was not preselected correctly.",
				new LabeledCombo(OpenShiftLabel.TextLabels.CONNECTION).
					getText().contains(username));
		
		new CancelButton().click();
	}	
}		
