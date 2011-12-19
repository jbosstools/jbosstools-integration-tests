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
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.jboss.tools.ui.bot.ext.parts.SWTBotRadioExt;

/**
 * 
 * @author jjankovi
 *
 */
public class SimpleWSWizard extends Wizard {
	
	private Type type;
	
	public SimpleWSWizard(Type type) throws WidgetNotFoundException {
		super(new SWTBot().activeShell().widget);
		assert "Simple Web Service".equals(getText());
		this.type = type;	
		if (type == Type.REST) {
			new SWTBotRadioExt(bot().radio(0).widget).setSelection(false);
			new SWTBotRadioExt(bot().radio(1).widget).clickWithoutDeselectionEvent();
		}
	}
	
	public SimpleWSWizard setProjectName(String name) {
		SWTBotCombo c = bot().comboBox(0);
		c.setSelection(name);
		return this;
	}

	public SimpleWSWizard setServiceName(String name) {
		setText("Service name", name);
		return this;
	}

	public SimpleWSWizard setPackageName(String name) {
		setText("Package", name);
		return this;
	}
	
	public SimpleWSWizard setClassName(String name) {
		setText("Class", name);
		return this;
	}
	
	public SimpleWSWizard setApplicationClassName(String name) {
		assert type == Type.REST;
		setText("Application class", name);
		return this;
	}
	
	public SimpleWSWizard addRESTEasyLibraryFromRuntime() {
		assert type == Type.REST;
		//if server is AS, this checkbox is not enabled
		if (bot().checkBox(1).isEnabled()) {
			bot().checkBox(1).select();
		}
		return this;
	}
	
}
