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
 * Test @MessageBundle annotation, with following approach:
 * 
 * 1. try to inject interface directly (no bean eligible validation problem)
 * 2. annotate interface with Mess	ageBundle annotation -> deltaspike will 
 * 	  automatically create producer for interface annotated with such an annotation
 *    -> no validation marker 
 * 
 * @author jjankovi
 *
 */
public class MessageBundleAnnotationTest extends DeltaspikeTestBase {

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher(
			"No bean is eligible.*");
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}
	
	@Test
	public void testInjectMessageBundleInterface() {
		
		String projectName = "messageBundle";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				validationProblemRegexMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "CustomInterface.java", 2, 0, "@MessageBundle");
		insertIntoFile(projectName, "test", "CustomInterface.java", 1, 0, 
				"import org.apache.deltaspike.core.api.message.MessageBundle; \n");
		
		new WaitWhile(new SpecificProblemExists(
				validationProblemRegexMatcher), TimePeriod.LONG);
		
	}
}
