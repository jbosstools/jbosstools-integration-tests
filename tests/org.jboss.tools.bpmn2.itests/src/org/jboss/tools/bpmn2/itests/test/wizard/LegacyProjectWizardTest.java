package org.jboss.tools.bpmn2.itests.test.wizard;

import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProjectLegacyWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.junit.After;
import org.junit.Test;

/**
 * Verify functionality of the project wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 * 
 */
@Require(jbpm = @JBPM(), runOnce = true)
public class LegacyProjectWizardTest extends SWTTestExt {

	// TBD: 
	//   * switch to org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer
	//	   - missing delete project method. 
	ProjectExplorer explorerView = new ProjectExplorer();
	
	JBPMProjectLegacyWizard wizardView  = new JBPMProjectLegacyWizard();
	
	@After
	public void deleteAllProjects() {
		explorerView.deleteAllProjects();
		
	}
	
	@Test
	public void newProjectWithSimpleProcessTest() throws Exception {
		wizardView.execute("TestProject", JBPMProjectLegacyWizard.ProcessType.SIMPLE, true, false);
		
		assertTrue(explorerView.existsResource("TestProject", "src/main/resources", "sample.bpmn"));
		assertTrue(explorerView.existsResource("TestProject", "src/main/java", "com.sample", "ProcessMain.java"));
	}
	
	@Test
	public void newProjectWithAdvancedProcessTest() {
		wizardView.execute("TestProject", JBPMProjectLegacyWizard.ProcessType.ADVANCED, true, true);
		
		assertTrue(explorerView.existsResource("TestProject", "src/main/resources", "sample.bpmn"));
		assertTrue(explorerView.existsResource("TestProject", "src/main/java", "com.sample", "ProcessMain.java"));
		assertTrue(explorerView.existsResource("TestProject", "src/main/java", "com.sample", "ProcessTest.java"));
	}
	
	@Test()
	public void newEmptyProjectTest() throws Exception {
		wizardView.execute("TestProject");
		
		// the node list will contain one empty node!
		assertTrue(explorerView.selectTreeItem("src/main/java", new String[] {"TestProject"}).getNodes().get(0).isEmpty());
		assertTrue(explorerView.selectTreeItem("src/main/resources", new String[] {"TestProject"}).getNodes().get(0).isEmpty());
	}

	@Test()
	public void newProjectFormValidationTest() throws Exception {
		try {
			wizardView.execute("");
			fail("Project with an empty name was created!");
		} catch (JFaceLayerException e) {
			assertEquals("Button '&Next >' is not enabled", e.getMessage());
		} finally {
			wizardView.cancel();
		}
	}
	
}
