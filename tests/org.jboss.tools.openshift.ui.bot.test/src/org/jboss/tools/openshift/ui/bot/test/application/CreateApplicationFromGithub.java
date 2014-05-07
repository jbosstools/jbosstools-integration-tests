package org.jboss.tools.openshift.ui.bot.test.application;

import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of create application on a github template. Wordpress application
 * is chosen as a template, which is build upon a PHP basic cartridge with a MySQL embedded
 * cartridge.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateApplicationFromGithub {

	private static final String APP_NAME = "wordpressapp" + System.currentTimeMillis();
	private static final String URL = "https://github.com/openshift/wordpress-example";
	
	@Test
	public void createAppFromGithubTemplate() {
		new NewApplicationTemplates(false).createApplicationFromGithubTemplate(
				OpenShiftLabel.AppType.PHP, APP_NAME, URL, null, 
				OpenShiftLabel.Cartridge.MYSQL);
	}

	@After	
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
}
