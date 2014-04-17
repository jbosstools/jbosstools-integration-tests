package org.jboss.tools.openshift.ui.bot.test.application.create;

import java.util.Date;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateDeleteScalablePythonApp {

	private final String PYTHON_APP_NAME = TestProperties
			.get("openshift.pythonapp.name") + new Date().getTime();
	
	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.pythonapp.name"));
	}
	
	@Test
	public void canCreateScalablePythonApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.PYTHON, PYTHON_APP_NAME, true, true, true);
	}
	
	@After
	public void canDeleteScalablePythonApp() {
		new DeleteApplication(PYTHON_APP_NAME, OpenShiftLabel.AppType.PYTHON_TREE).perform();
	}
}
