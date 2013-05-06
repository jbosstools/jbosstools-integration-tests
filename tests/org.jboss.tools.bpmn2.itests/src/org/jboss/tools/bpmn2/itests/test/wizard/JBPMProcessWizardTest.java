package org.jboss.tools.bpmn2.itests.test.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.itests.swt.ext.JBPM5RuntimeRequirement.JBPM5;
import org.jboss.tools.bpmn2.itests.swt.ext.SetUpWorkspaceRequirement.SetUpWorkspace;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProcessWizard;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProjectLegacyWizard;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the jbpm model wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@JBPM5()
@SetUpWorkspace()
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
		Assert.assertTrue(new ProjectExplorer().getProject("TestProject").containsItem("SampleProcess.bpmn"));
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
