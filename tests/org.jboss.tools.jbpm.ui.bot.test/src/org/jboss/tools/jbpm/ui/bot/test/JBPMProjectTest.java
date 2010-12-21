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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.jbpm.ui.bot.test.suite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.junit.Test;

@SWTBotTestRequires(jbpm=@JBPM, perspective = "jBPM jPDL 3", clearProjects = false)
public class JBPMProjectTest extends JBPMTest {

	@Test
	public void createProject() {

		// Create project
		eclipse.createNew(EntityType.JBPM3_PROJECT);
		bot.textWithLabel("Project name:").setText(Project.PROJECT_NAME);
		bot.clickButton(IDELabel.Button.NEXT);

		String rtName = "JBPM-"
				+ TestConfigurator.currentConfig.getJBPM().version;

		// There is a bug related to undefined runtime even if it's defined (FIXED)		
		// bot.textWithLabel("Name :").setText(rtName);
		// String rtHome = TestConfigurator.currentConfig.getJBPM().jbpmHome;
		// bot.textWithLabel("Location :").setText(rtHome);
		
		bot.comboBox(0).setSelection(rtName);		
		String msg3 = "Press next to continue the project creation";

		/*
		try {
			bot.text(msg3);
		} catch (WidgetNotFoundException e) {
			fail("Missing confirmation during jbpm runtime definition text");
		}
		*/

		//bot.clickButton(IDELabel.Button.NEXT);
		//bot.clickButton(IDELabel.Button.FINISH);

		bot.comboBox().setSelection(rtName);

		SWTBotShell wizard = bot.activeShell();
		bot.clickButton(IDELabel.Button.FINISH);
		util.waitForNonIgnoredJobs();
		eclipse.waitForClosedShell(wizard);
	}

	@Test
	public void openProcess() {
		PackageExplorer pe = new PackageExplorer();
		pe.openFile(Project.PROJECT_NAME, "src/main/jpdl", "simple.jpdl.xml");
		util.waitForNonIgnoredJobs();
	}
}
