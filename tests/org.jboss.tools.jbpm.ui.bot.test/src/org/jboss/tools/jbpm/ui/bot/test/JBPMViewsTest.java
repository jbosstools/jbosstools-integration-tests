/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.jbpm.ui.bot.test.suite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.junit.Test;

@SWTBotTestRequires( perspective="jBPM jPDL 3",  clearProjects = false )
public class JBPMViewsTest extends JBPMTest {

	@Test
	public void testViews() {
		
		// reset perspective
		bot.resetActivePerspective();
		
		// Open process
		PackageExplorer pe = new PackageExplorer();
		pe.openFile(Project.PROJECT_NAME, "src/main/jpdl", "simple.jpdl.xml");
		util.waitForNonIgnoredJobs();
		
		// check if all views are opened
		IView[] views = { ActionItem.View.JBossjBPMOverviewjBPM3.LABEL, 
				ActionItem.View.GeneralOutline.LABEL,
				ActionItem.View.GeneralProperties.LABEL,
				ActionItem.View.GeneralProjectExplorer.LABEL};
	
		for (IView view : views) {
			// For jBPM Overview titles aren't the same
			if (view.equals(ActionItem.View.JBossjBPMOverviewjBPM3.LABEL)) { 
				assertTrue(eclipse.isViewOpened("Overview")); 
			}
			else {				
				assertTrue(eclipse.isViewOpened(view.getName()));
			}
		}
	}	
}
