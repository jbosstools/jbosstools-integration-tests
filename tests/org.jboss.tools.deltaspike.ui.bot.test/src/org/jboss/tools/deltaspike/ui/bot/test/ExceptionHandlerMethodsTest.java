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
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
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
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class ExceptionHandlerMethodsTest extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex("Parameter of a handler method must be a " +
			"ExceptionEvent.*");
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}
	
	@Test
	public void testHandlesMethods() {
		
		String projectName = "handles";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.LONG);
		
	}
	
	@Test
	public void testBeforeHandlesMethods() {
		
		String projectName = "before-handles";
		importDeltaspikeProject(projectName,sr);

		new WaitUntil(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.LONG);
		
	}
	
}
