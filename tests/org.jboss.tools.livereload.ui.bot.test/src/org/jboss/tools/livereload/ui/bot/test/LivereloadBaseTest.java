/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.livereload.ui.bot.test;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardHTMLPage;
import org.junit.BeforeClass;

public class LivereloadBaseTest {
	
	public static final String PROJECT_NAME="WebProject";
	
	@BeforeClass
	public static void prepareBase(){
		WorkbenchShell ws = new WorkbenchShell();
		if(!ws.isMaximized()){
			ws.maximize();
		}
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
	
	public void deployProjectToServer(String server, String projectName){
		ServersView sw = new ServersView();
		sw.open();
		ModifyModulesDialog md = sw.getServer(server).addAndRemoveModules();
		new ModifyModulesPage().add(projectName);
		md.finish();
		new WaitUntil(new ConsoleHasText("Deployed \""+projectName+".war\""),TimePeriod.LONG);
	}
	
	public void injectLivereload(String server, String projectName){
		ServersView sw = new ServersView();
		sw.open();
		sw.getServer(server).getModule(projectName).getLabel();
		new ContextMenu("Show In", "Web Browser on External Device...").select();
		Shell s =new DefaultShell("LiveReload");
		new YesButton().click();
		new WaitWhile(new ShellIsAvailable(s));
		new WaitWhile(new JobIsRunning());
		
		new DefaultShell("Open").close();
		new WaitWhile(new JobIsRunning());
	}

}
