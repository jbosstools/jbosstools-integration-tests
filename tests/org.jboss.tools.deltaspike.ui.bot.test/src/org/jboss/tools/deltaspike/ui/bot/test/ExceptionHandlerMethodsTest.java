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

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.regex.Regex;
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
		new SWTWorkbenchBot().closeAllEditors();
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
