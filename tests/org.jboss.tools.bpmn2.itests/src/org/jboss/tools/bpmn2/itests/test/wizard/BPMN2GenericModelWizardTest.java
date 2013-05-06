package org.jboss.tools.bpmn2.itests.test.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;

import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessRuntimeRequirement.ProcessRuntime;
import org.jboss.tools.bpmn2.itests.wizard.BPMN2GenericModelWizard;
import org.jboss.tools.bpmn2.itests.wizard.JBPMProjectLegacyWizard;
import org.jboss.tools.bpmn2.itests.wizard.BPMN2GenericModelWizard.ModelType;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the generic bpmn2 model wizard.
 * 
 * ISSUES:
 * 	1) Finish button will not be enabled unless the project is chosen using the browse 
 *     button. Writing the text into the field does not work!
 *  2) When a file name is entered and the user attempts to change the package then the
 *     name will be reset!
 *     
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@ProcessRuntime()
public class BPMN2GenericModelWizardTest {

	public static final String NAMESPACE = "http://jboss.org/bpmn2";
	public static final String PROJECT   = "TestProject"; 
	
	@BeforeClass
	public static void createProject() {
		new JBPMProjectLegacyWizard().execute(PROJECT);
	}
	
	@AfterClass
	public static void deleteProject() throws Exception {
		new PackageExplorer().getProject(PROJECT).delete(true);
	}
	
	@Test
	public void newProcessModelTest() throws Exception {
		new BPMN2GenericModelWizard().execute(new String[] {PROJECT}, "ProcessModel.bpmn2", NAMESPACE, ModelType.PROCESS);
		Assert.assertTrue(new ProjectExplorer().getProject(PROJECT).containsItem("ProcessModel.bpmn2"));
	}
	
	@Test
	public void newChoreographyModelTest() throws Exception {
		new BPMN2GenericModelWizard().execute(new String[] {PROJECT}, "ChoreographyModel.bpmn2", NAMESPACE, ModelType.CHOREOGRAPHY);
		Assert.assertTrue(new ProjectExplorer().getProject(PROJECT).containsItem("ChoreographyModel.bpmn2"));
	}
	
	@Test
	public void newCollaborationModelTest() throws Exception {
		new BPMN2GenericModelWizard().execute(new String[] {PROJECT}, "CollaborationModel.bpmn2", NAMESPACE, ModelType.COLLABORATION);
		Assert.assertTrue(new ProjectExplorer().getProject(PROJECT).containsItem("CollaborationModel.bpmn2"));
	}
	
	@Test
	public void newModelFormValidationTest() throws Exception {
		BPMN2GenericModelWizard wizard = new BPMN2GenericModelWizard();
		try {
			wizard.execute(new String[0], "unknown.bpmn", "", ModelType.PROCESS);
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
}
