/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.jbpm.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMRequirement.JBPM;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMSuite;
import org.jboss.tools.jbpm.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.jbpm.ui.bot.test.wizard.JBPMProjectWizard;
import org.junit.Test;

@JBPM
@CleanWorkspace
@Perspective(name = "jBPM jPDL 3")
public class JBPMProjectTest extends SWTBotTestCase {

	@Test
	public void createProject() {
		/* Create Project */
		JBPMProjectWizard projectWizard = new JBPMProjectWizard();
		projectWizard.open();
		projectWizard.setName("test").next();
		projectWizard.setRuntime(JBPMSuite.getJBPMRuntime()).finish();

		new ProjectExplorer().getProject("test").getProjectItem("src", "main", "jpdl", "simple.jpdl.xml").open();
		
		Bot.get().editorByTitle("simple");
	
	}

}
