package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.Before;
import org.junit.Test;

/**
 * Create a new domain on a OpenShift connection.
 * 
 * @author  mlabuda@redhat.com
 * 
 */
public class CreateDomain {

	@Before
	public void waitNoJobs() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@Test
	public void canCreateDomain() {
		createDomain(true, false);
	}
	 
	// be sure that the given openshift server support multiple domains
	public static void createDomain(boolean runFromCreateDomain, boolean multipleDomain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		TreeItem connection = explorer.getConnection();
		connection.select();
		
		new ContextMenu(OpenShiftLabel.Labels.MANAGE_DOMAINS).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.LONG);
		
		new DefaultShell("Domains");
		
		if (runFromCreateDomain) {
			assertTrue("Domain should not be set at this stage! Remove domains and run tests again", new DefaultTable().rowCount() == 0);
		}
		
		String domainName = "jbdstestdomain" + new Random().nextInt(100);
		
		new PushButton("New...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Create Domain"), TimePeriod.LONG);
		
		new DefaultShell("Create Domain");
		new DefaultText(0).setText(domainName);
		new PushButton("Finish").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		Logger logger = new Logger(CreateDomain.class);
		logger.info("*** OpenShift RedDeer Tests: Domain created. ***");		
		
		new DefaultShell("Domains").setFocus();
		
		if (multipleDomain) {
			new PushButton("New...").click();
			
			new WaitUntil(new ShellWithTextIsAvailable("Create Domain"), TimePeriod.LONG);
			
			new DefaultShell("Create Domain");
			new DefaultText().setText("seconddomain69");
			
			new WaitUntil(new ButtonWithTextIsActive(new PushButton(
					OpenShiftLabel.Button.FINISH)), TimePeriod.NORMAL);
			
			new PushButton("Finish").click();
			
			new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
			logger.info("*** OpenShift RedDeer Tests: Second Domain created. ***");
			
			new DefaultShell("Domains").setFocus();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		if (multipleDomain) {
			assertTrue("There is not multiple domains. Number of domains is " + 
					new DefaultTable().getItems().size(), new DefaultTable().getItems().size() == 2);
		} else {
			assertTrue("Domain does not exist", new DefaultTable().getItems().size() == 1);
		}
		
		new PushButton("OK").click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
