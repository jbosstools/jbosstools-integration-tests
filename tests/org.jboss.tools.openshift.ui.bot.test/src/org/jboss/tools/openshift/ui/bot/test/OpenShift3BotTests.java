package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ConnectionPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.CreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ConnectionDialogHandlingTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ShowConnectionInWebConsoleTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.StoreConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.project.CreateNewProjectTest;
import org.jboss.tools.openshift.ui.bot.test.project.DeleteProjectTest;
import org.jboss.tools.openshift.ui.bot.test.project.ProjectNameValidationTest;
import org.jboss.tools.openshift.ui.bot.test.project.ProjectPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.project.ShowProjectInWebConsoleTest;
import org.jboss.tools.openshift.ui.bot.test.util.CleanUpOS3;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// Connection
	CreateNewConnectionTest.class,
	RemoveConnectionTest.class,
	ConnectionDialogHandlingTest.class,
	StoreConnectionTest.class,
	ConnectionPropertiesTest.class,
	ShowConnectionInWebConsoleTest.class,
	
	// Project
	ProjectNameValidationTest.class,
	CreateNewProjectTest.class,
	DeleteProjectTest.class,
	ProjectPropertiesTest.class,
	ShowProjectInWebConsoleTest.class,
	
	// Clean up
	CleanUpOS3.class
})
public class OpenShift3BotTests {
	
}
