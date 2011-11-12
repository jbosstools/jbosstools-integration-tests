package org.jboss.tools.esb.ui.bot.tests;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Present))
public class NewProjectUsingBundledInSOA extends SWTTestExt{


	
	@Test
	public void newProject() {
		testNewProject("ESBstandalone");
	}
	public void testNewProject(String projectName) {
		
		/* This test updated for JBDS-1927 - ldimaggi - Nov 11 2011 */
		
        SWTBot wiz = open.newObject(ActionItem.NewObject.ESBESBProject.LABEL);
		wiz.textWithLabel(ESBESBProject.TEXT_PROJECT_NAME).setText(projectName);
		wiz.comboBoxInGroup("Target runtime").setSelection(configuredState.getServer().name);
		wiz.comboBoxInGroup("JBoss ESB version").setSelection(SWTTestExt.configuredState.getServer().bundledESBVersion);
		
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.button(IDELabel.Button.NEXT).click();

		/* Radio button indicating that server supplied ESB runtime is used */
		//System.out.println ("DEBUG = " + wiz.radio(0).isSelected() );
		assertTrue(wiz.radio(0).isSelected());
		 
		wiz.sleep(3000l);
		org.jboss.tools.ui.bot.ext.SWTUtilExt.displayAllBotWidgets(wiz);   
		wiz.button("Finish").click();
		wiz.sleep(3000l);	
		
		/* Locate the SOA-P server */
		SWTBot serv = servers.bot();
		SWTBotTree servTree = serv.tree();		
		servTree.select ("SOA-5.2  [Stopped]").contextMenu("Add and Remove...").click();
		
		/* Deploy the ESB project to the server */		
		bot.button ("Add All >>").click();		
		bot.sleep(3000l);
		
		/* There should only be one project deployed to the server */ 
		assertTrue (bot.tree(1).getAllItems().length == 1);
		
		/* And it is the ESB project */				
		assertTrue (projectName.equals(bot.tree(1).getTreeItem(projectName).getText()));
		
		bot.button("Finish").click();
		
		/* Check for JBDS-1927 */
		assertFalse (bot.label(1).getText().equals("There are no resources that can be added or removed from the server."));
		wiz.sleep(3000l);
		
		/* Verify that the ESB editor can open jboss-esb.xml */		
		assertTrue(projectExplorer.existsResource(projectName));
//		assertTrue(projectExplorer.existsResource(projectName, "JBoss ESB Runtime ["+configuredState.getServer().name+"]"));
		assertTrue(bot.editorByTitle("jboss-esb.xml")!=null);
		
	} /* test method */

} /* class */
