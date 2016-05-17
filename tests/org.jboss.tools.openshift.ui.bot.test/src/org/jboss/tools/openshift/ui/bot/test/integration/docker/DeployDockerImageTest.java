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
package org.jboss.tools.openshift.ui.bot.test.integration.docker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolBar;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.reddeer.condition.BrowserContainsText;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftProjectExists;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.widget.ShellWithButton;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeployDockerImageTest {

	private static TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	public static final String HELLO_OS_DOCKER_IMAGE = "docker.io/openshift/hello-openshift";
	
	@BeforeClass
	public static void prepareDockerImage() {
		createDockerConnection();
		pullHelloImageIfDoesNotExist();
	}
	
	@After
	public void recreateProjects() {
		OpenShift3Connection connection  = new OpenShiftExplorerView().getOpenShift3Connection();
		connection.getProject().delete();
		
		new WaitWhile(new OpenShiftProjectExists(), TimePeriod.LONG);
		
		connection.createNewProject();
		
		new WaitUntil(new OpenShiftProjectExists(), TimePeriod.LONG);
		
		// Close browser if it is opened
		try {
			BrowserEditor browser = new BrowserEditor(new StringContains("hello"));
			browser.close();
		} catch (RedDeerException ex) {
			// do nothing, browser is not opened
		}
	}
	
	@Test
	public void testWizardDataHandlingOpenedFromOpenShiftExplorerTest() {
		selectAndVerifyProjectProcessingFromOpenShiftExplorer(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		selectAndVerifyProjectProcessingFromOpenShiftExplorer(DatastoreOS3.PROJECT2, null);
		selectAndVerifyProjectProcessingFromOpenShiftExplorer(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
	}
	
	@Test
	public void testWizardDataHandlingOpenedFromDockerExplorer() {
		selectAndVerifyDataProcessingFromDockerExplorer(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		selectAndVerifyDataProcessingFromDockerExplorer(DatastoreOS3.PROJECT2, null);		
		selectAndVerifyDataProcessingFromDockerExplorer(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
	}
	
	@Test
	public void testDeployDockerImageFromOpenShiftExplorer() {
		selectProject(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		new ContextMenu(OpenShiftLabel.ContextMenu.DEPLOY_DOCKER_IMAGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DEPLOY_IMAGE_TO_OPENSHIFT);
		
		new LabeledText(OpenShiftLabel.TextLabels.IMAGE_NAME).setText(HELLO_OS_DOCKER_IMAGE);
		
		proceedThroughDeployImageToOpenShiftWizard();
		
		verifyDeployedHelloWorldDockerImage();	
	}
		
	@Test
	public void testDeployDockerImageFromDockerExplorer() {
		selectProject(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		openDeployToOpenShiftWizardFromDockerExplorer();
		new LabeledCombo("OpenShift Project: ").setSelection(DatastoreOS3.PROJECT1_DISPLAYED_NAME + 
				" (" + DatastoreOS3.PROJECT1+ ")");

		proceedThroughDeployImageToOpenShiftWizard();
		
		verifyDeployedHelloWorldDockerImage();	
	}
	
	/**
	 * Verifies whether an application pod has been created and application is running successfully.
	 */
	private void verifyDeployedHelloWorldDockerImage() {
		try {
			new WaitUntil(new ResourceExists(Resource.POD, new StringContains("hello-openshift"),
				ResourceState.RUNNING), TimePeriod.getCustom(240));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be a running application pod for a deployed docker image, "
					+ "but it does not exist.");
		}
		
		new OpenShiftExplorerView().getOpenShift3Connection().getProject(
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).getOpenShiftResources(
						Resource.ROUTE).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		try {
			new WaitUntil(new BrowserContainsText("Hello OpenShift!"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Browser does not containg hello world content.");
		}
	}
	
	/**
	 * Proceeds through the image if the first wizard page has correct details -
	 * connection, project and image name.
	 */
	private void proceedThroughDeployImageToOpenShiftWizard() {
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL, false);
		
		assertTrue("Next button should be enabled if all details are set correctly",
				new NextButton().isEnabled());
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		if (!new CheckBox("Add Route").isChecked()) {
			new CheckBox("Add Route").click();
		}
		
		new FinishButton().click();
		
		new ShellWithButton("Deploy Image to OpenShift", "OK");
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Deploy Image to OpenShift"), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	/**
	 * Selects project in OpenShift explorer view, open Deploy Image to OpenShift wizard
	 * from its context menu and assert that there are correct values.
	 * @param projectName
	 * @param projectDisplayName
	 */
	private void selectAndVerifyProjectProcessingFromOpenShiftExplorer(String projectName, 
			String projectDisplayName) {
		selectProject(projectName, projectDisplayName);
		new ContextMenu(OpenShiftLabel.ContextMenu.DEPLOY_DOCKER_IMAGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DEPLOY_IMAGE_TO_OPENSHIFT);
		
		assertFalse("No project has been preselected.", new LabeledCombo("OpenShift Project: ").
				getSelection().equals(""));
		
		String preselected = projectDisplayName == null ? projectName : projectDisplayName + 
				" (" + projectName + ")";
		assertTrue("Wrong project has been preselected.", new LabeledCombo("OpenShift Project: ").
				getSelection().equals(preselected));
		
		closeWizard();
	}
	
	/**
	 * Select project in OpenShift explorer and open Deploy Image to OpenShift wizard
	 * from Docker Explorer and check data processing and close wizard in the end.
	 * @param projectName
	 * @param projectDisplayName
	 */
	private void selectAndVerifyDataProcessingFromDockerExplorer(String projectName,
			String projectDisplayName) {
		selectProject(projectName, projectDisplayName);
		openDeployToOpenShiftWizardFromDockerExplorer();
		selectProjectAndVerifyDataProcessingInDeployToOpenShiftWizard(projectName,
				projectDisplayName);
		closeWizard();
	}	
	
	/**
	 * Selects a specified project and verify it is correctly processed in Deploy Image
	 * to OpenShift wizard as well as processing of docker image details.
	 * 
	 * @param projectName
	 * @param projectDisplayName
	 */
	private void selectProjectAndVerifyDataProcessingInDeployToOpenShiftWizard(String projectName, 
			String projectDisplayName) {
		String preselected = projectDisplayName == null ? projectName : projectDisplayName + 
				" (" + projectName + ")";
		
		assertTrue("Wrong project has been preselected.", new LabeledCombo("OpenShift Project: ").
				getSelection().equals(preselected));
		
		assertTrue("Selected docker image should be used in wizard but it is not.",
				new LabeledText(OpenShiftLabel.TextLabels.IMAGE_NAME).getText().equals(
						HELLO_OS_DOCKER_IMAGE));
		
		assertTrue("Resource should be infered from image name but it is not",
				HELLO_OS_DOCKER_IMAGE.split("/")[2].equals(new LabeledText(OpenShiftLabel.
						TextLabels.RESOURCE_NAME).getText()));
	}
	
	/**
	 * Creates a new docker connection using autodetection. If this meant to be used 
	 * on OS systems where autodetection does not work, changes are necessary.
	 */
	private static void createDockerConnection() {
		View dockerExplorer = new WorkbenchView("Docker Explorer");
		dockerExplorer.open();
		ViewToolBar dockerToolBar = new ViewToolBar();
		new DefaultToolItem(dockerToolBar, "Add Connection").click();;
		
		new DefaultShell("New Docker Connection");
		
		// Here goes more magic if this is supposed to run on various systems where autodetect does not work
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("New Docker Connection"));
	}
	
	/**
	 * If hello world docker image does not exist, this method will pull it.
	 */
	private static void pullHelloImageIfDoesNotExist() {
		new WorkbenchView("Docker Explorer").activate();
		TreeItem imagesItem = new DefaultTree().getItems().get(0).getItem("Images");
		try {
			treeViewerHandler.getTreeItem(imagesItem, HELLO_OS_DOCKER_IMAGE);
		} catch (RedDeerException ex) {
			imagesItem.select();
			new ContextMenu("Pull...");
			
			new DefaultShell("Pull Image");
			new DefaultText().setText(HELLO_OS_DOCKER_IMAGE);
			new FinishButton().click();
			
			
			new WaitWhile(new ShellWithTextIsAvailable("Pull Image"));
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
	
	/**
	 * Opens a Deploy Image to OpenShift wizard from context menu of a docker image
	 */
	private void openDeployToOpenShiftWizardFromDockerExplorer() {
		new WorkbenchView("Docker Explorer").activate();
		
		TreeItem imagesItem = new DefaultTree().getItems().get(0).getItem("Images");
		treeViewerHandler.getTreeItem(imagesItem, HELLO_OS_DOCKER_IMAGE).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.DEPLOY_TO_OPENSHIFT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DEPLOY_IMAGE_TO_OPENSHIFT);
	}
	
	/**
	 * Closes Deploy Image to OpenShift wizard.
	 */
	private void closeWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DEPLOY_IMAGE_TO_OPENSHIFT));
	}
	
	/**
	 * Selects project in OpenShift explorer view.
	 * @param projectName
	 * @param projectDisplayName
	 */
	private void selectProject(String projectName, String projectDisplayName) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		String selectProject = projectDisplayName == null ? projectName : projectDisplayName;
		explorer.getOpenShift3Connection().getProject(selectProject).select();
	}
}
