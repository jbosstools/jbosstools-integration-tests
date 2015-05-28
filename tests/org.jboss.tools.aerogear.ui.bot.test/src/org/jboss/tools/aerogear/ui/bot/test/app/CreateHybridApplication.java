/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.junit.Test;

@CleanWorkspace
public class CreateHybridApplication extends AerogearBotTest {
	@Test
	public void canCreateHTMLHybridApplication() {
		// Project is created within setup method
		assertTrue(new ProjectExplorer().containsProject(CORDOVA_PROJECT_NAME));
		// Check there is no error/warning on Hybrid Mobile Project
		new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
		
		ProblemsView pview = new ProblemsView();
		pview.open();
		
		List<Problem> errors = pview.getProblems(ProblemType.ERROR, new ProblemsPathMatcher(new RegexMatcher("(/" + CORDOVA_PROJECT_NAME + "/)(.*)")));
		List<Problem> warnings = pview.getProblems(ProblemType.WARNING, new ProblemsPathMatcher(new RegexMatcher("(/" + CORDOVA_PROJECT_NAME + "/)(.*)")));
		
		assertTrue("There were these errors for " + CORDOVA_PROJECT_NAME
        + " project " + Arrays.toString(errors.toArray()),
        errors == null || errors.size() == 0);
		
		assertTrue("There were these warnings for " + CORDOVA_PROJECT_NAME
        + " project " + Arrays.toString(warnings.toArray()),
        warnings == null || warnings.size() == 0);
		
	}
}
