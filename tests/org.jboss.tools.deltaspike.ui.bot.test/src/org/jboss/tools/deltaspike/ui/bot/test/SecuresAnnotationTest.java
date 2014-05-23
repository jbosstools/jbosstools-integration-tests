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
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
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
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class SecuresAnnotationTest extends DeltaspikeTestBase {

	private Regex returnValueAuthorizerProblem = new Regex(
			"Authorizer method .* does not return a boolean");
	
	private Regex notDeclaredSecurityBindingProblem = new Regex(
			"Authorizer method .* does not declare a security binding type");
	
	@InjectRequirement
	private ServerRequirement sr;
	
	@After
	public void closeAllEditors() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}
	
	@Test
	public void testInvalidAuthorizerReturnValue() {
		
		String projectName = "invalidAuthorizer1";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				returnValueAuthorizerProblem), TimePeriod.NORMAL);

		insertIntoFile(projectName, "test", "CustomAuthorizer.java", 9, 0, 
				"return true;");
		replaceInEditor("void", "boolean", true);
		
		new WaitWhile(new SpecificProblemExists(
				returnValueAuthorizerProblem), TimePeriod.NORMAL);
		
	}
	
	@Test
	public void testNotDeclaredSecurityBindingInAuthorizer() {
		
		String projectName = "invalidAuthorizer2";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				notDeclaredSecurityBindingProblem), TimePeriod.NORMAL);

		insertIntoFile(projectName, "test", "CustomAuthorizer.java", 7, 0, 
				"@CustomSecurityBinding");
		
		new WaitWhile(new SpecificProblemExists(
				notDeclaredSecurityBindingProblem), TimePeriod.NORMAL);
		
	}
	
}
