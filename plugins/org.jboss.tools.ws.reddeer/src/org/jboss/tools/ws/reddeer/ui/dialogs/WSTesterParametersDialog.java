/*******************************************************************************
 * Copyright (c) 2010-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.reddeer.ui.dialogs;

import java.util.List;

import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.ws.reddeer.helper.RedDeerHelper;

/**
 * Dialog is invoked when there is a need to set jaxrs parameters
 *
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class WSTesterParametersDialog {

	public static final String DIALOG_TITLE = "WS Tester: URL Parameters";

	private Shell dialog;
	private Tree mainTree;

	public WSTesterParametersDialog() {
		dialog = new DefaultShell(getDialogTitle());
		mainTree = new DefaultTree();
	}

	public static String getDialogTitle() {
		return DIALOG_TITLE;
	}

	public boolean isOpened() {
		return !dialog.getSWTWidget().isDisposed();
	}

	public void ok() {
		new OkButton().click();
	}

	public void cancel() {
		new CancelButton().click();
	}

	public boolean isOkButtonEnabled() {
		return new OkButton().isEnabled();
	}

	public boolean isCancelButtonEnabled() {
		return new CancelButton().isEnabled();
	}

	public List<TreeItem> getAllParameters() {
		return mainTree.getAllItems();
	}

	public TreeItem getParameter(String parameterName) {
		for (TreeItem ti : getAllParameters()) {
			if (ti.getCell(0).equals(parameterName)) {
				return ti;
			}
		}
		return null;
	}

	public TreeItem getParameter(int index) {
		return mainTree.getAllItems().get(index);
	}

	public String getParameterName(TreeItem parameter) {
		return parameter.getCell(0);
	}

	public String getParameterValue(TreeItem parameter) {
		return parameter.getCell(1);
	}

	public String getParameterType(TreeItem parameter) {
		return parameter.getCell(2);
	}

	public void setParameterValue(final TreeItem parameter, String value) {
		RedDeerHelper.click(parameter, 1);
		new DefaultText(0).setText(value);
		RedDeerHelper.click(parameter, 0);
	}
}
