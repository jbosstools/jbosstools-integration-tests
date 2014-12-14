package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Test;


public class ProjectNewWizardTest extends WizardTestBase {
	
	private static final String WORKSPACE = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString(); 
	
	@Test
	public void testIsFocusedOnStartup(){
		runForgeCommand("project-new");
		RegexMatcher rm = new RegexMatcher("(Project: New).*");
		DefaultShell shell = new DefaultShell(new WithTextMatcher(rm));
		assertTrue("'Project: New' wizard is not focused on startup", shell.isFocused());
		shell.close();
	}
	
}
