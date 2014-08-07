package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.openshift.ui.bot.test.domain.ID203DeleteDomainTest;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of opening new application wizard without domain on the connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID304OpenNewApplicationWizardWithoutDomainTest {

	private static boolean domainsDeleted;
	
	@BeforeClass
	public static void deleteDomain() {
		domainsDeleted = ID203DeleteDomainTest.deleteDomain(Datastore.USERNAME, Datastore.DOMAIN) &&
				ID203DeleteDomainTest.deleteDomain(Datastore.USERNAME, Datastore.SECOND_DOMAIN);
	}
	
	@Test
	public void testOpenNewApplicationWizardViaExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getConnection(Datastore.USERNAME).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_APPLICATION).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
		}
	}
	
	@Test
	public void testOpenNewApplicationWizardViaMenu() {
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			new NextButton().click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
		}
	}
	
	@Test
	public void testOpenNewApplicationWizardViaCentral() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, OpenShiftLabel.Others.OPENSHIFT_APP).activate();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			new NextButton().click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
		}
	}
	
	@AfterClass
	public static void addDomain() {
		if (domainsDeleted) {
			ID203DeleteDomainTest.createDomain(Datastore.USERNAME, Datastore.DOMAIN);
			ID203DeleteDomainTest.createDomain(Datastore.USERNAME, Datastore.SECOND_DOMAIN);
		}
	}
}
