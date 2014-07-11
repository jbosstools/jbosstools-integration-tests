package org.jboss.tools.ws.reddeer.ui.wizards;

/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class DynamicWebProjectWizard extends NewWizardDialog {
	
	@Override
	protected String getDialogTitle() {
		return "New Dynamic Web Project";
	}
	
	public DynamicWebProjectWizard() {
		super("Web Services", "Dynamic Web Project");
	}

	public void setProjectName(String name) {
		new LabeledText("Project name:").setText(name);
	}
	
	public DynamicWebProjectWizard addProjectToEar(String earProject) {
		setCheckBoxState("Add project to an EAR", true);
		setComboBoxText("EAR project name:", earProject);
		return this;
	}
	
	public DynamicWebProjectWizard generateDeploymentDescriptor() {
		setCheckBoxState("Generate web.xml deployment descriptor", true);
		return this;
	}
	
	private void setCheckBoxState(String checkBoxLabel, boolean checked) {
		new CheckBox(checkBoxLabel).toggle(checked);
	}
	
	private void setComboBoxText(String comboBoxLabel, String text) {
		new LabeledCombo(comboBoxLabel).setText(text);
	}
	
	@Override
	public void finish() {
		String shellText = new DefaultShell().getText();
		
		new PushButton("Finish").click();
		
		closeOpenAssociatedPerspectiveDialog();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void closeOpenAssociatedPerspectiveDialog() {
		WaitCondition condition = new ShellWithTextIsActive("Open Associated Perspective?");
		new WaitUntil(condition, TimePeriod.NORMAL, false);
		if(condition.test()) {
			CheckBox checkbox = new CheckBox("Remember my decision");
			if(!checkbox.isChecked()) {
				checkbox.click();
			}
			new PushButton("No").click();
		}
	}
}
