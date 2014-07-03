package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.junit.After;
import org.junit.Test;

public class CreateQuickstart {

	private String quickstart = "CakePHP (Quickstart)";
	private String appName = "cookiesphp" + System.currentTimeMillis();
	
	@Test
	public void creatQuickstart() {
		new NewApplicationTemplates(false).
			createQuickstart(quickstart, appName, false, true, true, true);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(appName).perform();
	}
	
}
