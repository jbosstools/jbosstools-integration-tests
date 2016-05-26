/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.v3.create;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ProjectExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.application.v3.basic.TemplateParametersTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CreateApplicationFromTemplateTest {
	
	private String gitFolder = "jboss-eap-quickstarts";
	private String projectName = "jboss-kitchensink";
	
	private static final String TESTS_PROJECT = "org.jboss.tools.openshift.ui.bot.test";
	private static final String TESTS_PROJECT_LOCATION = System.getProperty("user.dir");
	private static final String URL = "https://raw.githubusercontent.com/jbosstools/"
			+ "jbosstools-integration-tests/master/tests/org.jboss.tools.openshift.ui.bot.test/"
			+ "resources/eap64-basic-s2i.json";
	
	private String genericWebhookURL;
	private String githubWebhookURL;
	
	private String srcRepoURI;
	private String applicationName;
	
	@BeforeClass
	public static void importTestsProject()  {		
		new ExternalProjectImportWizardDialog().open();
		new DefaultCombo().setText(TESTS_PROJECT_LOCATION);
		new PushButton("Refresh").click();
		
		new WaitUntil(new WidgetIsEnabled(new FinishButton()));
		
		new FinishButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ProjectExists(TESTS_PROJECT), TimePeriod.LONG);
	}
	
	@Before
	public void setUp() {
		TestUtils.cleanupGitFolder(gitFolder);
		if (new ProjectExists(projectName).test()) {
			new ProjectExplorer().getProject(projectName).delete(true);
		}
		genericWebhookURL = null;
		githubWebhookURL = null;
		srcRepoURI = null;
		applicationName = null;
	}
	
	@Test
	public void createApplicationFromLocalWorkspaceTemplate() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
		new DefaultTabItem(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).activate();
		new PushButton(OpenShiftLabel.Button.WORKSPACE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SELECT_OPENSHIFT_TEMPLATE);
		new DefaultTreeItem("org.jboss.tools.openshift.ui.bot.test", "resources", 
				"eap64-basic-s2i.json").select();
		new OkButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);		
		assertTrue("Template from workspace is not correctly shown in text field containing its path",
				new LabeledText(OpenShiftLabel.TextLabels.SELECT_LOCAL_TEMPLATE).getText().
					equals("${workspace_loc:" + File.separator + "org.jboss.tools.openshift.ui.bot.test"
							+ File.separator + "resources" + File.separator + "eap64-basic-s2i.json}"));

		new WaitUntil(new WidgetIsEnabled(new CancelButton()));
		
		assertTrue("Defined resource button should be enabled", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		completeApplicationCreationAndVerify();
	}
	
	@Test
	public void createApplicationFromLocalFileSystemTemplate() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
		new DefaultTabItem(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).activate();
		new LabeledText(OpenShiftLabel.TextLabels.SELECT_LOCAL_TEMPLATE).setText(
				TESTS_PROJECT_LOCATION + File.separator + "resources"
				+ File.separator + "eap64-basic-s2i.json");
		
		assertTrue("Defined resource button should be enabled", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		completeApplicationCreationAndVerify();
	}
	
	@Test
	public void createApplicationFromTemplateProvidedByURL() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
		new DefaultTabItem(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).activate();
		new LabeledText(OpenShiftLabel.TextLabels.SELECT_LOCAL_TEMPLATE).setText(URL);
		
		assertTrue("Defined resource button should be enabled", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		completeApplicationCreationAndVerify();
	}
	
	@Test
	public void testCreateApplicationFromServerTemplate() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		completeApplicationCreationAndVerify();
	}
	
	private void completeApplicationCreationAndVerify() {
		completeWizardAndVerify();
		importApplicationAndVerify();
		verifyCreatedApplication();
	}
	
	private void completeWizardAndVerify() {
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		String srcRepoRef = new DefaultTable().getItem(TemplateParametersTest.SOURCE_REPOSITORY_REF).getText(1);
		srcRepoURI = new DefaultTable().getItem(TemplateParametersTest.SOURCE_REPOSITORY_URL).getText(1);
		String contextDir = new DefaultTable().getItem(TemplateParametersTest.CONTEXT_DIR).getText(1);
		applicationName = new DefaultTable().getItem(TemplateParametersTest.APPLICATION_NAME).getText(1);
		new NextButton().click();
		
		new WaitWhile(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SUMMARY), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SUMMARY);
		
		assertTrue(TemplateParametersTest.SOURCE_REPOSITORY_REF + " is not same as the one shown in "
				+ "New OpenShift Application wizard.", new DefaultTable().getItem(
						TemplateParametersTest.SOURCE_REPOSITORY_REF).getText(1).equals(srcRepoRef));
		assertTrue(TemplateParametersTest.SOURCE_REPOSITORY_URL.split(" ")[0] + " is not same as the one shown in "
				+ "New OpenShift Application wizard.", new DefaultTable().getItem(
						TemplateParametersTest.SOURCE_REPOSITORY_URL.split(" ")[0]).getText(1).equals(srcRepoURI));
		assertTrue(TemplateParametersTest.CONTEXT_DIR + " is not same as the one shown in New OpenShift"
				+ " Application wizard.", new DefaultTable().getItem(TemplateParametersTest.CONTEXT_DIR).
					getText(1).equals(contextDir));
		assertTrue(TemplateParametersTest.APPLICATION_NAME.split(" ")[0] + " is not same as the one shown in "
				+ "New OpenShift Application wizard.", new DefaultTable().getItem(
						TemplateParametersTest.APPLICATION_NAME.split(" ")[0]).getText(1).equals(applicationName));
		assertFalse(TemplateParametersTest.GENERIC_SECRET.split(" ")[0] + " should be generated and non-empty.",
				new DefaultTable().getItem(TemplateParametersTest.GENERIC_SECRET.split(" ")[0]).getText(1).isEmpty());
		assertFalse(TemplateParametersTest.GITHUB_SECRET.split(" ")[0] + " should be generated and non-empty.", 
				new DefaultTable().getItem(TemplateParametersTest.GITHUB_SECRET.split(" ")[0]).getText(1).isEmpty());
		
		new DefaultLink("Click here to display the webhooks available to automatically trigger builds.").click();
		
		new DefaultShell(OpenShiftLabel.Shell.WEBHOOK_TRIGGERS);
		genericWebhookURL = new DefaultText(0).getText();
		githubWebhookURL = new DefaultText(1).getText();
		
		assertFalse("Generic webhook URL should not be empty.", genericWebhookURL.isEmpty());
		assertFalse("GitHub webhook URL should not be empty.", githubWebhookURL.isEmpty());
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.WEBHOOK_TRIGGERS));
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SUMMARY);
		new OkButton().click();
	}
	
	private void importApplicationAndVerify() {
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION));
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		new FinishButton().click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET), TimePeriod.LONG);
			
			new DefaultShell(OpenShiftLabel.Shell.CHEATSHEET);
			new CheckBox(0).click();
			new NoButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET));
		} catch (WaitTimeoutExpiredException ex) {
			// do nothing if cheat sheet is not provided
		}
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		assertTrue("Project Explorer should contain imported project kitchensink",
				projectExplorer.containsProject(projectName));
	}
	
	private void verifyCreatedApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		OpenShiftProject project = explorer.getOpenShift3Connection().getProject();
		project.refresh();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(120));
		new WaitUntil(new ResourceExists(Resource.BUILD_CONFIG), TimePeriod.LONG, false);
		
		List<OpenShiftResource> buildConfig = project.getOpenShiftResources(Resource.BUILD_CONFIG);
		assertTrue("There should be precisely 1 build config for created application, but there is following amount"
				+ " of build configs: " + buildConfig.size(), buildConfig.size() == 1);
		assertTrue("There should be application name and git URI in build config tree item, but they are not."
				+ "Application name is '" + applicationName + "' and git URI is '" + srcRepoURI + "', but build "
						+ "config has name '" + buildConfig.get(0).getName() + "'",
				buildConfig.get(0).getPropertyValue("Labels", "application").equals(applicationName) && 
				buildConfig.get(0).getPropertyValue("Source", "URI").equals(srcRepoURI));
		
		List<OpenShiftResource> imageStream = project.getOpenShiftResources(Resource.IMAGE_STREAM);
		assertTrue("There should be precisely 1 image stream for created application, but there is following amount"
				+ " of image streams: " + imageStream.size(), imageStream.size() == 1);
	
		List<OpenShiftResource> routes = project.getOpenShiftResources(Resource.ROUTE);
		assertTrue("There should be precisely 1 route for created application, but there is following amount"
				+ " of routes:" + routes.size(), routes.size() == 1);
		assertTrue("Generated (default) route should contain application name, but it's not contained.",
				routes.get(0).getName().equals(applicationName));
		
		List<OpenShiftResource> services = project.getOpenShiftResources(Resource.SERVICE);
		assertTrue("There should be precisely 1 service for created application, but there is following amount"
				+ " of services: " + services.size(), services.size() == 1);
	}
	
	@After
	public void tearDown() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection  = explorer.getOpenShift3Connection();
		connection.getProject().delete();
		
		connection.createNewProject();
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		if (projectExplorer.containsProject(projectName)) {
			projectExplorer.getProject(projectName).delete(true);
		}
	}
	
	@AfterClass
	public static void deleteTestsProjectFromWorkspace() {
		new ProjectExplorer().getProject(TESTS_PROJECT).delete(false);
	}
}
