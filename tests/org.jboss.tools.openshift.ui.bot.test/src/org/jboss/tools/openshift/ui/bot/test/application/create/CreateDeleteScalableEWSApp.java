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

public class CreateDeleteScalableEWSApp {
	private final String EWS_APP_NAME = TestProperties
			.get("openshift.ewsapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties.get("openshift.ewsapp.name"));
	}

	@Test
	public void canCreateScalableEWSApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EWS, EWS_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableEWSApp() {
		new DeleteApplication(EWS_APP_NAME, OpenShiftLabel.AppType.JBOSS_EWS_TREE).perform();
	}
}
