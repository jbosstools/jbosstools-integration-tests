package org.jboss.tools.central.test.ui.reddeer.examples.tests;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.jboss.tools.central.reddeer.projects.Project;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractExamplesTest {

	private static Map<Project, List<String>> projectWarnings = new HashMap<Project, List<String>>();

	@InjectRequirement
	private ServerRequirement req;
	
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
		for (org.jboss.reddeer.eclipse.core.resources.Project p : new  ProjectExplorer().getProjects()) {
			p.delete(true);
		}
		new DefaultToolItem(new DefaultShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@AfterClass
	public static void teardownClass() {
		StringBuilder sb = new StringBuilder();
		boolean fail = false;
		for (Entry<Project, List<String>> projectWarning : projectWarnings
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

	protected void importExample(CentralExampleProject project) {
		ExamplesOperator.getInstance().importExampleProjectFromCentral(project);
		//check for warnings
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}

	protected void importAndDeployExample(CentralExampleProject project) {
		importExample(project);
		ExamplesOperator.getInstance().deployProject(project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		ExamplesOperator.getInstance().checkDeployedProject(
				project.getProjectName(), req.getServerNameLabelText(req.getConfig()));
	}
}
