package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
@CleanWorkspace
public class ProjectTest extends ForgeConsoleTestBase {

	@Test
	public void pomProject() {
		createProject(ProjectTypes.pom);
		String text = fView.getConsoleText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		assertTrue(pExplorer.containsProject(PROJECT_NAME));
		Project project = pExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem("pom.xml"));
		
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>pom</packaging>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
	
	@Test
	public void warProject() {
		createProject(ProjectTypes.war);
		String text = fView.getConsoleText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		assertTrue(pExplorer.containsProject(PROJECT_NAME));
		Project project = pExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem("pom.xml"));
		
		try {
			String pomContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>war</packaging>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
	}
	
}
