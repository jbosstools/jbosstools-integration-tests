/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.preview.editor;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class EngineDialog extends DefaultShell{
	
	public EngineDialog(){
		super("Browser Engine");
	}
	
	public void stayWithHTML5(){
		new PushButton("Stay with HTML5").click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
	public void changeToJSF(){
		new PushButton("Change to JSF").click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
	public boolean isHTML5ButtonEnabled(){
		return new PushButton("Stay with HTML5").isEnabled();
	}
	
	public boolean isJSFButtonEnabled(){
		return new PushButton("Change to JSF").isEnabled();
	}

}
