package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.tools.ws.reddeer.ui.preferences.JAXRSValidatorPreferencePage;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class JaxRsValidatorTest extends RESTfulTestBase {

	@Override
	public String getWsProjectName() {
		return "restValidation2";
	}

	@Override
	public void cleanup() {
	
	}

	@Test
	public void testValidatorInPreferences() {
		/* try to open JAX-RS Validator in Preferences */
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		JAXRSValidatorPreferencePage page = new JAXRSValidatorPreferencePage();
		dialog.select(page);
		page.cancel();
	}

	@Test
	public void testValidatorEnabled() {
		/* enable restful validation */
		restfulHelper.enableRESTValidation();

		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 1);
	}

	@Test
	public void testValidatorDisabled() {
		/* disable restful validation */
		restfulHelper.disableRESTValidation();

		/* test count of validation errors */
		assertCountOfPathAnnotationValidationErrors(getWsProjectName(), 0);

		/* enable restful validation - to have proper test environment*/
		restfulHelper.enableRESTValidation();
	}
}
