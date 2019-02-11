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

import java.util.Collection;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.cdi.reddeer.condition.SpecificProblemExists;
import org.jboss.tools.deltaspike.ui.bot.test.exception.DeltaspikeTestInFailureException;
import org.junit.After;
import org.junit.Test;

/**
 * Test @ConfigProperty annotation, two approaches:<br>
 * 
 * 1. When config property is a valid injection point<br> 
 * 2. When config property is not a valid injection point
 * 
 * @author jjankovi
 *
 */
public class ConfigPropertyAnnotationTest extends DeltaspikeTestBase {

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher("No bean is eligible.*");

	@RequirementRestriction 
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		return getServerRuntimeRestriction();
	}
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}

	@Test
	public void testInjectSupportedConfigProperty() {

		String projectName = "configProperty-support";
		importDeltaspikeProject(projectName, sr);

		new WaitUntil(new SpecificProblemExists(validationProblemRegexMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "Test.java", 7, 0, "@ConfigProperty(name = \"boolean\") \n");
		insertIntoFile(projectName, "test", "Test.java", 2, 0,
				"import org.apache.deltaspike.core.api.config.ConfigProperty; \n");

		new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher), TimePeriod.LONG);

	}

	@Test(expected = DeltaspikeTestInFailureException.class)
	public void testInjectUnsupportedConfigProperty() {

		String projectName = "configProperty-unsupport";
		importDeltaspikeProject(projectName, sr);

		try {
			new WaitUntil(new SpecificProblemExists(validationProblemRegexMatcher), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			throw new DeltaspikeTestInFailureException("JBIDE-13554 should be fixed now.");
		}
		
		insertIntoFile(projectName, "test", "Test.java", 8, 0, "@ConfigProperty(name = \"boolean\") \n");
		insertIntoFile(projectName, "test", "Test.java", 2, 0,
				"import org.apache.deltaspike.core.api.config.ConfigProperty; \n");
		try {
			new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher), TimePeriod.DEFAULT);
		} catch (WaitTimeoutExpiredException ex) {
			fail("this is known issue JBIDE-13554");
		}
	}

}
