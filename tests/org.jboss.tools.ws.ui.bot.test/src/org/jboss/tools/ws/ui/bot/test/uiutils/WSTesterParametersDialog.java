/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.uiutils;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Dialog is invoked when there is a need to set jaxrs parameters
 * 
 * @author jjankovi
 *
 */
public class WSTesterParametersDialog {

	public static final String DIALOG_TITLE = "WS Tester: URL Parameters";
	
	private SWTBotShell shell = null;
	private SWTBot bot = null;
	
	private SWTBotTree mainTree;
	
	public WSTesterParametersDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
		mainTree = bot.tree();
	}
	
	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public void ok() {
		bot.button(IDELabel.Button.OK).click();
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public boolean isOkButtonEnabled() {
		return bot.button(IDELabel.Button.OK).isEnabled();
	}
	
	public boolean isCancelButtonEnabled() {
		return bot.button(IDELabel.Button.CANCEL).isEnabled();
	}
	
	public SWTBotTreeItem[] getAllParameters() {
		return mainTree.getAllItems();
	}
	
	public SWTBotTreeItem getParameter(String parameterName) {
		for (SWTBotTreeItem ti : getAllParameters()) {
			if (ti.cell(0).equals(parameterName)) {
				return ti;
			}
		}
		return null;
	}
	
	public SWTBotTreeItem getParameter(int index) {
		return mainTree.getAllItems()[index];
	}
	
	public String getParameterName(SWTBotTreeItem parameter) {
		return parameter.cell(0);
	}
	
	public String getParameterValue(SWTBotTreeItem parameter) {
		return parameter.cell(1);
	}
	
	public String getParameterType(SWTBotTreeItem parameter) {
		return parameter.cell(2);
	}
	
	public void setParameterValue(SWTBotTreeItem parameter, String value) {
		mainTree.select(parameter);
		parameter.click(1);
		bot.text().setText(value);
		parameter.click(0);
	}
	
}
