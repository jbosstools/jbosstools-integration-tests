package org.jboss.tools.deltaspike.ui.bot.test;

import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Tests if there are all deltaspike validation messages under CDI Validator and
 * whether their severity can be changed.
 * 
 * @author jjankovi
 *
 */
public class ValidationsInPreferenceTest extends DeltaspikeTestBase {

	@InjectRequirement
	private static ServerRequirement sr;

	private WorkbenchPreferenceDialog preferenceDialog;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}

	/**
	 * Tests if there are all deltaspike validation messages under CDI Validator.
	 */
	@Test
	public void testAllValidationsPresence() {
		openValidationsInPreferenceDialog();

		checkDeltaspikeValidationPresense();

		preferenceDialog.cancel();
	}

	/**
	 * Test if problem severity can be changed.
	 */
	@Test
	public void testConfigureProblemSeverity() {
		importDeltaspikeProject("configureProblemSeverity", sr);

		setSeverityToAll("Error");
		checkAllProblemsDetected(ProblemType.ERROR);

		setSeverityToAll("Warning");
		checkAllProblemsDetected(ProblemType.WARNING);

		setSeverityToAll("Ignore");

		// Check there is no problems
		try {
			new WaitWhile(new ProblemExists(ProblemType.ANY), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail(e.getMessage());
		}
	}

	private void openValidationsInPreferenceDialog() {
		preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(new CDIValidatorPreferencePage());

		WidgetHandler.getInstance().notify(SWT.MouseUp, new DefaultLabel("Extensions").getSWTWidget());
		WidgetHandler.getInstance().notify(SWT.MouseUp, new DefaultLabel("Deltaspike").getSWTWidget());
	}

	private void checkDeltaspikeValidationPresense() {
		for (DeltaspikeValidations validation : DeltaspikeValidations.values()) {
			new DefaultLabel(validation.getValidationLabel());
		}
	}

	private void setSeverityToAll(String severity) {
		openValidationsInPreferenceDialog();

		for (DeltaspikeValidations validation : DeltaspikeValidations.values()) {
			new LabeledCombo(validation.getValidationLabel()).setSelection(severity);
		}

		applyChanges();
	}

	private void checkAllProblemsDetected(ProblemType problemType) {
		for (DeltaspikeValidations validation : DeltaspikeValidations.values()) {
			try {
				new WaitUntil(
						new SpecificProblemExists(new RegexMatcher(validation.getValidationMessage()), problemType),
						TimePeriod.LONG);
			} catch (WaitTimeoutExpiredException e) {
				fail(e.getMessage());
			}
		}
	}

	private void applyChanges() {
		String confirmChangesShell = "Validator Settings Changed";
		
		new PushButton("Apply").click();
		new WaitUntil(new ShellWithTextIsAvailable(confirmChangesShell));
		new DefaultShell(confirmChangesShell);
		new PushButton("Yes").click();
		new WaitWhile(new ShellWithTextIsAvailable(confirmChangesShell));
		preferenceDialog.ok();
	}
}

enum DeltaspikeValidations {

	// @formatter:off
	EXCEPTION_HANDLER(
			"Bean is not annotated @ExceptionHandler:",
			"Exception handler methods must be registered on beans annotated with @ExceptionHandler"), 
	INVALID_HANDER(
			"Invalid hander method parameter type:",
			"Parameter of a handler method must be a ExceptionEvent.*"), 
	AMBIGUOUS_AUTHORIZER(
			"Ambiguous authorizer for a secured method:",
			"Ambiguous authorizers found.*"), 
	UNRESOLVED_AUTHORIZER(
			"Unresolved authorizer for a secured method:",
			"No matching authorizer found.*"), 
	INVALID_AUTHORIZER(
			"Invalid authorizer:",
			"Authorizer method .*");
	// @formatter:on

	private String label;
	private String message;

	private DeltaspikeValidations(String label, String message) {
		this.label = label;
		this.message = message;
	}

	/**
	 * Returns text which should be used in CDIValidatorPreferencePage
	 * 
	 * @return label used in preference page
	 */
	public String getValidationLabel() {
		return label;
	}

	/**
	 * Returns validation message which should be displayed in java editor
	 * 
	 * @return validation message text
	 */
	public String getValidationMessage() {
		return message;
	}

}
