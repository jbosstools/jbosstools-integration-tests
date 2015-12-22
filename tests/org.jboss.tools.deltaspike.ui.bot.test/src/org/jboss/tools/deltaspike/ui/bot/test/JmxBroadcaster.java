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
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * 1. injecting JmxBroadcaster will not fail -> no validation error 2. try to
 * open on JmxBroadcaster -> BroadcasterProducer should be opened
 * 
 * @author jjankovi
 * 
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class JmxBroadcaster extends DeltaspikeTestBase {

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher("No bean is eligible.*");
	
	@InjectRequirement
	private ServerRequirement sr;

	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}

	@Test
	public void testJmxBroadcaster() {

		String projectName = "jmxBroadcaster";
		importDeltaspikeProject(projectName,sr);

		new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);
		openClass(projectName, "test", "Test.java");
		TextEditor e = new TextEditor("Test.java");
		e.selectText("jmxBroadcaster");
		ContentAssistant ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains("Open @Inject Bean")){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("BroadcasterProducer.class");
		
		try{
			new DefaultShell("Found source jar for 'deltaspike-core-impl-0.4.jar'");
			new PushButton("No").click();
			new WaitWhile(new ShellWithTextIsAvailable("Found source jar for 'deltaskipe-core-impl-0.4.jar'"));
		} catch (SWTLayerException ex){
			//do nothing because maven source lookup is not installed
		}

	}

}
