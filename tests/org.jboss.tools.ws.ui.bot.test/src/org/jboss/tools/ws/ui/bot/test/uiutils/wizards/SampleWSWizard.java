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

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;

public class SampleWSWizard extends Wizard {

	private Type type;
	
	public SampleWSWizard(Type type) throws WidgetNotFoundException {
		super(new SWTBotExt().activeShell().widget);
		this.type = type;
	}

	public SampleWSWizard setProjectName(String name) {
		SWTBotCombo c = bot().comboBoxInGroup(JBossWSUIMessages.JBossWS_GenerateWizard_GenerateWizardPage_Project_Group);
		c.setSelection(name);
		return this;
	}

	public SampleWSWizard setServiceName(String name) {
		setText(JBossWSUIMessages.JBossWS_GenerateWizard_GenerateWizardPage_ServiceName_Label, name);
		return this;
	}

	public SampleWSWizard setPackageName(String name) {
		setText(JBossWSUIMessages.JBossWS_GenerateWizard_GenerateWizardPage_Package_Label, name);
		return this;
	}
	
	public SampleWSWizard setClassName(String name) {
		setText(JBossWSUIMessages.JBossWS_GenerateWizard_GenerateWizardPage_ClassName_Label, name);
		return this;
	}
	
	public SampleWSWizard setApplicationClassName(String name) {
		assert type == Type.REST;
		setText(JBossWSUIMessages.JBossRSGenerateWizardPage_Label_Application_Class_Name, name);
		return this;
	}
	
	public SampleWSWizard addRESTEasyLibraryFromRuntime() {
		assert type == Type.REST;
		//if server is AS, this checkbox is not enabled
		if (bot().checkBox(1).isEnabled()) {
			bot().checkBox(1).select();
		}
		return this;
	}
	
}
