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

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.jbpm.ui.bot.test.suite.Project;
import org.jboss.tools.ui.bot.ext.config.Annotations.JBPM;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;
import org.junit.Test;

@Require( clearProjects=false, jbpm=@JBPM(), server=@Server(type=ServerType.SOA,state=ServerState.Present))
public class JBPMDeployTest extends JBPMTest {

	@Test
	public void deploy() {
		// Open process
		PackageExplorer pe = new PackageExplorer();
		pe.openFile(Project.PROJECT_NAME, "src/main/jpdl", "simple.jpdl.xml");
		util.waitForNonIgnoredJobs();
		
		// Deploy
		SWTGefBot gefBot = new SWTGefBot();
		SWTBotGefEditor editor =  gefBot.gefEditor("simple");
		editor.setFocus();
		SWTBotMultiPageEditor multi = new SWTBotMultiPageEditor(editor.getReference(), gefBot);
		multi.activatePage("Deployment");
		
		gefBot.textWithLabel("Server Name:").setText("127.0.0.1");
		SWTBotText item = gefBot.textWithLabel("Server Deployer:").setText("gpd-deployer/upload");
		editor.save();
		
		//gefBot.checkBoxWithLabel("Use credentials").select();
		gefBot.checkBox(3).select();
		gefBot.textWithLabel("Username:").setText("admin");
		gefBot.textWithLabel("Password:").setText("admin");
		
		String serverName = TestConfigurator.currentConfig.getServer().type+"-"+TestConfigurator.currentConfig.getServer().version;
		servers.startServer(serverName);	
		
		item.setFocus();		
		bot.menu("jBPM").menu("Ping Server").click();			

		try {
			bot.sleep(TIME_1S);
			bot.shell("Password Required").activate();
			bot.text("User name:").setText("admin");
			bot.text("Password:").setText("admin");
			bot.button("OK").click();
			bot.sleep(TIME_1S);
		} catch (WidgetNotFoundException e) {
			// do nothing
			log.info("jBPM already authenticated");
		}

		// Confirm ping message dialog
		bot.sleep(TIME_1S);
		bot.shell("Ping Server Successful").activate();
		bot.button("OK").click();
		bot.sleep(TIME_1S);
		

		// Deploy
		bot.menu("jBPM").menu("Deploy Process").click();
		// Confirm deployed message dialog
		bot.sleep(TIME_1S);
		bot.shell("Deployment Successful").activate();
		bot.button("OK").click();
		bot.sleep(TIME_1S);
		
		log.info("Process deployed");
		
		// TODO - check via jpdl console
	}
}
