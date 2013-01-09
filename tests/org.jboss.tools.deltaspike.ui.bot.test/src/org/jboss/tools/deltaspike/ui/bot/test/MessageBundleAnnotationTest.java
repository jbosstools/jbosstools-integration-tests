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
 * Test @MessageBundle annotation, with following approach:
 * 
 * 1. try to inject interface directly (no bean eligible validation problem)
 * 2. annotate interface with MessageBundle annotation -> deltaspike will 
 * 	  automatically create producer for interface annotated with such an annotation
 *    -> no validation marker 
 * 
 * @author jjankovi
 *
 */
public class MessageBundleAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex(
			"No bean is eligible.*");

	@After
	public void closeAllEditors() {
		Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testInjectMessageBundleInterface() {
		
		String projectName = "messageBundle";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);

		insertIntoFile(projectName, "test", "CustomInterface.java", 2, 0, "@MessageBundle");
		insertIntoFile(projectName, "test", "CustomInterface.java", 1, 0, 
				"import org.apache.deltaspike.core.api.message.annotation.MessageBundle; \n");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegex), TimePeriod.NORMAL);
		
	}
}
