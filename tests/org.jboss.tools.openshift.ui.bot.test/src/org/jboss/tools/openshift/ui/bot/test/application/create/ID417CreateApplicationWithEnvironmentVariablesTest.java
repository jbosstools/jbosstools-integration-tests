package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
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
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME,
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.DIY, applicationName, false,
				true, true, false, null, null, true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(true);
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		
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
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
