package org.jboss.tools.cdi.bot.test.validation;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ProgressInformationShellIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Check if CDI validator is correctly located in Preferences and if
 * enabling/disabling validation works as expected
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class CDIValidatorTest extends CDITestBase {
	
	private final static CDIValidatorPreferencePage 
		cdiValidatorPage = new CDIValidatorPreferencePage();
	
	
	@Override
	public String getProjectName() {
		return "CDIAssignableDialogTest";
	}
	
	@AfterClass
	public static void enableValidator() {
		modifyCDIValidatorState(true);
	}
	
	@Test
	public void testValidatorInPreferences() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(cdiValidatorPage);
		preferenceDialog.cancel();	
	}
	
	@Test
	public void testEnabledValidator() {
		modifyCDIValidatorState(true);
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		new WaitUntil(new ProblemExists(ProblemType.ANY), TimePeriod.NORMAL);
		assertEquals("Warnings node should contain one warning", 
				problemsView.getAllWarnings().size(), 1);
		assertEquals("Warning text", 
				"No bean is eligible for injection to the injection point [JSR-299 §5.2.1]",
				problemsView.getAllWarnings().get(0).getText());
	}
	
	@Test
	public void testDisabledValidator() {
		modifyCDIValidatorState(false);
		new WaitWhile(new ProblemExists(ProblemType.ANY), TimePeriod.NORMAL);
		modifyCDIValidatorState(true);
	}
	
	private static void modifyCDIValidatorState(boolean enable) {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(cdiValidatorPage);
		boolean stateChanged = cdiValidatorPage.isValidationEnabled() != enable;
		if (enable) {
			cdiValidatorPage.enableValidation();
		} else {
			cdiValidatorPage.disableValidation();
		}
		String shellText = new DefaultShell().getText();
		new PushButton("OK").click();
		if (stateChanged) {
			closeSettingsChangedShell();
		}
		new WaitWhile(new ProgressInformationShellIsActive());
		new WaitWhile(new ShellWithTextIsAvailable(shellText));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private static void closeSettingsChangedShell() {
		new WaitUntil(new ShellWithTextIsAvailable("Validator Settings Changed"), TimePeriod.LONG);
		DefaultShell shell = new DefaultShell("Validator Settings Changed");
		String shellText = shell.getText();
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
	}

}