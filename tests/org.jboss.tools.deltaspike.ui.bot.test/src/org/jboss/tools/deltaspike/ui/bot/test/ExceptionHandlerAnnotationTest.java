/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;


import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
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

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher("Exception handler methods must be registered on " +
			"beans annotated with @ExceptionHandler.*");
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}

	@Test
	public void testNoExceptionHandlerWithHandles() {

		String projectName = "exception-handler1";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);
		
		annotateBean(projectName, "test", "Test.java", 5, 0, "@ExceptionHandler");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);

	}
	
	@Test
	public void testNoExceptionHandlerWithBeforeHandles() {
		
		String projectName = "exception-handler1";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);
		
		annotateBean(projectName, "test", "Test.java", 5, 0, "@ExceptionHandler");
		
		new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);
	}
	
}
