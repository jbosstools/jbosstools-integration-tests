package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * Create application with different gear size. Applications are typically created
 * with on a small gear. This test try to create application on a medium gear.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateApplicationWithDifferentGearSize {

	private String APP_NAME = "diyapp" + System.currentTimeMillis();
	
	@Test
	public void createApplicationWithMediumGears() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, false, true);	
		
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		explorer.verifyApplicationInBrowser(APP_NAME, "OpenShift");
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(APP_NAME).perform();
	}
	
}
