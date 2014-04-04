package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author  mlabuda
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
		explorer.open();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		DefaultTree connection = new DefaultTree(0);
		connection.setFocus();
		connection.getItems().get(0).select();
		new ContextMenu(OpenShiftLabel.Labels.MANAGE_DOMAINS).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.LONG);
		
		new DefaultShell("Domains").setFocus();
		
		if (runFromCreateDomain) {
			assertTrue("Domain should not be set at this stage! Remove domains and run tests again", new DefaultTable().rowCount() == 0);
		}
		
		String domainName = TestProperties.get("openshift.domain") + new Random().nextInt(10000);
		
		new PushButton("New...").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Create Domain"), TimePeriod.LONG);
		
		new DefaultShell("Create Domain").setFocus();
		new DefaultText(0).setText(domainName);
		new PushButton("Finish").click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		Logger logger = new Logger(CreateDomain.class);
		logger.info("*** OpenShift RedDeer Tests: Domain created. ***");		
		
		new DefaultShell("Domains").setFocus();
		
		if (multipleDomain) {
			new PushButton("New...").click();
			
			new WaitUntil(new ShellWithTextIsAvailable("Create Domain"), TimePeriod.LONG);
			
			new DefaultShell("Create Domain").setFocus();
			new DefaultText().setText("seconddomain69");
			new PushButton("Finish").click();
			
			new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
			logger.info("*** OpenShift RedDeer Tests: Second Domain created. ***");
			
			new WaitUntil(new ShellWithTextIsAvailable("Domains"), TimePeriod.NORMAL);
			
			new DefaultShell("Domains").setFocus();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		if (multipleDomain) {
			assertTrue("There is not multiple domains", new DefaultTable().getItems().size() == 2);
		} else {
			assertTrue("Domain does not exist", new DefaultTable().getItems().size() == 1);
		}
		
		new PushButton("OK").click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}
