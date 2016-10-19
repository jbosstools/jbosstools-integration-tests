/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.reddeer.BrowserSimController;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardHTMLPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BrowsersimBaseTest {
	
	protected static IBrowsersimHandler bsHandler;
	public static final String PROJECT_NAME="WebProject";
	
	@BeforeClass
	public static void maximize(){
		WorkbenchShell ws = new WorkbenchShell();
		if(!ws.isMaximized()){
			ws.maximize();
		}
	}
	
	@AfterClass
	public static void stopBrowsersim(){
		BrowserSimController bsController = new BrowserSimController();
		bsController.stopBrowsersim();
	}
	
	public static void launchBrowsersim(ContextMenu menu){
		BrowserSimController bsController = new BrowserSimController();
		bsHandler = bsController.launchBrowserSim(menu);
	}
	
	public String createProjectWithPage(){
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		new WebProjectFirstPage().setProjectName(PROJECT_NAME);
		ww.finish();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		
		NewHTMLFileWizardDialog nh = new NewHTMLFileWizardDialog();
		nh.open();
		NewHTMLFileWizardHTMLPage np = new NewHTMLFileWizardHTMLPage();
		String pageName = np.getFileName();
		nh.finish();
		return pageName;
	}

}
