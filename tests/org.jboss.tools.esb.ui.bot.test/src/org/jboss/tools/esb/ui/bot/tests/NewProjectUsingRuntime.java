package org.jboss.tools.esb.ui.bot.tests;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.ESB;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.AfterClass;
import org.junit.Test;

@SWTBotTestRequires(esb=@ESB())
public class NewProjectUsingRuntime extends SWTTestExt{

	@AfterClass
	public static void waitAMinute() {
	//	bot.sleep(Long.MAX_VALUE);
	}
	
	@Test
	public void newProject() {
		testNewProject("ESB");
	}
	public static void testNewProject(String projectName) {
		SWTBot wiz = open.newObject(ActionItem.NewObject.ESBESBProject.LABEL);
		wiz.textWithLabel(ESBESBProject.TEXT_PROJECT_NAME).setText(projectName);
		wiz.comboBoxInGroup("JBoss ESB version").setSelection(SWTTestExt.configuredState.getEsb().version);		
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.radio(1).click();
		assertTrue(wiz.comboBox(0).getText().equals(SWTTestExt.configuredState.getEsb().name));
		open.finish(wiz);
		assertTrue(projectExplorer.existsResource(projectName));
		assertTrue(bot.editorByTitle("jboss-esb.xml")!=null);
	}
}
