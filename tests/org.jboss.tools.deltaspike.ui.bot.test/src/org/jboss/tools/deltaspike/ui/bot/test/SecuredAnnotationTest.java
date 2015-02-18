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
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
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
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class SecuredAnnotationTest extends DeltaspikeTestBase {

	private Regex ambiguousAuthorizerProblem = new Regex(
			"Ambiguous authorizers found.*");
	
	private Regex noMatchingAuthorizerProblem = new Regex(
			"No matching authorizer found.*");
	
	@InjectRequirement
	private static ServerRequirement sr;
	
	@After
	public void closeAllEditors() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}
	
	@Test
	public void testAmbiguousAuthorizers() {
		
		String projectName = "ambiguousAuthorizers";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				ambiguousAuthorizerProblem), TimePeriod.LONG);

		replaceInEditor(projectName, "test", "SecuredBean.java", "4", "1", true);
		
		new WaitWhile(new SpecificProblemExists(
				ambiguousAuthorizerProblem), TimePeriod.LONG);
		
		TextEditor e = new TextEditor("SecuredBean.java");
		e.selectText("doSomething");
		ContentAssistant ca = e.openOpenOnAssistant();
		ca.chooseProposal("Open authorizer method CustomAuthorizer.check3()");
		new TextEditor("CustomAuthorizer.java");
	}
	
	@Test
	public void testNotDeclaredSecurityBindingInAuthorizer() {
		
		String projectName = "noAuthorizer";
		importDeltaspikeProject(projectName,sr);
		
		new WaitUntil(new SpecificProblemExists(
				noMatchingAuthorizerProblem), TimePeriod.LONG);

		replaceInEditor(projectName, "test", "SecuredBean.java", "1", "4", true);
		
		new WaitWhile(new SpecificProblemExists(
				noMatchingAuthorizerProblem), TimePeriod.LONG);
		
		TextEditor e = new TextEditor("SecuredBean.java");
		e.selectText("doSomething");
		ContentAssistant ca = e.openOpenOnAssistant();
		ca.chooseProposal("Open authorizer method CustomAuthorizer.check()");
		new TextEditor("CustomAuthorizer.java");
		
	}
	
}
