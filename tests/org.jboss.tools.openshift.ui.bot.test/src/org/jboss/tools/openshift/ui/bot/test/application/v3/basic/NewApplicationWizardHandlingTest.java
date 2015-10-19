package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewApplicationWizardHandlingTest {

	@Before
	public void openNewApplicationWizard() {
		new NewOpenShift3ApplicationWizard(DatastoreOS3.SERVER, DatastoreOS3.USERNAME,
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).openWizardFromExplorer();
	}
	
	@Test
	public void testTemplatesRelatedWidgetAccess() {
		assertTrue("Server template selection should be chosen by default.",
				new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).isSelected());
		
		new RadioButton(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).click();
		
		assertFalse("Tree with server templates should be disabled if local template "
				+ "radio button is selected.", new DefaultTree().isEnabled());
		assertTrue("Browse button should be enabled while local template "
				+ "radio button is selected.", new PushButton(OpenShiftLabel.Button.BROWSE).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		
		assertTrue("Tree with server templates should be enabled if server template "
				+ "radio button is selected.", new DefaultTree().isEnabled());
		assertFalse("Browse button should be disabled while server template "
				+ "radio button is selected.", new PushButton(OpenShiftLabel.Button.BROWSE).isEnabled());
	}
	
	@Test
	public void testSwitchProject() {
		String project1Text = DatastoreOS3.PROJECT1_DISPLAYED_NAME + " (" + DatastoreOS3.PROJECT1 + ")";
		String project2Text = DatastoreOS3.PROJECT2;
		LabeledCombo projectCombo = new LabeledCombo(OpenShiftLabel.TextLabels.PROJECT);
		
		assertTrue(projectCombo.getText().equals(project1Text));
		assertTrue(projectCombo.getItems().contains(project2Text));
		
		projectCombo.setSelection(project2Text);
		projectCombo.setSelection(project1Text);
		projectCombo.setSelection(project2Text);
	}
	
	@Test
	public void testAccessibilityOfDefinedResourcesButton() {
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		
		assertFalse("Defines Resources button should be disabled if no server template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).click();
		
		assertFalse("Defines Resources button should be disabled if no local template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		assertTrue("Defines Resources button should be enabled if a server template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).click();
		
		assertFalse("Defines Resources button should be disabled if no local template is selected,"
				+ " but a server template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		new DefaultTree().unselectAllItems();
		new RadioButton(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).click();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new DefaultText().getSWTWidget().setText(DatastoreOS3.TEMPLATE_PATH);
			}
		});
		
		assertTrue("Defines Resources button should be enabled if a local template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
		
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		
		assertFalse("Defines Resources button should be disabled if no server template is selected,"
				+ " but a local template is selected.", 
				new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).isEnabled());
	}
	
	@Test
	public void testFilteringServerTemplates() {
		DefaultText searchBar = new DefaultText(1);
		
		searchBar.setText(OpenShiftLabel.Others.EAP_TEMPLATE);
		assertTrue("There should be precisely one tree item in a tree.",
				new DefaultTree().getItems().size() == 1);
		assertTrue("There should be item representing basic EAP template in a tree but it is not there.",
				new DefaultTree().getItems().get(0).getText().equals(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		searchBar.setText(OpenShiftLabel.Others.TOMCAT_TEMPLATE);
		assertTrue("There should be precisely one tree item in a tree.",
				new DefaultTree().getItems().size() == 1);
		assertTrue("There should be item representing basic Tomcate template in a tree but it is not there.",
				new DefaultTree().getItems().get(0).getText().equals(OpenShiftLabel.Others.TOMCAT_TEMPLATE));
		
		searchBar.setText("");
		assertTrue("There should be more templates if search bar does not contain any search query", 
				new DefaultTree().getItems().size() > 2);
	}
	
	@Test
	public void testShowDefinedResourcesForLocalTemplate() {
		new RadioButton(OpenShiftLabel.TextLabels.LOCAL_TEMPLATE).click();
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new DefaultText().getSWTWidget().setText(DatastoreOS3.TEMPLATE_PATH);
			}
		});
		
		verifyDefinedResourcesForTemplate();
	}
	
	@Test
	public void testShowDefinedResourcesForServerTemplate() {
		new RadioButton(OpenShiftLabel.TextLabels.SERVER_TEMPLATE).click();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		verifyDefinedResourcesForTemplate();
	}
	
	private void verifyDefinedResourcesForTemplate() {
		new WaitUntil(new WidgetIsEnabled(new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES)),
				TimePeriod.NORMAL);
		new PushButton(OpenShiftLabel.Button.DEFINED_RESOURCES).click();
		
		new DefaultShell(OpenShiftLabel.Shell.TEMPLATE_DETAILS);
		List<TreeItem> items = new DefaultTree().getItems();
		
		assertTrue("There should be build config item in tree describing resources", 
				items.get(0).getText().contains("BuildConfig"));
		assertTrue("There should be deployment config item in tree describing resources", 
				items.get(1).getText().contains("DeploymentConfig"));
		assertTrue("There should be image stream item in tree describing resources", 
				items.get(2).getText().contains("ImageStream"));
		assertTrue("There should be route item in tree describing resources", 
				items.get(3).getText().contains("Route"));
		assertTrue("There should be service item in tree describing resources", 
				items.get(4).getText().contains("Service"));
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.TEMPLATE_DETAILS), TimePeriod.NORMAL);
	}
	
	@After
	public void closeNewApplicationWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
}
