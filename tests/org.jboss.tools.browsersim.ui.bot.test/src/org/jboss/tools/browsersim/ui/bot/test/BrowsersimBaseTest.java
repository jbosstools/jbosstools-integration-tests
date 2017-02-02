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

import java.rmi.RemoteException;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.reddeer.BrowserSimLauncher;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardHTMLPage;
import org.junit.AfterClass;

public class BrowsersimBaseTest {
	
	protected static IBrowsersimHandler bsHandler;
	public static final String PROJECT_NAME="WebProject";
	
	@AfterClass
	public static void stopBrowsersim(){
		BrowserSimLauncher bsController = new BrowserSimLauncher();
		bsController.stopBrowsersim();
	}
	
	public static void launchBrowsersim(ContextMenu menu) throws RemoteException{
		BrowserSimLauncher bsController = new BrowserSimLauncher();
		bsHandler = bsController.launchBrowserSim(menu);
		
		setProperBrowsersimLocation();
	}
	
	private static void setProperBrowsersimLocation() throws RemoteException{
		Point browserSimSize = bsHandler.getBrowsersimSize();
		WorkbenchShell ws = new WorkbenchShell();
		Rectangle area = Display.syncExec(new ResultRunnable<Rectangle>() {
			
			@Override
			public Rectangle run() {
				Rectangle area = Display.getDisplay().getPrimaryMonitor().getClientArea();
				ws.getSWTWidget().setSize(area.width-browserSimSize.x, area.height);
				ws.getSWTWidget().setLocation(0, 0);
				return area;
			}
		});
		bsHandler.setBrowsersimLocation(area.width-browserSimSize.x, area.height);
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
