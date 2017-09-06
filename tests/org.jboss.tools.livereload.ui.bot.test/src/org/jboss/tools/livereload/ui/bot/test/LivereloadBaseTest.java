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

import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServerModule;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerPublishState;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardDialog;
import org.jboss.tools.jst.reddeer.wst.html.ui.wizard.NewHTMLFileWizardHTMLPage;
import org.junit.BeforeClass;

@CleanWorkspace
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
		new WebProjectFirstPage(ww).setProjectName(PROJECT_NAME);
		ww.finish();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		
		NewHTMLFileWizardDialog nh = new NewHTMLFileWizardDialog();
		nh.open();
		NewHTMLFileWizardHTMLPage np = new NewHTMLFileWizardHTMLPage(nh);
		String pageName = np.getFileName();
		nh.finish();
		return pageName;
	}
	
	public void deployProjectToServer(String server, String projectName){
		ServersView2 sw = new ServersView2();
		sw.open();
		ModifyModulesDialog md = sw.getServer(server).addAndRemoveModules();
		new ModifyModulesPage(md).add(projectName);
		md.finish();
		new WaitUntil(new ConsoleHasText("Deployed \""+projectName+".war\""),TimePeriod.LONG);
	}
	
	public void deployProjectToRemoteServer(String server, String projectName){
		ServersView2 sw = new ServersView2();
		sw.open();
		ModifyModulesDialog md = sw.getServer(server).addAndRemoveModules();
		new ModifyModulesPage(md).add(projectName);
		md.finish();
		new WaitUntil(new ModuleIsDeployed(server, projectName), TimePeriod.LONG);
	}
	
	public void injectLivereload(String server, String projectName){
		ServersView2 sw = new ServersView2();
		sw.open();
		sw.getServer(server).getModule(projectName).getLabel();
		new ContextMenuItem("Show In", "Web Browser on External Device...").select();
		Shell s =new DefaultShell("LiveReload");
		new YesButton().click();
		new WaitWhile(new ShellIsAvailable(s));
		new WaitWhile(new JobIsRunning());		
		new DefaultShell("Open").close();
		new WaitWhile(new JobIsRunning());
	}
	
	private class ModuleIsDeployed extends AbstractWaitCondition {
		
		private String server;
		private String module;
		
		public ModuleIsDeployed(String server, String module) {
			this.server = server;
			this.module = module;
		}

		@Override
		public boolean test() {
			ServersView2 sw = new ServersView2();
			sw.open();
			List<ServerModule> modules = sw.getServer(server).getModules();
			if(modules == null || modules.isEmpty()) {
				return false;
			}
			for(ServerModule m: modules) {
				return m.getLabel().getName().equals(module) && m.getLabel().getPublishState().equals(ServerPublishState.SYNCHRONIZED);
			}
			return false;
			
		}
		
	}

}
