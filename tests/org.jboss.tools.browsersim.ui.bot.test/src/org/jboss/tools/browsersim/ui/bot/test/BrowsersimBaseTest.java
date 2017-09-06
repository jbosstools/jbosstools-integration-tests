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
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.html.ui.wizard.NewHTMLFileWizardPage;
import org.eclipse.reddeer.eclipse.wst.html.ui.wizard.NewHTMLWizard;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.reddeer.BrowserSimLauncher;
import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.junit.AfterClass;

public class BrowsersimBaseTest {
	
	protected static IBrowsersimHandler bsHandler;
	public static final String PROJECT_NAME="WebProject";
	
	@AfterClass
	public static void stopBrowsersim(){
		BrowserSimLauncher bsController = new BrowserSimLauncher();
		bsController.stopBrowsersim();
	}
	
	public static void launchBrowsersim(ContextMenuItem menu) throws RemoteException{
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
		new WebProjectFirstPage(ww).setProjectName(PROJECT_NAME);
		ww.finish();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		
		NewHTMLWizard nh = new NewHTMLWizard();
		nh.open();
		NewHTMLFileWizardPage np = new NewHTMLFileWizardPage(nh);
		String pageName = np.getFileName();
		nh.finish();
		return pageName;
	}

}