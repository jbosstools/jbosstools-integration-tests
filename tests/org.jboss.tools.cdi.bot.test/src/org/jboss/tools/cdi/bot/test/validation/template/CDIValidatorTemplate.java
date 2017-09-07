package org.jboss.tools.cdi.bot.test.validation.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.condition.ProblemExists;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.jface.preference.PreferenceDialog;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Check if CDI validator is correctly located in Preferences and if
 * enabling/disabling validation works as expected
 * 
 * @author jjankovi
 */
public class CDIValidatorTemplate extends CDITestBase {
	
	private final static CDIValidatorPreferencePage 
		cdiValidatorPage = new CDIValidatorPreferencePage(new PreferenceDialog());
	
	@AfterClass
	public static void enableValidator() {
		modifyCDIValidatorState(true);
	}
	
	@Before
	public void createBean(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(!pe.getProject(PROJECT_NAME).containsResource("Java Resources","src","test","TestClass.java")){
			beansHelper.createClass("TestClass", "test");
			new TextEditor("TestClass.java");
			editResourceUtil.replaceInEditor("}", "@Inject String s; }");
			editResourceUtil.replaceInEditor("test;", "test; import javax.inject.Inject;",true);
		}
	}
	
	@Test
	public void testValidatorInPreferences() {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(new CDIValidatorPreferencePage(preferenceDialog));
		preferenceDialog.cancel();	
	}
	
	@Test
	public void testEnabledValidator() {
		modifyCDIValidatorState(true);
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		new WaitUntil(new ProblemExists(ProblemType.ALL), TimePeriod.DEFAULT);
		assertEquals("Warnings node should contain one warning", 
				problemsView.getProblems(ProblemType.WARNING).size(), 1);
		//TODO use validatorHelper
		assertTrue(problemsView.getProblems(ProblemType.WARNING).get(0).getDescription().
				contains("No bean is eligible for injection to the injection point"));
	}
	
	@Test
	public void testDisabledValidator() {
		modifyCDIValidatorState(false);
		new WaitWhile(new ProblemExists(ProblemType.ALL), TimePeriod.DEFAULT);
		modifyCDIValidatorState(true);
	}
	
	private static void modifyCDIValidatorState(boolean enable) {
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		
		CDIValidatorPreferencePage cdiValidatorPage = new CDIValidatorPreferencePage(preferenceDialog);
		preferenceDialog.select();
		
		boolean stateChanged = cdiValidatorPage.isValidationEnabled() != enable;
		if (enable) {
			cdiValidatorPage.enableValidation();
		} else {
			cdiValidatorPage.disableValidation();
		}
		String shellText = new DefaultShell().getText();
		new PushButton("Apply and Close").click();
		if (stateChanged) {
			closeSettingsChangedShell();
		}
		new WaitWhile(new ShellIsAvailable("Progress Information"));
		new WaitWhile(new ShellIsAvailable(shellText));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private static void closeSettingsChangedShell() {
		new WaitUntil(new ShellIsAvailable("Validator Settings Changed"), TimePeriod.LONG);
		DefaultShell shell = new DefaultShell("Validator Settings Changed");
		String shellText = shell.getText();
		new PushButton("Yes").click();
		new WaitWhile(new ShellIsAvailable(shellText), TimePeriod.LONG);
	}

}