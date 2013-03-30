package org.jboss.tools.bpmn2.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;

import org.jboss.tools.bpmn2.ui.bot.ext.wizard.JBPMProcessLegacyWizard;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.JBPMProjectLegacyWizard;
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
public class LegacyProcessWizardTest {

	static PackageExplorer packageView = new PackageExplorer();
	static JBPMProjectLegacyWizard projectWizardView = new JBPMProjectLegacyWizard();
	static JBPMProcessLegacyWizard processWizardView = new JBPMProcessLegacyWizard();
	
	@BeforeClass
	public static void createProject() {
		projectWizardView.execute("TestProject");
	}
	
	@AfterClass
	public static void deleteProject() {
		packageView.getProject("TestProject").delete(true);
	}
	
	@Test
	public void newProcessTest() {
		processWizardView.execute("SampleProcess", new String[] {"TestProject", "src/main/resources"});
		Assert.assertTrue(packageView.selectProject("TestProject").containsItem("src/main/resources", "SampleProcess.bpmn"));
	}
	
	/**
	 * ISSUES:
	 * 	1) make sure an empty name may not be added.
	 *  2) should it be legal to create a name without .bpmn suffix?
	 *  
	 * @throws Exception
	 */
	@Test
	public void newProcessFormValidationTest() throws Exception {
		try {
			processWizardView.execute("");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			processWizardView.cancel();
		}
	}
	
}
