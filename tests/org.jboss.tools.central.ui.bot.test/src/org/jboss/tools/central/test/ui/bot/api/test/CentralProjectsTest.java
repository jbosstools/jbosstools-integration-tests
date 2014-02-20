package org.jboss.tools.central.test.ui.bot.api.test;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.tools.central.test.ui.bot.api.CentralProjects;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
@SuiteClasses(CentralProjectsTest.class)
public class CentralProjectsTest extends SWTTestExt {
	
	@Test
	public void archetypeTest(){
		CentralProjects.importArchetype("HTML5 Project");
		List<Project> projects = new ProjectExplorer().getProjects();
		assertEquals(1, projects.size());
		assertEquals("HTML5Project", projects.get(0).getName());
		projects.get(0).delete(true);
	}
	
	@Test
	public void quickstartTest(){
		CentralProjects.importQuickstart("Mobile Applications", "kitchensink-html5-mobile");
	}

}
