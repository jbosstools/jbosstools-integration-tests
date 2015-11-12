package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.TableContainsItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LabelsTest {
	
	@Before
	public void getToLabelsWizardPage() {
		new NewOpenShift3ApplicationWizard(DatastoreOS3.SERVER, DatastoreOS3.USERNAME,
				DatastoreOS3.PROJECT1_DISPLAYED_NAME).openWizardFromExplorer();
		new DefaultTree().selectItems(new DefaultTreeItem(OpenShiftLabel.Others.EAP_TEMPLATE));
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		new WaitWhile(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
	}
	
	@Test
	public void manageReadOnlyLabels() {
		new DefaultTable().getItem("template").select();
		assertFalse("Edit button should be disabled for read only labels",
				buttonIsEnabled(new PushButton(OpenShiftLabel.Button.EDIT)));
		assertFalse("Remove button should be disabled for read only labels",
				buttonIsEnabled(new PushButton(OpenShiftLabel.Button.REMOVE_BASIC)));
	}
	
	@Test
	public void testManageLabels() {
		String name = "label.name";
		String value = "label-value";
		createLabel(name, value);
		assertTrue("Label " + name + ":" + value + " should be present in the table, but its not there.",
				new TableContainsItem(new DefaultTable(), name, 0).test());
		
		new PushButton(OpenShiftLabel.Button.EDIT).click();
		
		new DefaultShell(OpenShiftLabel.Shell.RESOURCE_LABEL);
		assertTrue("Label name has not been shown in previous state in resource label dialog",
				new LabeledText(OpenShiftLabel.TextLabels.LABEL).getText().equals(name));
		assertTrue("Label value has not been shown in previous state in resource label dialog",
				new LabeledText(OpenShiftLabel.TextLabels.VALUE).getText().equals(value));
		
		// Edit an existing label
		name += "m";
		value += "m";
		new LabeledText(OpenShiftLabel.TextLabels.LABEL).setText(name);
		new LabeledText(OpenShiftLabel.TextLabels.VALUE).setText(value);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESOURCE_LABEL));
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		TableItem item = null;
		try {
			item = new DefaultTable().getItem(name);
		} catch (RedDeerException ex) {
			fail("Label name has not been modified successfully.");
		}
		assertTrue("Label value has not been modified successfully.", item.getText(1).equals(value));
		
		// Remove an existing label
		item.select();
		new PushButton(OpenShiftLabel.Button.REMOVE_BASIC).click();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_LABEL);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_LABEL));
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		assertFalse("Label should not be present in the resouce labels table, but it is.",
				new TableContainsItem(new DefaultTable(), name, 0).test());		
	}
	
	private void createLabel(String name, String value) {
		new PushButton(OpenShiftLabel.Button.ADD).click();
		
		new DefaultShell(OpenShiftLabel.Shell.RESOURCE_LABEL);
		new LabeledText(OpenShiftLabel.TextLabels.LABEL).setText(name);
		new LabeledText(OpenShiftLabel.TextLabels.VALUE).setText(value);
		
		new WaitUntil(new WidgetIsEnabled(new OkButton()));
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESOURCE_LABEL));
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
	}
	
	@Test
	public void testCreateDuplicativeLabel() {
		String name = "label";
		String value = "value";
		createLabel(name, value);
		try {
			createLabel(name, value);
			fail("It should not be possible to create duplicitive labels");
		} catch (RedDeerException ex) {
			// pass
		} finally {
			closeResourceLabelShell();
		}
	}
	
	@Test
	public void testCreateDuplicativeReadOnlyLabel() {
		try {
			createLabel("template", "noway");
			fail("It should not be possible to create duplicite labels of already existing read only label");
		} catch (RedDeerException ex) {
			// pass
		} finally {
			closeResourceLabelShell();
		}
	}	
	
	@Test 
	public void testBasicLabelValidation() {
		openResourceLabelShell();
		LabeledText name = new LabeledText(OpenShiftLabel.TextLabels.LABEL);
		LabeledText value = new LabeledText(OpenShiftLabel.TextLabels.VALUE);
		
		assertFalse("OK button should be disable if there is no name nor value for a new label.",
				buttonIsEnabled(new OkButton()));

		// Set valid label with all allowed character
		name.setText("valid.prefix/valid_Label-Name1");
		value.setText("valid.Label-value_1");
		assertTrue("OK button should be enabled for valid name and value, but it is not.",
				buttonIsEnabled(new OkButton()));
		
		name.setText("invalid.");
		value.setText("invalid.");
		assertFalse("OK button should be disable for invalid name and value, but it is not.",
				buttonIsEnabled(new OkButton()));
		
		closeResourceLabelShell();
	}
	
	@Test
	public void testLabelNameValidation() {
		openResourceLabelShell();
		
		setValidLabel();
		setInvalidName(".invalid");
		setValidLabel();
		setInvalidName("invalid.");
		setValidLabel();
		setInvalidName("-invalid");
		setValidLabel();
		setInvalidName("invalid-");
		setValidLabel();
		setInvalidName("_invalid");
		setValidLabel();
		setInvalidName("invalid_");
		setValidLabel();
		setInvalidName("{invalid}");
		setValidLabel();
		setInvalidName("prefix//invalid");
		setValidLabel();
		setInvalidName("0123456789012345678901234567890123456789012345678901234567890123");
		setValidLabel();
		setInvalidName("01234567890123456789012345678901234567890123456789" + ""
				+ "01234567890123456789012345678901234567890123456789"
				+ "01234567890123456789012345678901234567890123456789"
				+ "01234567890123456789012345678901234567890123456789"
				+ "012345678901234567890123456789012345678901234567890123"
				+ "/012345678901234567890123456789012345678901234567890123456789012");
	
		closeResourceLabelShell();
	}
	
	@Test
	public void testLabelValueValidation() {
		openResourceLabelShell();
		
		setValidLabel();
		setInvalidValue(".invalid");
		setValidLabel();
		setInvalidValue("invalid.");
		setValidLabel();
		setInvalidValue("-invalid");
		setValidLabel();
		setInvalidValue("invalid-");
		setValidLabel();
		setInvalidValue("_invalid");
		setValidLabel();
		setInvalidValue("invalid_");
		setValidLabel();
		setInvalidValue("{invalid}");
		setValidLabel();
		setInvalidValue("invalid/invalid");
		setValidLabel();
		setInvalidValue("0123456789012345678901234567890123456789012345678901234567890123");
		setValidLabel();
		
		closeResourceLabelShell();
	}
	
	private void setInvalidName(String name) {
		new LabeledText(OpenShiftLabel.TextLabels.LABEL).setText(name);
		assertFalse("OK button should be disable for invalid name, but it is not.",
				buttonIsEnabled(new OkButton()));
	}
	
	private void setInvalidValue(String value) {
		new LabeledText(OpenShiftLabel.TextLabels.VALUE).setText(value);
		assertFalse("OK button should be disable for invalid value, but it is not.",
				buttonIsEnabled(new OkButton()));
	}
	
	private void setValidLabel() {
		new LabeledText(OpenShiftLabel.TextLabels.LABEL).setText("valid");
		new LabeledText(OpenShiftLabel.TextLabels.VALUE).setText("valid");
		assertTrue("OK button should be enabled for valid name and value, but it is not.",
				buttonIsEnabled(new OkButton()));
	}
	
	private boolean buttonIsEnabled(Button button) {
		try {
			new WaitUntil(new WidgetIsEnabled(button), TimePeriod.getCustom(2));
			return true;
		} catch (WaitTimeoutExpiredException ex) {
			return false;
		}
	}
	
	private void openResourceLabelShell() {
		new PushButton(OpenShiftLabel.Button.ADD).click();
		new DefaultShell(OpenShiftLabel.Shell.RESOURCE_LABEL);
	}
	
	private void closeResourceLabelShell() {
		if (new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESOURCE_LABEL).test()) {
			new CancelButton().click();
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		}
	}
	
	@After
	public void closeNewApplicationWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
}
