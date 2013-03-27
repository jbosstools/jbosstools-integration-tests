package org.jboss.tools.bpmn2.ui.bot.test.legacy;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProcessWizard;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProjectWizard;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the process wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@Require(jbpm = @JBPM(), runOnce = true)
public class ProcessWizardTest {

	PackageExplorer packageView = new PackageExplorer();
	
	NewJBpmProcessWizard wizardView = new NewJBpmProcessWizard();
	
	@BeforeClass
	public void newProjectTest() {
		new NewJBpmProjectWizard().execute("TestProject");
	}
	
	@AfterClass
	public void deleteProjectTest() {
		List<Project> projectList = packageView.getProjects();
		for (Project p : projectList) {
			if ("TestProject".equals(p.getName())) {
				p.delete(true);
			}
		}
	}
	
	@Test
	public void newProcessTest() {
		wizardView.execute("process.bpmn", new String[] {"TestProject", "src/main/resources"});
		Assert.assertTrue(packageView.selectProject("TestProject").containsItem("src/main/resources", "process.bpmn"));
	}
	
	@Test()
	public void newProcessFormValidationTest() throws Exception {
		try {
			wizardView.execute("");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Next >' is not enabled", e.getMessage());
		}
	}
	
}
