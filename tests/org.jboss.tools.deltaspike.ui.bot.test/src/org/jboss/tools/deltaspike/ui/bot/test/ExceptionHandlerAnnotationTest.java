package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Test @ExceptionHandler annotation, with following approach:
 * 
 * 1. insert into basic class some exception handler method (one of parameter annotated with
 *    @Handles or @BeforeHandles
 * 2. check there is validation error (class must be annotated with @ExceptionHandler
 * 3. annotate class with @ExceptionHandler
 * 4. check there is no problem any more 
 * 
 * @author jjankovi
 * 
 */
public class ExceptionHandlerAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex("Exception handler methods must be registered on " +
			"beans annotated with @ExceptionHandler.*");

	@After
	public void closeAllEditors() {
		Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}

	@Test
	public void testNoExceptionHandlerWithHandles() {

		String projectName = "exception-handler1";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		
		annotateBean(projectName, "test", "Test.java", 5, 0, "@ExceptionHandler");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);

	}
	
	@Test
	public void testNoExceptionHandlerWithBeforeHandles() {
		
		String projectName = "exception-handler1";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		
		annotateBean(projectName, "test", "Test.java", 5, 0, "@ExceptionHandler");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
	}
	
}
