package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * This test checks behaviour of exception handler methods validation. There are two
 * of them: Handles and BeforeHandles. Both of them are tested with the same approach:
 * 
 * 1. check method with parameter annotated with some exception handler annotation
 *    Type of method should be invalid - anything except of ExceptionEvent
 * 2. check there is validation error
 * 
 * @author jjankovi
 * 
 */
public class ExceptionHandlerMethodsTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex("Parameter of a handler method must be a " +
			"ExceptionEvent.*");

	@After
	public void closeAllEditors() {
		Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testHandlesMethods() {
		
		String projectName = "handles";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		
	}
	
	@Test
	public void testBeforeHandlesMethods() {
		
		String projectName = "before-handles";
		importDeltaspikeProject(projectName);

		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		
	}
	
}
