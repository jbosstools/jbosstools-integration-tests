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

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;
import org.osgi.framework.Bundle;

public class SampleWSWizard extends Wizard {

	private static final Bundle WSUI_BUNDLE = Platform.getBundle("org.jboss.tools.ws.ui");
	
	public enum Type {
		SOAP, REST;
	
		public String getLabel() {
			switch (this) {
			case SOAP:
				return getStringFromBundle("%JBOSSWS_GENERATEACTION_LABEL");
			case REST:
				return getStringFromBundle("%restful.wizard.name");
			default:
				throw new IllegalArgumentException("Unknown type: " + this);
			}
		}
	}
	
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
	
	private static String getStringFromBundle(String key) {
		return Platform.getResourceString(WSUI_BUNDLE, key);
	}
}
