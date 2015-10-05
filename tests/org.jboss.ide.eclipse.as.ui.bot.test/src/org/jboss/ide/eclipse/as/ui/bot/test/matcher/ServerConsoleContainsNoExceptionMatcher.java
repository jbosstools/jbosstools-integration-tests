package org.jboss.ide.eclipse.as.ui.bot.test.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * Checks that the active console in console view does not contain an unexpected exception. 
 * Note that some exceptions are ignored - it is declared in {@link #EXPECTED_EXCEPTIONS_PATTERNS} constant. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ServerConsoleContainsNoExceptionMatcher extends TypeSafeMatcher<ConsoleView> {

	private static final String[] EXPECTED_EXCEPTIONS_PATTERNS = new String[]{".*Remote connection failed: java.io.IOException: Connection reset by peer.*"};

	private static final Logger log = Logger.getLogger(ServerConsoleContainsNoExceptionMatcher.class);

	private List<String> expectedExceptions = new ArrayList<String>();
	
	private List<String> unexpectedExceptions = new ArrayList<String>();
	
	private String actualText;

	@Override
	protected boolean matchesSafely(ConsoleView view) {
		actualText = view.getConsoleText();
		if (!actualText.contains("Exception")){
			return true;
		}
		
		findExceptionLines(actualText);
		return unexpectedExceptions.isEmpty();
	}

	private void findExceptionLines(String text) {
		unexpectedExceptions = new ArrayList<String>();
		expectedExceptions = new ArrayList<String>();

		Scanner scanner = new Scanner(text);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains("Exception")){
				log.trace("Found line with exception: " + line);
				if (isExpected(line)){
					log.trace("Line will be ignored");
					expectedExceptions.add(line);
				} else {
					unexpectedExceptions.add(line);
				}
			}
		}
		scanner.close();
	}

	private boolean isExpected(String line) {
		for (String pattern : EXPECTED_EXCEPTIONS_PATTERNS){
			if(line.matches(pattern)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("console contains no unexpected exceptions '\n");
		description.appendText("Found exceptions are:\n");
		for (String exception : unexpectedExceptions){
			description.appendText("\t" + exception + "\n");
		}
		description.appendText("Found expected exceptions are:\n");
		for (String exception : expectedExceptions){
			description.appendText("\t" + exception + "\n");
		}
		description.appendText("Full console log:\n");
		description.appendText(actualText);
		description.appendText("'.");
	} 
}
