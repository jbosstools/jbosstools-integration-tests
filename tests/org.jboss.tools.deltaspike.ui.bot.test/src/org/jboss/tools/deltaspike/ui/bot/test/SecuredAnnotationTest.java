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

import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.After;
import org.junit.Test;

/**
 * Test @Secured annotation, two approaches:
 * 
 * 1. When there are multiple authorizer methods
 * 2. When there is no authorizer method
 * 
 * @author jjankovi
 *
 */
public class SecuredAnnotationTest extends DeltaspikeTestBase {

	private Regex ambiguousAuthorizerProblem = new Regex(
			"Ambiguous authorizers found.*");
	
	private Regex noMatchingAuthorizerProblem = new Regex(
			"No matching authorizer found.*");
	
	@After
	public void closeAllEditors() {
	//	Bot.get().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testAmbiguousAuthorizers() {
		
		String projectName = "ambiguousAuthorizers";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(
				ambiguousAuthorizerProblem), TimePeriod.NORMAL);

		replaceInEditor(projectName, "test", "SecuredBean.java", "4", "1", true);
		
		new WaitWhile(new SpecificProblemExists(
				ambiguousAuthorizerProblem), TimePeriod.NORMAL);
		
		OpenOnHelper.checkOpenOnFileIsOpened(bot, "SecuredBean.java",
				"doSomething", "Open authorizer method CustomAuthorizer.check3()",
				"CustomAuthorizer.java");
	}
	
	@Test
	public void testNotDeclaredSecurityBindingInAuthorizer() {
		
		String projectName = "noAuthorizer";
		importDeltaspikeProject(projectName);
		
		new WaitUntil(new SpecificProblemExists(
				noMatchingAuthorizerProblem), TimePeriod.NORMAL);

		replaceInEditor(projectName, "test", "SecuredBean.java", "1", "4", true);
		
		new WaitWhile(new SpecificProblemExists(
				noMatchingAuthorizerProblem), TimePeriod.NORMAL);
		
		OpenOnHelper.checkOpenOnFileIsOpened(bot, "SecuredBean.java",
				"doSomething", "Open authorizer method CustomAuthorizer.check()",
				"CustomAuthorizer.java");
		
	}
	
}
