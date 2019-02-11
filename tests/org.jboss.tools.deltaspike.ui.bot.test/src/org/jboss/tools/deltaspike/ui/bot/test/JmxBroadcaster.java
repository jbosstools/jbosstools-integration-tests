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


import java.util.Collection;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.tools.cdi.reddeer.condition.SpecificProblemExists;
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
public class JmxBroadcaster extends DeltaspikeTestBase {

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

	}

}
