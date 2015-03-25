package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.test.ui.reddeer.projects.AngularJSForge;
import org.jboss.tools.central.test.ui.reddeer.projects.HTML5Project;
import org.jboss.tools.central.test.ui.reddeer.projects.JavaEEWebProject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@JBossServer(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class ArchetypesTest {

	private static Map<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarnings = new HashMap<org.jboss.tools.central.reddeer.projects.Project, List<String>>();
	
	@InjectRequirement
	ServerRequirement req;

	@BeforeClass
	public static void setup() {
		String mvnConfigFileName = new File("target/classes/settings.xml").getAbsolutePath();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage();
		preferenceDialog.select(prefPage);
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		preferenceDialog.ok();
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@After
	public void teardown() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		for (Project p : new ProjectExplorer().getProjects()) {
			p.delete(true);
		}
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}
	
	@AfterClass
	public static void teardownClass() {
		StringBuilder sb = new StringBuilder();
		boolean fail = false;
		for (Entry<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarning : projectWarnings
				.entrySet()) {
			sb.append(projectWarning.getKey().getName() + "\n\r");
			if (!projectWarning.getValue().isEmpty()) fail = true;
			for (String warning : projectWarning.getValue()) {
				sb.append("\t" + warning + "\n\r");
			}
		}
		projectWarnings.clear();
		assertFalse(sb.toString(), fail);
	}
	
	@Test
	public void HTML5ProjectTest() {
		ArchetypeProject project = new HTML5Project();
		importArchetypeProject(project);
	}
	
	@Test
	public void AngularJSForgeTest(){
		ArchetypeProject project = new AngularJSForge();
		importArchetypeProject(project);
	}

	@Test
	public void JavaEEWebProjectTest() {
		importArchetypeProject(new JavaEEWebProject(false));
	}

	@Test
	public void JavaEEWebProjectBlankTest() {
		importArchetypeProject(new JavaEEWebProject(true));
	}

//	@Test
//	public void HybridMobileTest(){
//		try{
//			new DefaultHyperlink("Hybrid Mobile Project").activate();
//			new WaitUntil(new ShellWithTextIsActive("Information"));
//			//HMT is not installed
//			new PushButton("No").click();
//		}catch(WaitTimeoutExpiredException ex){
//			//TODO check whether this is OK.
//			new DefaultShell().close();
//		}
//	}
	
	private void importArchetypeProject(ArchetypeProject project) {
		ExamplesOperator.getInstance().importArchetypeProject(project);
		ExamplesOperator.getInstance().deployProject(project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		ExamplesOperator.getInstance().checkDeployedProject(
				project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}
}
