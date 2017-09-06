/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.engine;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class EngineDownloadDialog extends DefaultShell {
	
	
	public EngineDownloadDialog(){
		super("Download Hybrid Mobile Engine");
	}
	
	public void ok(){
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
	public void cancel(){
		new CancelButton().click();
		new WaitWhile(new ShellIsAvailable(this));
	}
	
	public void selectEngine(String engine, String version){
		new DefaultTreeItem(engine,version).setChecked(true);
	}
	
	

}
