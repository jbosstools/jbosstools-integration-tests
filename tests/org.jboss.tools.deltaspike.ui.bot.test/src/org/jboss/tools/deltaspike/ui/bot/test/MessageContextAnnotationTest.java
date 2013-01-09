package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.After;
import org.junit.Test;

/**
 * Test @MessageContext annotation, with following approach:
 * 
 * 1. injecting MessageContext will not fail -> no validation error 2. try to
 * open on MessageContext -> MessageContextProducer should be opened
 * 
 * @author jjankovi
 * 
 */
public class MessageContextAnnotationTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex("No bean is eligible.*");

	@After
	public void closeAllEditors() {
		Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}

	@Test
	public void testInjectMessageContext() {

		String projectName = "messageContext";
		importDeltaspikeProject(projectName);

		new WaitWhile(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		openClass(projectName, "test", "Test.java");
		OpenOnHelper.checkOpenOnFileIsOpened(bot, "Test.java",
				"messageContext", "Open @Inject Bean",
				"MessageContextProducer.class");

	}

}
