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

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotMultiPageEditor;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.jbpm.ui.bot.test.editor.JBPMEditor;
import org.jboss.tools.jbpm.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMRequirement.JBPM;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMSuite;
import org.jboss.tools.jbpm.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.jbpm.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.jbpm.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.jbpm.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.jbpm.ui.bot.test.wizard.JBPMProjectWizard;
import org.junit.BeforeClass;
import org.junit.Test;

@JBPM
@CleanWorkspace
@Perspective(name = "jBPM jPDL 3")
@Server(type = Type.ALL, state = State.RUNNING)
public class JBPMDeployTest extends SWTBotTestCase {

	public static final String PROJECT = "deploytest";

	@BeforeClass
	public static void createProject() {
		/* Create jBPM3 Project */
		JBPMProjectWizard projectWizard = new JBPMProjectWizard();
		projectWizard.open();
		projectWizard.setName(PROJECT).next();
		projectWizard.setRuntime(JBPMSuite.getJBPMRuntime()).finish();
	}

	@Test
	public void deployTest() {
		/* Open Simple Diagram */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src", "main", "jpdl", "simple.jpdl.xml").open();

		// Deploy
		JBPMEditor editor = new JBPMEditor("simple");
		SWTBotMultiPageEditor multi = new SWTBotMultiPageEditor(editor.getReference(), Bot.get());
		multi.activatePage("Deployment");

		new LabeledText("Server Name:").setText("127.0.0.1");
		new LabeledText("Server Deployer:").setText("gpd-deployer/upload");
		editor.save();

		// gefBot.checkBoxWithLabel("Use credentials").select();
		new CheckBox("Use credentials").click();
		new LabeledText("Username:").setText("admin");
		new LabeledText("Password:").setText("admin");

		editor.show();
		editor.setFocus();
		new ShellMenu("jBPM", "Ping Server").select();

		SWTWorkbenchBot bot = Bot.get();
		try {
			bot.sleep(1 * 1000);
			bot.shell("Password Required").activate();
			bot.text("User name:").setText("admin");
			bot.text("Password:").setText("admin");
			bot.button("OK").click();
			bot.sleep(1 * 1000);
		} catch (WidgetNotFoundException e) {
			// do nothing
		}

		// Confirm ping message dialog
		Bot.get().sleep(1 * 1000);
		Bot.get().shell("Ping Server Successful").activate();
		new PushButton("OK").click();
		Bot.get().sleep(1 * 1000);

		// Deploy
		editor.show();
		editor.setFocus();
		new ShellMenu("jBPM", "Deploy Process").select();

		// Confirm deployed message dialog
		bot.sleep(1 * 100);
		bot.shell("Deployment Successful").activate();
		bot.button("OK").click();
		bot.sleep(1 * 1000);

		// TODO - check via jpdl console
	}
}
