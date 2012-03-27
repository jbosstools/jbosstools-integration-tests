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
package org.jboss.tools.ws.ui.bot.test.uiutils.wizards;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;

public class DynamicWebProjectWizard extends Wizard {

	public DynamicWebProjectWizard() {
		super(new SWTBot().activeShell().widget);
		assert "New Dynamic Web Project".equals(getText());
	}

	public DynamicWebProjectWizard setProjectName(String name) {
		setText("Project name:", name);
		return this;
	}
	
	public DynamicWebProjectWizard addProjectToEar(String earProject) {
		checkBoxSetChecked("&Add project to an EAR", true);
		setTextInCombobox("EAR project name:", earProject);
		return this;
	}
	
	private void checkBoxSetChecked(String checkBoxLabel, boolean checked) {
		SWTBotCheckBox ch = bot().checkBox(checkBoxLabel);
		ch.setFocus();
		if (checked) ch.select();
		else ch.deselect();
	}
	
	private void setTextInCombobox(String comboboxLabel, String valueToSet) {
		SWTBotCombo c = bot().comboBoxWithLabel(comboboxLabel);
		c.setFocus();
		c.setSelection(valueToSet);		
	}
	
}
