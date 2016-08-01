package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.tools.cdi.reddeer.cdi.ui.CDIValidatorPreferencePage;
import org.junit.Test;

/**
 * Tests if there are all deltaspike validation messages under CDI Validator 
 * 
 * @author jjankovi
 *
 */
public class ValidationsInPreferenceTest extends DeltaspikeTestBase {

	@Test
	public void testAllValidationsPresence()  {
		
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(new CDIValidatorPreferencePage());

		WidgetHandler.getInstance().notify(SWT.MouseUp, new DefaultLabel("Extensions").getSWTWidget());
		
		WidgetHandler.getInstance().notify(SWT.MouseUp, new DefaultLabel("Deltaspike").getSWTWidget());
		
		checkDeltaspikeValidationPresense();
		
		preferenceDialog.cancel();
		
	}
	
	private void checkDeltaspikeValidationPresense() {
		for (DeltaspikeValidations validation: DeltaspikeValidations.values()) {
			new DefaultLabel(validation.validationMessage());
		}
	}
	
}

enum DeltaspikeValidations {
	
	EXCEPTION_HANDLER("Bean is not annotated @ExceptionHandler:"), 
	INVALID_HANDER("Invalid hander method parameter type:"), 
	UMBIGUOUS_AUTHORIZER("Umbiguous authorizer for a secured method:"),
	UNRESOLVED_AUTHORIZER("Unresolved authorizer for a secured method:"),
	INVALID_AUTHORIZER("Invalid authorizer:");
	
	private String message;
	
	private DeltaspikeValidations(String message) {
		this.message = message;
	}
	
	public String validationMessage() {
		return message;
	}
	
}
