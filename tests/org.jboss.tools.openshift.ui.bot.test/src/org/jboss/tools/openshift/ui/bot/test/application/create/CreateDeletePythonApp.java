package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * Test capabilities of create/delete Python application on a OpenShift server.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateDeletePythonApp { 

	private final String PYTHON_APP_NAME = "pythonapp" + new Date().getTime();
	
	@Test
	public void canCreatePythonApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PYTHON, PYTHON_APP_NAME, false, true, true);
	}
	
	@After
	public void canDeletePythonApp() {
		new DeleteApplication(PYTHON_APP_NAME).perform();
	}
}
