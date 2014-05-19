package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.toolbar.WorkbenchToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.CentralExampleProject;
import org.jboss.tools.central.reddeer.projects.Project;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

@JBossServer(type = ServerReqType.EAP, state = ServerReqState.RUNNING)
public class ExamplesTest {

	private static Map<Project, List<String>> projectWarnings = new HashMap<Project, List<String>>();

	@InjectRequirement
	private ServerRequirement req;
	
	@Before
	public void setup() {
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage();
		String mvnConfigFileName = System.getProperty("maven.config.file");
		prefPage.open();
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		prefPage.ok();
		new WorkbenchToolItem("JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}

	@After
	public void teardown() {
		for (org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project p : new  ProjectExplorer().getProjects()) {
			p.delete(true);
		}
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

	@Test
	public void kitchensinkTest() {
		importAndDeployExample(new CentralExampleProject("kitchensink",
				"jboss-kitchensink", "Web Applications"));
	}

	@Test
	public void greeterTest() {
		importAndDeployExample(new CentralExampleProject("greeter",
				"jboss-greeter", "Web Applications"));
	}

	@Test
	public void helloworldTest() {
		importAndDeployExample(new CentralExampleProject("helloworld",
				"jboss-helloworld", "Web Applications"));
	}

	@Test
	public void kitchensinkRfTest() {
		importAndDeployExample(new CentralExampleProject("kitchensink-rf",
				"jboss-kitchensink-rf", "Web Applications"));
	}

	@Test
	public void kitchensinkHTML5MobileTest() {
		importAndDeployExample(new CentralExampleProject(
				"kitchensink-html5-mobile",
				"jboss-kitchensink-html5-mobile", "Mobile Applications"));
	}

	@Test
	public void kitchensinkBackboneTest() {
		importAndDeployExample(new CentralExampleProject(
				"kitchensink-backbone", "jboss-kitchensink-backbone",
				"Mobile Applications"));
	}

	@Test
	public void backEndHelloworldRsTest() {
		importAndDeployExample(new CentralExampleProject("helloworld-rs",
				"jboss-helloworld-rs", "Back-end Applications"));
	}

	private void importExample(CentralExampleProject project) {
		ExamplesOperator.getInstance().importExampleProjectFromCentral(project);
		//check for warnings
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}

	private void importAndDeployExample(CentralExampleProject project) {
		importExample(project);
		ExamplesOperator.getInstance().deployProject(project.getProjectName(),
				req.getServerNameLabelText(req.getConfig()));
		ExamplesOperator.getInstance().checkDeployedProject(
				project.getProjectName(), req.getServerNameLabelText(req.getConfig()));
	}
}
