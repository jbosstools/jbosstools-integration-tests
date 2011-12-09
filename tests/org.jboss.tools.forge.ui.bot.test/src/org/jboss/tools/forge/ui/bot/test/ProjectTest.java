package org.jboss.tools.forge.ui.bot.test;

import java.io.IOException;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
@Require(clearWorkspace=true)
public class ProjectTest extends ForgeTest {

	@Test
	public void pomProject() {
		
		createProject(ProjectTypes.pom);
		
		String text = getStyledText().getText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		pExplorer.show();
		assertTrue(pExplorer.existsResource(PROJECT_NAME));
		assertTrue(pExplorer.existsResource(PROJECT_NAME, "pom.xml"));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pomContent = ResourceUtils.readFile(projectLocation + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>pom</packaging>"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
	
	@Test
	public void warProject() {
		
		createProject(ProjectTypes.war);
		
		String text = getStyledText().getText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		pExplorer.show();
		assertTrue(pExplorer.existsResource(PROJECT_NAME));
		assertTrue(pExplorer.existsResource(PROJECT_NAME, "pom.xml"));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pomContent = ResourceUtils.readFile(projectLocation + "/pom.xml");
			assertTrue(pomContent.contains("<packaging>war</packaging>"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Attempt to read the 'pom.xml' failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
	
	
	
}
