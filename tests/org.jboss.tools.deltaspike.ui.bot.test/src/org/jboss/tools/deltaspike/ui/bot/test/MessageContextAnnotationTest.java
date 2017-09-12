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
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.After;
import org.junit.Test;

/**
 * Test @MessageContext annotation, with following approach:
 * 
 * 1. injecting MessageContext will not fail -> no validation error 2. try to
 * open on MessageContext -> MessageContextProducer should be opened
 * 
 * @author jjankovi
 * 
 */
public class MessageContextAnnotationTest extends DeltaspikeTestBase {

	private RegexMatcher validationProblemRegexMatcher = new RegexMatcher("No bean is eligible.*");

	
	@InjectRequirement
	private ServerRequirement sr;
	
	@After
	public void closeAllEditors() {
		deleteAllProjects();
	}

	@Test
	public void testInjectMessageContext() {

		String projectName = "messageContext";
		importDeltaspikeProject(projectName,sr);

		new WaitWhile(new SpecificProblemExists(validationProblemRegexMatcher),
				TimePeriod.LONG);
		openClass(projectName, "test", "Test.java");
		TextEditor e = new TextEditor("Test.java");
		e.selectText("messageContext");
		ContentAssistant ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains("Open @Inject Bean")){
				ca.chooseProposal(p);
				break;
			}
		}
		new TextEditor("DefaultMessageContext.class");

	}

}
