/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.common.model.ui;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
/**
 * Represents dialog invoked when manipulating in JBT beans.xml editor
 * with include/exclude property
 * 
 * @author jjankovi
 *
 */
public class AddIfSystemPropertyDialog extends DefaultShell {

	public AddIfSystemPropertyDialog() {
		super("Add If System Property");
	}
	
	public void setName(String name) {
		/*
		 * Text labeled "Name:*"; no direct common parent -> LabeledText can't be used
		 */
		new DefaultText(0).setText(name);
	}
	
	public void setValue(String value) {
		/*
		 * Text labeled "Value:"; no direct common parent -> LabeledText can't be used
		 */
		new DefaultText(1).setText(value);
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
}
