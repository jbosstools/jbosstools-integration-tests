package org.jboss.tools.ws.reddeer.ui.wizards.jst.servlet;

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
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.ws.reddeer.ui.wizards.NewWizardDialogWithAssociatedPerspective;

/**
 * Dynamic Web Project wizard.
 *
 * Web > Dynamic Web Project
 *
 * @author Radoslav Rabara
 *
 */
public class DynamicWebProjectWizard extends NewWizardDialogWithAssociatedPerspective {

	public static String WIZARD_TITLE = "New Dynamic Web Project";

	/**
	 * Constructs Dynamic Web Project wizard.
	 */
	public DynamicWebProjectWizard() {
		super("Web", "Dynamic Web Project");
	}

	/**
	 * Sets the specified <var>name</var> as project name.
	 *
	 * @param name name to be as as project name
	 */
	public void setProjectName(String name) {
		new LabeledText(IDELabel.DynamicWebProjectWizard.PROJECT_NAME_LABEL)
				.setText(name);
	}

	/**
	 * Adds the project to the specified EAR project.
	 *
	 * @param earProjectName name of the EAR project, in which the project will
	 * 							be added
	 */
	public void addProjectToEar(String earProjectName) {
		setCheckBoxState(
				IDELabel.DynamicWebProjectWizard.ADD_PROJECT_TO_AN_EAR, true);
		setComboBoxText(IDELabel.DynamicWebProjectWizard.EAR_PROJECT_NAME,
				earProjectName);
	}

	/**
	 * Enables or disables the option of generation web.xml deployment descriptor.
	 *
	 * @param checked if it is <code>true</code> then the option will be enabled,
	 * 					otherwise the option will be disabled
	 */
	public void setGenerateDeploymentDescriptor(boolean checked) {
		setCheckBoxState(
				IDELabel.DynamicWebProjectWizard.GENERATE_DEPLOYMENT_DESCRIPTOR,
				checked);
	}

	private void setCheckBoxState(String checkBoxLabel, boolean checked) {
		new CheckBox(checkBoxLabel).toggle(checked);
	}

	private void setComboBoxText(String comboBoxLabel, String text) {
		new LabeledCombo(comboBoxLabel).setText(text);
	}
}
