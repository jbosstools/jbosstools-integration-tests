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
package org.jboss.tools.ws.ui.bot.test.widgets;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.ui.messages.JBossWSUIMessages;

public class SelectWSDLDialog extends SWTBotShell {

	public SelectWSDLDialog(Shell shell) throws WidgetNotFoundException {
		super(shell);
		//Select WSDL
		assert JBossWSUIMessages.WSDLBrowseDialog_Dialog_Title.equals(getText());
	}

	public void openURL() {
		//URL...
		bot().button(JBossWSUIMessages.WSDLBrowseDialog_URL_Browse).click();
	}

	public void setURI(String s) {
		//WSDL URI:
		bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_WSDL_URI_Field).setText(s);
	}

	public String getURI() {
		//WSDL URI:
		return bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_WSDL_URI_Field).getText();
	}

	public List<String> getServices() {
		//Service:
		return getItems(JBossWSUIMessages.WSDLBrowseDialog_Service_Field);
	}

	public void selectService(String service) {
		//Service:
		bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_Service_Field).setSelection(service);
	}

	public List<String> getPorts() {
		//Port:
		return getItems(JBossWSUIMessages.WSDLBrowseDialog_Port_Field);
	}

	public void selectPort(String port) {
		//Service:
		bot().comboBoxWithLabel(JBossWSUIMessages.WSDLBrowseDialog_Port_Field).setSelection(port);
	}

	public List<String> getOperations() {
		return Arrays.asList(getOperationsList().getItems());
	}

	public void selectOperation(String op) {
		getOperationsList().select(op);
	}

	public void ok() {
		new PushButton(IDELabel.Button.OK).click();
		
		// workaround for https://issues.jboss.org/browse/JBIDE-14618
		try {
			new WaitUntil(new ShellWithTextIsActive("Progress Information"));
			new WaitWhile(new ShellWithTextIsActive("Progress Information"));
		} catch (WaitTimeoutExpiredException sle) {
			// WISE call was pretty quick - no progress information dialog appears
		}
		
		// when replacing some existing WS message, press OK to confirm
		try {
			new DefaultShell("Message May Be Incorrect for Selected WSDL");
			new PushButton(IDELabel.Button.YES).click();;
		} catch (SWTLayerException sle) {
			// no WS message replacing - no dialog appeared
		}
	}

	private List<String> getItems(String label) {
		String[] items = bot().comboBoxWithLabel(label).items();
		return Arrays.asList(items);
	}

	private SWTBotList getOperationsList() {
		//Operation:
		return bot().listWithLabel(JBossWSUIMessages.WSDLBrowseDialog_Operation_Field);
	}
}