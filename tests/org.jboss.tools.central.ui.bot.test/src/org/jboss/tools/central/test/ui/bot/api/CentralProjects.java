package org.jboss.tools.central.test.ui.bot.api;

import org.jboss.reddeer.swt.impl.toolbar.WorkbenchToolItem;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.tools.central.test.ui.bot.CreateProjectsWithServerTest;

public class CentralProjects {

	public static void importArchetype(String projectName){
		openCentralPage();
		CreateProjectsWithServerTest createProjectsSuite = new CreateProjectsWithServerTest();
		createProjectsSuite.checkExample(null, projectName, false, false);
	}
	
	public static void importQuickstart(String category, String projectName){
		openCentralPage();
		CreateProjectsWithServerTest createProjectsSuite = new CreateProjectsWithServerTest();
		createProjectsSuite.importProjectExample(projectName, null, category);
	}
	
	
	private static void openCentralPage(){
		new WorkbenchToolItem("JBoss Central").click();
		new DefaultEditor("JBoss Central");
	}
}
