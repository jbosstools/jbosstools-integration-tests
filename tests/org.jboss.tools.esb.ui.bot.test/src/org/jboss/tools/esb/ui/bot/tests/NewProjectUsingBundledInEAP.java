package org.jboss.tools.esb.ui.bot.tests;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ESBESBProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * tests warning message, which should be displayed by new ESB project wizard, when user 
 * selects server runtime with non-bundled ESB
 * @author lzoubek
 *
 */
@Require(server=@Server(type=ServerType.EAP,state=ServerState.Present))
public class NewProjectUsingBundledInEAP extends SWTTestExt {

	@AfterClass
	public static void waitAMinute() {
		//bot.sleep(Long.MAX_VALUE);
	}
	@Test
	public void newProject() {
		testNewProject("ESBwithWarning");
	}
	public void testNewProject(String projectName) {
		SWTBot wiz = open.newObject(ActionItem.NewObject.ESBESBProject.LABEL);
		wiz.textWithLabel(ESBESBProject.TEXT_PROJECT_NAME).setText(projectName);
		wiz.comboBoxInGroup("Target runtime").setSelection(configuredState.getServer().name);		
		wiz.button(IDELabel.Button.NEXT).click();
		wiz.button(IDELabel.Button.NEXT).click();
		try {
		String text = wiz.text(1).getText();
		assertTrue("New ESB project wizard did not warn, when using runtime without ESB",text.contains("does not contain a valid ESB"));
		log.info(text);
		} catch (IndexOutOfBoundsException ex) {
			assertTrue("New ESB project wizard did not warn, when using runtime without ESB",false);
		}
		wiz.radio(1).click();
		
		open.closeCancel(wiz);
		
	}
}
