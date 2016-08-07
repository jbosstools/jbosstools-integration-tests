package org.jboss.tools.bpmn2.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.NewJBpmProcessWizard;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProjectWizard;
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
public class JBpmProcessWizardTest {

	@BeforeClass
	public static void createProject() {
		new NewJBpmProjectWizard().execute("TestProject");
	}
	
	@AfterClass
	public static void deleteProject() throws Exception {
		new PackageExplorer().getProject("TestProject").delete(true);
	}
	
	@Test
	public void newModelTest() throws Exception {
		new NewJBpmProcessWizard().execute("SampleProcess.bpmn", new String[] {"TestProject"}, "Sample", "jboss.org.bpmn2", "defaultPackage");
		Assert.assertTrue(new PackageExplorer().selectProject("TestProject").containsItem("SampleProcess.bpmn"));
		// Assert process name, process id and package
	}
	
	@Test
	public void formContainerExistenceValidationTest() throws Exception {
		NewJBpmProcessWizard wizard = new NewJBpmProcessWizard();
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
		NewJBpmProcessWizard wizard = new NewJBpmProcessWizard();
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
		NewJBpmProcessWizard wizard = new NewJBpmProcessWizard();
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
