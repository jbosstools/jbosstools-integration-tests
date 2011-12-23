/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.SWTBotHyperlinkExt;

public class WSWizardTest extends TestCase{
	protected static SWTBotExt bot = new SWTBotExt();
	public static final SWTOpenExt open = new SWTOpenExt(bot);
	public static final SWTEclipseExt eclipse = new SWTEclipseExt(bot);
	public static final String JBOSSWS_HOME_DEFAULT = "D:/softinstall/jboss-5.1.0.GA";
	public static final String JBOSSWS_42_HOME = "jbosstools.test.jboss.home.5.1";
	public static final String JBOSS_AS_42_HOME = System.getProperty(
			JBOSSWS_42_HOME, JBOSSWS_HOME_DEFAULT);
	IProject project;

	protected void setUp() throws Exception {
		bot.viewByTitle("Welcome").close();
		bot.perspectiveByLabel("Java EE").activate();
		createServerRuntime();
	}
	protected void tearDown() throws Exception {
		bot = null;
	}

	public void createServerRuntime() {
		if (!isServerRuntimeDefined(bot, "AS4.2Runtime")) {
			bot.menu("File").menu("New").menu("Other...").click();
			bot.shell("New").activate();
			SWTBotTree tree = bot.tree();
			bot.sleep(Timing.time1S());
			tree.expandNode("Server").select("Server");
			bot.button("Next >").click();
			SWTBotTree tree2 = bot.tree();
			tree2.expandNode("JBoss Community").select("JBoss AS 5.1");
			bot.textWithLabel("Server name:").setText("AS4.2Server");
			bot.button("Next >").click();
			bot.textWithLabel("Name").setText("AS4.2Runtime");
			bot.textWithLabel("Home Directory").setText(JBOSS_AS_42_HOME);
			bot.button("Finish").click();
			bot.sleep(Timing.time2S());
		}
	}

	public static boolean isServerRuntimeDefined(SWTWorkbenchBot bot,
			String runtimeName) {

		boolean serverRuntimeNotDefined = true;

		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		bot.tree().expandNode("Server").select("Runtime Environments");

		SWTBotTable tbRuntimeEnvironments = bot.table();
		int numRows = tbRuntimeEnvironments.rowCount();
		if (numRows > 0) {
			int currentRow = 0;
			while (serverRuntimeNotDefined && currentRow < numRows) {
				if (tbRuntimeEnvironments.cell(currentRow, 0).equalsIgnoreCase(
						runtimeName)) {
					serverRuntimeNotDefined = false;
				} else {
					currentRow++;
				}
			}
		}

		bot.button("OK").click();

		return !serverRuntimeNotDefined;

	}
	
	
	public IProject importProject() throws CoreException, IOException, InvocationTargetException, InterruptedException {
		TestProjectProvider provider = new TestProjectProvider("org.jboss.tools.ws.ui.bot.test", "/projects/" + "A",
				"A", true);
		IProject prj = provider.getProject();
		return prj;
	}

	public void selectProject() {
		SWTBotHyperlinkExt link = bot.hyperlink(2); 
		if(!"Service project: B".equals(link.getText())){	
		link.click();
			
			SWTBot dBot = bot.activeShell().bot();
			dBot.comboBoxWithLabel("Service project:").setText("B");
			dBot.button("OK").click();
			dBot.sleep(Timing.time1S());
		}
	}
	public void setDefaultWSRuntime(){
		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		SWTBotTree tree = bot.tree();
		tree.expandNode("Web Services").expandNode("Server and Runtime").select();
		bot.comboBoxWithLabel("Web service runtime:").setSelection("JBossWS");
		bot.button("OK").click();
		bot.sleep(Timing.time2S());
	}
}
