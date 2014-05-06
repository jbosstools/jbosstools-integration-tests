package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeleteScalablePythonApp {

	private final String PYTHON_APP_NAME = "spythonapp" + new Date().getTime();
	
	@Test
	public void canCreateScalablePythonApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PYTHON, PYTHON_APP_NAME, true, true, true);
	}
	
	@After
	public void canDeleteScalablePythonApp() {
		new DeleteApplication(PYTHON_APP_NAME).perform();
	}
}
