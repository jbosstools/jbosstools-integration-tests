package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of creating an application with environment variables.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID417CreateApplicationWithEnvironmentVariablesTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicationWithEnvironmentVariables() {
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.DIY, Datastore.DOMAIN, applicationName, false,
				true, true, false, null, null, true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(true);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.selectApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_ENV_VARS).select();
		
		try {
			new WaitUntil(new ConsoleHasText("varname=varvalue"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Environment variable has not been created correctly while application"
					+ "was creating. There is no user defined environment variable.");
		}
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
