package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.junit.Before;
import org.junit.Test;

public class ConstraintSetupWizardTest extends WizardTestBase {

	@Before
	public void prepare(){
		newProject(PROJECT_NAME);
	}
	
	@Test
	public void testValidationXmlCreated(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); //this will set context for forge
		WizardDialog wd = getWizardDialog("constraint-setup", "(Constraint: Setup).*");
		new LabeledCombo("Bean Validation provider:").setSelection("Generic Java EE");
		wd.finish(TimePeriod.getCustom(600));
		assertTrue("validation.xml has not been created!", pe.getProject(PROJECT_NAME)
				.containsItem("src", "main", "resources", "META-INF", "validation.xml"));
	}
}
