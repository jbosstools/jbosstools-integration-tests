package org.jboss.tools.openshift.ui.bot.test.domain;

import java.util.Random;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Before;
import org.junit.Test;

/**
 * Domain creation consists of creating the SSH key pair, storing user password
 * in the secure storage and creating the domain itself.
 * 
 * @author sbunciak, mlabuda
 * 
 */
@Require(clearWorkspace = true)
public class CreateDomain extends SWTTestExt {

	@Before
	public void waitNoJobs() {
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}

	@Test
	public void canCreateDomain() {
		createDomain(true, false);
	}
	 
	// be sure whether the given openshift server support multiple domains
	public static void createDomain(boolean runFromCreateDomain, boolean multipleDomain) {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(OpenShiftUI.Labels.MANAGE_DOMAINS).click();
		bot.shell("Select Domain");
		
		if (runFromCreateDomain) {
			assertTrue("Domain should not be set at this stage! Remove domains and run tests again", bot.tableInGroup("Domains").rowCount() == 0);
		}
		
		String domainName = TestProperties.get("openshift.domain") + new Random().nextInt(10000);
		
		bot.button("New...").click();
		bot.sleep(TIME_5S);
		bot.textWithLabel("Domain name").setText(domainName);
		bot.sleep(TIME_1S);
		bot.button("Finish").click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		log.info("*** OpenShift SWTBot Tests: Domain created. ***");		
		
		if (multipleDomain) {
			bot.button("New...").click();
			bot.sleep(TIME_5S);
			bot.textWithLabel("Domain name").setText("seconddomain69");
			bot.sleep(TIME_1S);
			bot.button("Finish").click();
			
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
			log.info("*** OpenShift SWTBot Tests: Second Domain created. ***");
		}
		
		if (multipleDomain) {
			assertTrue("There is not multiple domains", bot.table(0).rowCount() == 2);
		} else {
			assertTrue("Domain does not exist", bot.table(0).rowCount() == 1);
		}
		
		bot.button("OK").click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
	}
	
}
