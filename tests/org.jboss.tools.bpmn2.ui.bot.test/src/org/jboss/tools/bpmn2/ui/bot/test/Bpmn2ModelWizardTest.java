package org.jboss.tools.bpmn2.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.NewBpmn2ModelWizard;
import org.jboss.tools.bpmn2.ui.bot.ext.wizard.legacy.NewJBpmProjectWizard;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the bpmn2 model wizard.
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
@Require(jbpm = @JBPM(), runOnce = true)
public class Bpmn2ModelWizardTest {

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
		new NewBpmn2ModelWizard().execute("SampleProcess.bpmn2", new String[] {"TestProject"});
		Assert.assertTrue(new PackageExplorer().selectProject("TestProject").containsItem("SampleProcess.bpmn2"));
	}
	
	@Test
	public void newModelFormValidationTest() throws Exception {
		NewBpmn2ModelWizard wizard = new NewBpmn2ModelWizard();
		try {
			wizard.execute("");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
}
