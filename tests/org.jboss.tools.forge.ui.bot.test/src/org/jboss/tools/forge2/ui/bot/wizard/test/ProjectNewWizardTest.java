package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.junit.Test;


public class ProjectNewWizardTest extends WizardTestBase {
	 
	@Test
	public void testIsFocusedOnStartup(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		assertTrue("'Project: New' wizard is not focused on startup", new DefaultShell().isFocused());
		wd.cancel();
	}
	
	@Test
	public void testIsProjectCreated(){
		newProject(PROJECT_NAME);
	}
	
	
	@Test
	public void testFinishBtnDisabled(){
		WizardDialog wd = getWizardDialog("project-new", "(Project: New).*");
		assertTrue(new LabeledText("Project name:").getText().isEmpty());
		assertFalse(new PushButton("Finish").isEnabled());
		new LabeledText("Project name:").setText(PROJECT_NAME);
		assertTrue(new PushButton("Finish").isEnabled());
		wd.cancel();
	}
	
}
