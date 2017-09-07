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
 * Test @Secures annotation, two approaches:
 * 
 * 1. When authorizer method has invalid return value
 * 2. When authorizer method doesn't declare security binding
 * 
 * @author jjankovi
 *
 */
public class SecuresAnnotationTest extends DeltaspikeTestBase {

	private RegexMatcher returnValueAuthorizerProblemMatcher = new RegexMatcher(
			"Authorizer method .* does not return a boolean");
	
	private RegexMatcher notDeclaredSecurityBindingProblemMatcher = new RegexMatcher(
			"Authorizer method .* does not declare a security binding type");
	
	private RegexMatcher multipleSecutityBindingsProblemMatcher = new RegexMatcher(
			"Authorizer method .* declares multiple security binding types");
	
	@InjectRequirement
	private ServerRequirement sr;
	
	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}
	
	@Test
	public void testInvalidAuthorizerReturnValue() {
		
		String projectName = "invalidAuthorizer1";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				returnValueAuthorizerProblemMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "CustomAuthorizer.java", 9, 0, 
				"return true;");
		replaceInEditor("void", "boolean", true);
		
		new WaitWhile(new SpecificProblemExists(
				returnValueAuthorizerProblemMatcher), TimePeriod.LONG);
		
	}
	
	@Test
	public void testNotDeclaredSecurityBindingInAuthorizer() {
		
		String projectName = "invalidAuthorizer2";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				notDeclaredSecurityBindingProblemMatcher), TimePeriod.LONG);

		insertIntoFile(projectName, "test", "CustomAuthorizer.java", 7, 0, 
				"@CustomSecurityBinding");
		
		new WaitWhile(new SpecificProblemExists(
				notDeclaredSecurityBindingProblemMatcher), TimePeriod.LONG);
		
	}
	
	@Test
	public void testMultipleSecutityBindings() {
		
		String projectName = "multipleSecutityBindings";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				multipleSecutityBindingsProblemMatcher), TimePeriod.LONG);
		
		replaceInEditor(projectName, "test", "CustomAuthorizer.java", "@CustomSecurityBinding2()", "", true);
		
		new WaitWhile(new SpecificProblemExists(
				multipleSecutityBindingsProblemMatcher), TimePeriod.LONG);
		
	}
	
}
