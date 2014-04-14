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

import java.io.File;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.Test;

@Require(clearWorkspace = true)
public class CreateHybridApplication extends AerogearBotTest {
	@Test
	public void canCreateHTMLHybridApplication() {
	  SWTBotExt botExt = new SWTBotExt();
	  // Project is created within setup method
		assertTrue(projectExplorer.existsResource(CORDOVA_PROJECT_NAME));
		// Check there is no error/warning on Hybrid Mobile Project
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
    SWTBotTreeItem[] errors = ProblemsView.getFilteredErrorsTreeItems(botExt,
        null, File.separator + CORDOVA_PROJECT_NAME, null, null);
    assertTrue("There were these errors for " + CORDOVA_PROJECT_NAME
        + " project " + SWTEclipseExt.getFormattedTreeNodesText(errors),
        errors == null || errors.length == 0);
    SWTBotTreeItem[] warnings = ProblemsView.getFilteredWarningsTreeItems(
        botExt, null, File.separator + CORDOVA_PROJECT_NAME, null, null);
    assertTrue("There were these warnings for " + CORDOVA_PROJECT_NAME
        + " project " + SWTEclipseExt.getFormattedTreeNodesText(warnings),
        warnings == null || warnings.length == 0);
	}
}
