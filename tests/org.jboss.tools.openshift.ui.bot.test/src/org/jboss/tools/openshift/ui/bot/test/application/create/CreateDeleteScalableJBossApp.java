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


public class CreateDeleteScalableJBossApp {

	private final String JBOSS_APP_NAME = TestProperties
			.get("openshift.jbossapp.name") + new Date().getTime();

	@Before
	public void cleanUpProject() {
		TestUtils.cleanupGitFolder(TestProperties
				.get("openshift.jbossapp.name"));
	}

	@Test
	public void canCreateScalableJBossApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.JBOSS_EAP, JBOSS_APP_NAME, true, true, true);
	}

	@After
	public void canDeleteScalableJBossApp() {
		new DeleteApplication(JBOSS_APP_NAME, OpenShiftLabel.AppType.JBOSS_EAP_TREE).perform();
	}
}
