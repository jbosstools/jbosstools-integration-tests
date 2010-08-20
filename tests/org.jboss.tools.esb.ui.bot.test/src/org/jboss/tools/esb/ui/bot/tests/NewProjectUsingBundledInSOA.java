package org.jboss.tools.esb.ui.bot.tests;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.*;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@SWTBotTestRequires(server=@Server(type=ServerType.SOA))
public class NewProjectUsingBundledInSOA extends SWTTestExt{


	
	@Test
	public void newProject() {
		testNewProject("ESBstandalone");
	}
	public void testNewProject(String projectName) {
		SWTBot wiz = open.newObject(ActionItem.NewObject.ESBESBProject.LABEL);
		wiz.textWithLabel(ESBESBProject.TEXT_PROJECT_NAME).setText(projectName);
		wiz.comboBoxInGroup("Target runtime").setSelection(configuredState.getServer().name);
		wiz.comboBoxInGroup("JBoss ESB version").setSelection(SWTTestExt.configuredState.getServer().bundledESBVersion);		
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.button(IDELabel.Button.NEXT).click();
		assertTrue(wiz.comboBox(0).getText().equals(SWTTestExt.configuredState.getEsb().name));
		open.finish(wiz);
		assertTrue(projectExplorer.existsResource(projectName));
		assertTrue(projectExplorer.existsResource(projectName, "JBoss ESB Runtime ["+configuredState.getServer().name+"]"));
		assertTrue(bot.editorByTitle("jboss-esb.xml")!=null);
		
	}
}
