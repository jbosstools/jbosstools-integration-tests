package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.reddeer.utils.CleanUpOS3;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.DeleteResourceTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.GithubWebhoookTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.ImportApplicationTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.LogsTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.PortForwardingTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.advanced.TriggerBuildTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.LabelsTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.NewApplicationWizardHandlingTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.OpenNewApplicationWizardTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.OpenNewApplicationWizardWithNoProjectTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.TemplateParametersTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.CreateApplicationFromTemplateTest;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.DeploymentTest;
import org.jboss.tools.openshift.ui.bot.test.common.OCBinaryLocationTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ConnectionDialogHandlingTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ConnectionPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.CreateNewConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.RemoveConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ShowConnectionInWebConsoleTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.StoreConnectionTest;
import org.jboss.tools.openshift.ui.bot.test.project.CreateNewProjectTest;
import org.jboss.tools.openshift.ui.bot.test.project.DeleteProjectTest;
import org.jboss.tools.openshift.ui.bot.test.project.ProjectNameValidationTest;
import org.jboss.tools.openshift.ui.bot.test.project.ProjectPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.project.ResourcesTest;
import org.jboss.tools.openshift.ui.bot.test.project.ShowProjectInWebConsoleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// General
	OCBinaryLocationTest.class,
	
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
	ResourcesTest.class,
	ProjectPropertiesTest.class,
	ShowProjectInWebConsoleTest.class,
	
	// Application wizard handling
	OpenNewApplicationWizardTest.class,
	OpenNewApplicationWizardWithNoProjectTest.class,
	NewApplicationWizardHandlingTest.class,
	TemplateParametersTest.class,
	LabelsTest.class,
	
	// Creation of a new application
	CreateApplicationFromTemplateTest.class,
	DeploymentTest.class,
	
	// Application handling
	DeleteResourceTest.class,
	TriggerBuildTest.class,
	ImportApplicationTest.class,
	GithubWebhoookTest.class,
	PortForwardingTest.class,
	LogsTest.class,
	
	// Clean up
	CleanUpOS3.class
})
public class OpenShift3BotTests {
	
}
