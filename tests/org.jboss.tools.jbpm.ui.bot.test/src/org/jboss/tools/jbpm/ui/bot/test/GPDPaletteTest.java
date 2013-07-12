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
import org.jboss.tools.jbpm.ui.bot.test.editor.JBPMEditor;
import org.jboss.tools.jbpm.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMRequirement.JBPM;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMSuite;
import org.jboss.tools.jbpm.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.jbpm.ui.bot.test.wizard.JBPMProjectWizard;
import org.junit.BeforeClass;
import org.junit.Test;

@JBPM
@CleanWorkspace
@Perspective(name = "jBPM jPDL 3")
public class GPDPaletteTest extends SWTBotTestCase {

	public static final String PROJECT = "palettetest";

	@BeforeClass
	public static void createProject() {
		/* Create jBPM3 Project */
		JBPMProjectWizard projectWizard = new JBPMProjectWizard();
		projectWizard.open();
		projectWizard.setName(PROJECT).next();
		projectWizard.setRuntime(JBPMSuite.getJBPMRuntime()).finish();
	}

	@Test
	public void insertNodes() {
		/* Open Simple Diagram */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src", "main", "jpdl", "simple.jpdl.xml").open();

		String[] entities = { "Select", "Start", "State", "End", "Fork", "Join", "Decision", "Node", "Task Node",
				"Mail Node", "ESB Service", "Process State", "Super State", "Transition" };

		/* Add All Entities */
		JBPMEditor editor = new JBPMEditor("simple");
		for (int i = 0; i < entities.length; i++) {
			int x = 100;
			int y = 100 + 10 * i;
			String entity = entities[i];
			editor.insertEntity(entity, x, y);
		}
		editor.save();
	}
}
