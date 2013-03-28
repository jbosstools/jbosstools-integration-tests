package org.jboss.tools.bpmn2.ui.bot.test;

import org.jboss.tools.bpmn2.ui.bot.ext.wizard.NewGenericBpmn2ModelWizard.ModelType;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.NewGenericBpmn2ModelWizard;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProjectWizard;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;

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
@Require(jbpm = @JBPM(), runOnce = true)
public class GenericBpmn2ModelWizardTest {

	public static final String NAMESPACE = "http://jboss.org/bpmn2";
	public static final String PROJECT   = "TestProject"; 
	
	@BeforeClass
	public static void createProject() {
		new NewJBpmProjectWizard().execute(PROJECT);
	}
	
	@AfterClass
	public static void deleteProject() throws Exception {
		new PackageExplorer().getProject(PROJECT).delete(true);
	}
	
	@Test
	public void newProcessModelTest() throws Exception {
		new NewGenericBpmn2ModelWizard().execute("ProcessModel.bpmn2", new String[] {PROJECT}, NAMESPACE, ModelType.PROCESS);
		Assert.assertTrue(new PackageExplorer().selectProject(PROJECT).containsItem("ProcessModel.bpmn2"));
	}
	
	@Test
	public void newChoreographyModelTest() throws Exception {
		new NewGenericBpmn2ModelWizard().execute("ChoreographyModel.bpmn2", new String[] {PROJECT}, NAMESPACE, ModelType.CHOREOGRAPHY);
		Assert.assertTrue(new PackageExplorer().selectProject(PROJECT).containsItem("ChoreographyModel.bpmn2"));
	}
	
	@Test
	public void newCollaborationModelTest() throws Exception {
		new NewGenericBpmn2ModelWizard().execute("CollaborationModel.bpmn2", new String[] {PROJECT}, NAMESPACE, ModelType.COLLABORATION);
		Assert.assertTrue(new PackageExplorer().selectProject(PROJECT).containsItem("CollaborationModel.bpmn2"));
	}
	
	@Test
	public void newModelFormValidationTest() throws Exception {
		NewGenericBpmn2ModelWizard wizard = new NewGenericBpmn2ModelWizard();
		try {
			wizard.execute("unknown.bpmn", new String[0], "", ModelType.PROCESS);
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
}
