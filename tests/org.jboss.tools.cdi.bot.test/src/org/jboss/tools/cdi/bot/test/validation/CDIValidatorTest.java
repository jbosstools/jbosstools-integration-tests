package org.jboss.tools.cdi.bot.test.validation;

import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.eclipse.condition.ProblemsExists;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.junit.Test;

/**
 * Check if CDI validator is correctly located in Preferences and if
 * enabling/disabling validation works as expected
 * 
 * @author jjankovi
 *
 */
public class CDIValidatorTest extends CDITestBase {
	
	private final CDIValidatorPreferencePage 
		cdiValidatorPage = new CDIValidatorPreferencePage();
	private final ProblemsView problemsView = new ProblemsView();
	
	
	@Override
	public String getProjectName() {
		return "CDIAssignableDialogTest";
	}
	
	@Test
	public void testValidatorInPreferences() {
		
		cdiValidatorPage.open();
		cdiValidatorPage.cancel();
		
	}
	
	@Test
	public void testEnabledValidator() {
		
		modifyCDIValidatorState(true);
		problemsView.open();
		new WaitUntil(new ProblemsExists(), TimePeriod.NORMAL);
		assertEquals("Warnings node should contain one warning", 
				problemsView.getAllWarnings().size(), 1);
		
	}
	
	@Test
	public void testDisabledValidator() {
		
		modifyCDIValidatorState(false);
		new WaitWhile(new ProblemsExists(), TimePeriod.NORMAL);
		modifyCDIValidatorState(true);
		
	}
	
	private void modifyCDIValidatorState(boolean enable) {
		cdiValidatorPage.open();
		if (enable) {
			cdiValidatorPage.enableValidation();
		} else {
			cdiValidatorPage.disableValidation();
		}
		cdiValidatorPage.ok();
		try {
			new WaitUntil(new ShellWithTextIsActive("Validator Settings Changed"));
			DefaultShell shell = new DefaultShell("Validator Settings Changed");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		} catch (TimeoutException te) {
			log.info("Shell 'Validator Settings Changed' wasn't shown");
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}