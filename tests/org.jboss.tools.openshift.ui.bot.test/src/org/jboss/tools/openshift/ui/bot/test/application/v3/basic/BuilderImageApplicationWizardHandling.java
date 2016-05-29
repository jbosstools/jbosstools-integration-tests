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
package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.spinner.DefaultSpinner;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BuilderImageApplicationWizardHandling {

	@Before
	public void openNewApplicationWizard() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
	}
	
	@Test
	public void testCheckButtonsStateForBuildImage() {
		selectBuilderImageAndAssertButtonsAvailability();
		
		new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE).select();
		
		assertTrue("Next button should be enabled if EAP template was selected after build image.",
				new NextButton().isEnabled());
		assertTrue("Finish button should be enabled if template is selected after build image.",
				new FinishButton().isEnabled());
		
		selectBuilderImageAndAssertButtonsAvailability();
	}
	
	private void selectBuilderImageAndAssertButtonsAvailability() {
		new DefaultTreeItem(OpenShiftLabel.Others.EAP_BUILDER_IMAGE).select();
		
		assertTrue("Next button should be enabled if EAP builder image is selected.",
				new NextButton().isEnabled());
		assertFalse("Finish button should be disabled on first wizard page for builder images.",
				new FinishButton().isEnabled());
	}
	
	
	private void nextToBuildConfigurationWizardPage() {
		new DefaultTreeItem(OpenShiftLabel.Others.EAP_BUILDER_IMAGE).select();
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()));
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()));
	}
	
	private void next() {		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()));
	}
	
	@Test
	public void testResourceNameOnBuildConfigurationWizardPage() {
		nextToBuildConfigurationWizardPage();
		
		LabeledText resourceName = new LabeledText(OpenShiftLabel.TextLabels.BUILDER_RESOURCE_NAME);
		String defaultName = "jboss-eap64-openshift";
		
		assertTrue("Resource name has not been inferred correctly.", 
				resourceName.getText().equals(defaultName));
		
		validateInvalidResourceName("");
		validateInvalidResourceName(" ");
		validateInvalidResourceName("invAlid");
		validateValidResourceName("valid-name");
		validateInvalidResourceName("-invalid");
		validateValidResourceName("val1d-n4me-r3source");
		validateInvalidResourceName("invalid-");
		validateValidResourceName(defaultName);	
	}
	
	private void validateInvalidResourceName(String name) {
		new LabeledText(OpenShiftLabel.TextLabels.BUILDER_RESOURCE_NAME).setText(name);
		
		assertFalse("Next button should be disabled if resource name is invalid '" + name + "'",
				new NextButton().isEnabled());
		assertFalse("Finish button should be disabled if resource name is invalid '" + name + "'",
				new FinishButton().isEnabled());
	}
	
	private void validateValidResourceName(String name) {
		new LabeledText(OpenShiftLabel.TextLabels.BUILDER_RESOURCE_NAME).setText(name);
		
		assertTrue("Next button should be enabled if resource name is valid '" + name + "'",
				new NextButton().isEnabled());
		assertTrue("Finish button should be enabled if resource name is valid '" + name + "'",
				new FinishButton().isEnabled());
	}
	
	@Test
	public void testGitFieldsValidationOnBuildConfigurationWizardPage() {
		nextToBuildConfigurationWizardPage();
		
		LabeledText gitRepo = new LabeledText(OpenShiftLabel.TextLabels.GIT_REPO_URL);
		LabeledText gitReference = new LabeledText(OpenShiftLabel.TextLabels.GIT_REF);
		LabeledText contextDirectory = new LabeledText(OpenShiftLabel.TextLabels.CONTEXT_DIR);
		
		String defaultRepo = gitRepo.getText();
		String defaultRef = gitReference.getText();
		String defaultContextDir = contextDirectory.getText();
		
		validateGitRepoURL("invalid");
		validateGitRepoURL("");
		
		setDefaultValuesAndAssert(defaultRepo, defaultRef, defaultContextDir);
		
		gitReference.setText("");
		contextDirectory.setText("");
		
		assertTrue("Next button should be enabled if ref and context dir are empty.",
				new NextButton().isEnabled());
		assertTrue("Finish button should be enabled if ref and context dir are empty.",
				new FinishButton().isEnabled());
		
		setDefaultValuesAndAssert(defaultRepo, defaultRef, defaultContextDir);
		
		validateGitReference("invalid reference");
		validateGitReference("invalidad*reference");
		validateGitReference("invalid:reference");
		validateGitReference("@");
		validateGitReference("invalid\reference");
		
		setDefaultValuesAndAssert(defaultRepo, defaultRef, defaultContextDir);
	}
	
	private void setDefaultValuesAndAssert(String defaultRepo, String defaultRef, String defaultContextDir) {
		new LabeledText(OpenShiftLabel.TextLabels.GIT_REPO_URL).setText(defaultRepo);
		new LabeledText(OpenShiftLabel.TextLabels.GIT_REF).setText(defaultRef);
		new LabeledText(OpenShiftLabel.TextLabels.CONTEXT_DIR).setText(defaultContextDir);
		
		assertTrue("Next button should be enabled after setting git values to default.",
				new NextButton().isEnabled());
		assertTrue("Finish button should be enabled after setting git values to default.",
				new FinishButton().isEnabled());
	}
		
	private void validateGitRepoURL(String url) {
		new LabeledText(OpenShiftLabel.TextLabels.GIT_REPO_URL).setText("url");
		
		assertFalse("Next button should be disabled if git repo URL is invalid",
				new NextButton().isEnabled());
		assertFalse("Finish button should be disabled if git repo URL is invalid",
				new FinishButton().isEnabled());
	}
	
	private void validateGitReference(String ref) {
		new LabeledText(OpenShiftLabel.TextLabels.GIT_REF).setText("ref");
		
		assertFalse("Next button should be disabled if git reference is invalid",
				new NextButton().isEnabled());
		assertFalse("Finish button should be disabled if git reference is invalid",
				new FinishButton().isEnabled());
	}
	
	@Test
	public void testManageCustomEnvironmentVariableValidationOnDeploymentConfigurationWizardPage() {
		nextToBuildConfigurationWizardPage();
		next();
		
		String varName = "varname1";
		String varValue = "varvalue1";
		String varValue2 = "varvalue2";
		
		assertFalse("Edit button should be disabled if no environmnent variable is selected.",
				new PushButton(OpenShiftLabel.Button.EDIT).isEnabled());
		assertFalse("Remove button should be disabled if there is no variable selected.",
				new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).isEnabled());
		assertFalse("Reset button should be disabled if there is no change performed on environment variable.",
				new PushButton(OpenShiftLabel.Button.RESET).isEnabled());
		assertFalse("Reset All button should be disabled if there is no change performed on environment variables list.",
				new PushButton(OpenShiftLabel.Button.RESET_ALL).isEnabled());
		assertFalse("There should environment variables obtained from image metadata.",
				new DefaultTable().getItems().isEmpty());
		
	 	addEnvVar(varName, varValue);
		new DefaultTable().getItem(varName).select();
		
		assertTrue("Remove button should be enabled if environment variable is selected.",
				new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).isEnabled());
		
		editEnvVar(varName, varValue, varValue2);
		removeEnvVar(varName);
	}
	
	@Test
	public void testManageDefaultEnvironmentVariable() {
		nextToBuildConfigurationWizardPage();
		next();
		
		String varName = "HOME";
		String varValue = "/home/jboss";
		String newVarValue = "/home/jbosstools";
		
		new DefaultTable().select(varName);
		
		assertFalse("Remove button should be disabled for read only variables",
				new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).isEnabled());
		
		editAndResetEnvVar(varName, varValue, newVarValue, OpenShiftLabel.Button.RESET);
		editAndResetEnvVar(varName, varValue, newVarValue, OpenShiftLabel.Button.RESET_ALL);
	}
	
	private void addEnvVar(String name, String value) {
		new PushButton(OpenShiftLabel.Button.ADD).click();
		
		new DefaultShell(OpenShiftLabel.Shell.ENVIRONMENT_VARIABLE);
		
		new LabeledText("Name:").setText(name);
		new LabeledText("Value:").setText(value);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ENVIRONMENT_VARIABLE));
		
		assertTrue("Table does not contain added environment variable",
				new DefaultTable().containsItem(name, 0));
	}
	
	private void editEnvVar(String name, String oldValue, String newValue) {
		new DefaultTable().getItem(name).select();
		
		assertTrue("Edit button should be enabled if environment variable is selected.",
				new PushButton(OpenShiftLabel.Button.EDIT).isEnabled());
		
		new PushButton(OpenShiftLabel.Button.EDIT).click();
		
		new DefaultShell(OpenShiftLabel.Shell.ENVIRONMENT_VARIABLE);
		
		assertTrue("Environment variable name should be filled when editing an environment variable.",
				new LabeledText("Environment Variable:").getText().equals(name));
				//new LabeledText("Name:").equals(varName));
		assertTrue("Environment variable value should be filled when editing an environment variable.",
				new LabeledText("Value:").getText().equals(oldValue));
		
		new LabeledText("Value:").setText(newValue);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ENVIRONMENT_VARIABLE));
		
		assertTrue("Table does not contain modified environment variable. Value is not shown correctly.",
				new DefaultTable().getItem(name).getText(1).equals(newValue));
	}
	
	private void removeEnvVar(String name) {
		new DefaultTable().select(name);
		new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).click();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_ENV_VAR);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_ENV_VAR));
		
		assertFalse("Environment variable should no longer be present in table.",
				new DefaultTable().containsItem(name));
	}
	
	private void editAndResetEnvVar(String varName, String oldValue, String newValue, String resetButton) {
		editEnvVar(varName, oldValue, newValue);
		new DefaultTable().getItem(varName).select();
		
		assertFalse("Remove button should be disabled if variable is read only.",
				new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).isEnabled());
		assertTrue("Reset button should be enabled if variable is modified.",
				new PushButton(OpenShiftLabel.Button.RESET).isEnabled());
		assertTrue("Reset All button should be enabled if variable is modified.",
				new PushButton(OpenShiftLabel.Button.RESET_ALL).isEnabled());
		
		new PushButton(resetButton).click();
		
		new DefaultShell(OpenShiftLabel.Shell.RESET_ENV_VAR);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESET_ENV_VAR));
		
		assertTrue("Variable was not reset successfully.",
				new DefaultTable().getItem(varName).getText(1).equals(oldValue));
	}
	
	@Test
	public void testPorts() {
		nextToBuildConfigurationWizardPage();
		next();
		next();
	
		new PushButton(OpenShiftLabel.Button.ADD).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SERVICE_PORTS);
		new DefaultSpinner(OpenShiftLabel.TextLabels.SERVICE_PORT).setValue(6666);
		new LabeledText(OpenShiftLabel.TextLabels.POD_PORT).setText("9999");
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SERVICE_PORTS));
		
		assertTrue("Table should containg new port mapping.", new DefaultTable().containsItem("6666-tcp"));
		assertTrue("New pod mapping has incorrect mapped ports.", new DefaultTable().getItem("6666-tcp").getText(1)
				.equals("6666") && new DefaultTable().getItem("6666-tcp").getText(2).equals("9999"));
		
		new DefaultTable().select("6666-tcp");
		new PushButton(OpenShiftLabel.Button.EDIT).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SERVICE_PORTS);
		new LabeledText(OpenShiftLabel.TextLabels.POD_PORT).setText("6666");
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SERVICE_PORTS));
		
		assertTrue("Modified pod mapping has incorrect mapped ports.", new DefaultTable().getItem("6666-tcp").getText(1)
				.equals("6666") && new DefaultTable().getItem("6666-tcp").getText(2).equals("6666"));
		
		new DefaultTable().select("8080-tcp");
		new PushButton(OpenShiftLabel.Button.REMOVE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_PORT);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_PORT));
		
		assertFalse("Table should no longer contain removed mapped ports.",
				new DefaultTable().containsItem("8080-tcp"));
		assertTrue("Reset button should be enabled if default ports has been changed or removed.",
				new PushButton(OpenShiftLabel.Button.RESET).isEnabled());
		
		new PushButton(OpenShiftLabel.Button.RESET).click();
		
		new DefaultShell(OpenShiftLabel.Shell.RESET_PORTS);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESET_PORTS));
		
		assertTrue("There should previously removed port mapping in the table after reset.",
				new DefaultTable().containsItem("8080-tcp"));
	}

	@After
	public void closeNewApplicationWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
}
