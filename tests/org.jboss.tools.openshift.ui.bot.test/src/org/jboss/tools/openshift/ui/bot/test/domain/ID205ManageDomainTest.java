package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.ui.condition.TableIsEnabled;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test domains managment via Manage domains shell.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID205ManageDomainTest {

	@Test
	public void testManageDomains() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getConnection(Datastore.USERNAME).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_DOMAINS).select();;	
		
		testCreateNewDomain();
		
		testEditDomain();
		
		testDeleteDomain();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MANAGE_DOMAINS), TimePeriod.LONG);
	}
	
	private void testCreateNewDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		int domainsCount = new DefaultTable().getItems().size();
		
		new PushButton(OpenShiftLabel.Button.CREATE_DOMAIN).click();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(Datastore.X_DOMAIN);
	
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		assertTrue("New domain has not been created successfully.",
				domainsCount + 1 == new DefaultTable().getItems().size());
	}
	
	private void testEditDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		new DefaultTable().getItem(Datastore.X_DOMAIN).select();
		
		new PushButton(OpenShiftLabel.Button.EDIT_DOMAIN).click();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_DOMAIN);
		
		Datastore.X_DOMAIN = "xqetestjbds" + new Random().nextInt(100);
		
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(Datastore.X_DOMAIN);
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_DOMAIN), 
				TimePeriod.LONG);
		
		try {
			new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
			new DefaultTable().getItem(Datastore.X_DOMAIN);
			// pass
		} catch (SWTLayerException ex) {
			fail("Domain does not exists in table.");
		}
	}
	
	private void testDeleteDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		new DefaultTable().getItem(Datastore.X_DOMAIN).select();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.REMOVE)), TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.REMOVE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_DOMAIN);
		
		new CheckBox(0).click();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_DOMAIN),
				TimePeriod.LONG);
		
		new WaitUntil(new TableIsEnabled(new DefaultTable()), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning());
		
		try {
			new DefaultTable().getItem(Datastore.X_DOMAIN);
			fail("Domain should not exists in table.");
		} catch (CoreLayerException ex) {
			// PASS
		}
	}
}
