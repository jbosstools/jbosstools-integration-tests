package org.jboss.tools.bpmn2.itests.test.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProcessWizard;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProjectLegacyWizard;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the jbpm model wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@Require(jbpm = @JBPM(), runOnce = true)
public class JBPMProcessWizardTest {

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
		new JBPMProcessWizard().execute("SampleProcess.bpmn", new String[] {"TestProject"}, "Sample", "jboss.org.bpmn2", "defaultPackage");
		Assert.assertTrue(new PackageExplorer().selectProject("TestProject").containsItem("SampleProcess.bpmn"));
		// Assert process name, process id and package
	}
	
	@Test
	public void formContainerExistenceValidationTest() throws Exception {
		JBPMProcessWizard wizard = new JBPMProcessWizard();
		try {
			wizard.execute("new_process.bpmn", new String[] {"NonExistentProject"}, "Sample", "jboss.org.bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
	@Test
	public void formPackageNameValidationTest() throws Exception {
		JBPMProcessWizard wizard = new JBPMProcessWizard();
		try {
			wizard.execute("new_process.bpmn", new String[] {"TestProject"}, "Sample", "jboss.org/bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
	@Test
	public void formFileNameFieldValidationTest() throws Exception {
		JBPMProcessWizard wizard = new JBPMProcessWizard();
		try {
			// file must end with 'bpmn' or 'bpmn2'
			wizard.execute("new_process.bpmnX", new String[] {"TestProject"}, "Sample", "jboss.org/bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
}
