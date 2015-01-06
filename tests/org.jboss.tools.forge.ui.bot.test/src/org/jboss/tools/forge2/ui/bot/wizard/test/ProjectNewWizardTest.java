package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Test;


public class ProjectNewWizardTest extends WizardTestBase {
	
	private static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString(); 
	private static final String PROJECT_NAME = "testProject";
	
	@Test
	public void testIsFocusedOnStartup(){
		runForgeCommand("project-new");
		RegexMatcher rm = new RegexMatcher("(Project: New).*");
		DefaultShell shell = new DefaultShell(new WithTextMatcher(rm));
		assertTrue("'Project: New' wizard is not focused on startup", shell.isFocused());
		shell.close();
	}
	
	@Test
	public void testIsProjectCreated(){
		runForgeCommand("project-new");
		RegexMatcher rm = new RegexMatcher("(Project: New).*");
		new WaitUntil(new ShellWithTextIsActive(rm));
		
		new LabeledText("Project name:").setText(PROJECT_NAME);
		new LabeledText("Project location:").setText(WORKSPACE);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(rm), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning());
		
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue(pe.containsProject(PROJECT_NAME));
	}
	
	
	@Test
	public void testFinishBtnDisabled(){
		runForgeCommand("project-new");
		RegexMatcher rm = new RegexMatcher("(Project: New).*");
		DefaultShell shell = new DefaultShell(new WithTextMatcher(rm));
		
		assertTrue(new LabeledText("Project name:").getText().isEmpty());
		assertFalse(new PushButton("Finish").isEnabled());
		
		new LabeledText("Project name:").setText(PROJECT_NAME);
		assertTrue(new PushButton("Finish").isEnabled());
		shell.close();
	}
	
}
