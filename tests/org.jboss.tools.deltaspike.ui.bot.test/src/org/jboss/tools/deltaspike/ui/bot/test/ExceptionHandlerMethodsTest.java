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

import static org.junit.Assert.fail;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * This test checks behaviour of exception handler methods validation. There are
 * two of them: Handles and BeforeHandles. Both of them are tested with the same
 * approach:
 * 
 * <ul>
 * 	<li>{@link #testHandlerParameterType}</li>
 * 		<ul>
 * 			<li>Import handles or before-handles project.</li>
 * 			<li>Check method with parameter annotated with some exception handler
 * 				annotation. Type of method should be invalid - anything except of
 * 				ExceptionEvent.</li>
 * 			<li>Check there is validation error.</li>
 * 		</ul>
 * 	<li>{@link #testHandlerAdditionalParameters}</li>
 * 		<ul>
 * 			<li>Import handles-additionalParameters or
 * 				before-handles-additionalParameters project.</li>
 * 			<li>Check method with parameter annotated with exception handler annotation,
 * 				any additional parameters of a handler method should be treated as injection
 * 				points.</li>
 * 			<li>Check there is validation error.</li>
 * 			<li>Fix problem.</li>
 * 			<li>Check there isn't validation error.</li>
 * 		</ul>
 * </ul>
 * 
 * @author jjankovi
 * 
 */
public class ExceptionHandlerMethodsTest extends DeltaspikeTestBase {

	private RegexMatcher invalidParameterTypeRegexMatcher = new RegexMatcher(
			"Parameter of a handler method must be a " + "ExceptionEvent.*");

	private RegexMatcher noBeanIsEligibleRegexMatcher = new RegexMatcher("No bean is eligible for injection.*");

	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}
	
	@Test
	public void testHandlerParameterType() {
		testHandlerParameterType("handles");
	}

	@Test
	public void testBeforeHandlerParameterType() {
		testHandlerParameterType("before-handles");
	}

	@Test
	public void testHandlerAdditionalParameters() {
		testHandlerAdditionalParameters("handles-additionalParameters");
	}

	@Test
	public void testBeforeHandlerAdditionalParameters() {
		testHandlerAdditionalParameters("before-handles-additionalParameters");
	}

	private void testHandlerParameterType(String projectName) {
		importDeltaspikeProject(projectName, sr);

		try {
			new WaitUntil(new SpecificProblemExists(invalidParameterTypeRegexMatcher), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail(e.getMessage());
		}

	}

	private void testHandlerAdditionalParameters(String projectName) {
		importDeltaspikeProject(projectName, sr);

		try {
			new WaitUntil(new SpecificProblemExists(noBeanIsEligibleRegexMatcher), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail(e.getMessage());
		}

		insertIntoFile(projectName, "test", "StringProducer.java", 12, 0, "import javax.enterprise.inject.Produces;");
		insertIntoFile(projectName, "test", "StringProducer.java", 17, 0, "@Produces");

		try {
			new WaitWhile(new ProblemExists(ProblemType.ALL), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException e) {
			fail(e.getMessage());
		}
	}

}
