package org.jboss.tools.ws.ui.bot.test.rest.validation;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.ext.gen.IPreference;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
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
		SWTBot bot = openJaxRsValidator();
		closeJaxRsValidator(bot);
		
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
	
	private SWTBot openJaxRsValidator() {
		
		try {
			return open.preferenceOpen(new IPreference() {
				@Override
				public String getName() {
					return "JAX-RS Validator";
				
				}
				
				@Override
				public List<String> getGroupPath() {
					return Arrays.asList("JBoss Tools", "JAX-RS");
				}
			});
		} catch(WidgetNotFoundException exc) {
			fail("JAX-RS Validator is not located in Preferences");
		}
		return null;	
	}
	
	private void closeJaxRsValidator(SWTBot bot) {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
}
