package org.jboss.tools.cdi.bot.test.validation.template;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ProgressInformationShellIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
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
		cdiValidatorPage = new CDIValidatorPreferencePage();
	
	@AfterClass
	public static void enableValidator() {
		modifyCDIValidatorState(true);
	}
	
	@Before
	public void createBean(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(!pe.getProject(PROJECT_NAME).containsItem("Java Resources","src","test","TestClass.java")){
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
				problemsView.getProblems(ProblemType.WARNING).size(), 1);
		//TODO use validatorHelper
		assertTrue(problemsView.getProblems(ProblemType.WARNING).get(0).getDescription().
				contains("No bean is eligible for injection to the injection point"));
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