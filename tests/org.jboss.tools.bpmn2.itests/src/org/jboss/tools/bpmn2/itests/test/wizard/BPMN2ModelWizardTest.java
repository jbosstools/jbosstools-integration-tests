package org.jboss.tools.bpmn2.itests.test.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessRuntimeRequirement.ProcessRuntime;
import org.jboss.tools.bpmn2.itests.wizard.BPMN2ModelWizard;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProjectLegacyWizard;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the bpmn2 model wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@ProcessRuntime()
public class BPMN2ModelWizardTest {

	@BeforeClass
	public static void createProject() {
		new JBPMProjectLegacyWizard().execute("TestProject");
	}
	
	@AfterClass
	public static void deleteProject() throws Exception {
		new PackageExplorer().getProject("TestProject").delete(true);
	}
	
	@Test
	public void newModelTest() throws Exception {
		new BPMN2ModelWizard().execute(new String[] {"TestProject"}, "SampleProcess.bpmn2");
		Assert.assertTrue(new ProjectExplorer().getProject("TestProject").containsItem("SampleProcess.bpmn2"));
	}
	
	@Test
	public void newModelFormValidationTest() throws Exception {
		BPMN2ModelWizard wizard = new BPMN2ModelWizard();
		try {
			wizard.execute("");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
}
