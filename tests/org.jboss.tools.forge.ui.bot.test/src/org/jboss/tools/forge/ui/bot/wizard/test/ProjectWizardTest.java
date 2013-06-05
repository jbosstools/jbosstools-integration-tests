package org.jboss.tools.forge.ui.bot.wizard.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeWizardTestBase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.Ignore;
import org.junit.Test;

@Require(clearWorkspace=true, perspective="JBoss")
public class ProjectWizardTest extends ForgeWizardTestBase {

	@Test
	public void createProjectTest(){
		
		SWTBotWizard w = openWizard("New Project");
		w.bot().text(0).setText(W_PROJECT_NAME);
		w.finishWithWait();
		assertTrue(pExplorer.existsResource(W_PROJECT_NAME));
	}
	
	@Test
	public void validProjectNameTest(){
		
		SWTBotWizard w = openWizard("New Project");
		w.bot().text(0).setText(""); 
		if(w.canFinish()){
			w.bot().activeShell().close();
			fail();
		}
		w.close();
	}
	
	@Test
	@Ignore
	public void duplicateProjectNameTest(){
		//TODO
	}
	
	@Test
	@Ignore
	public void forgeConsoleEmptyTest(){
		//TODO
	}
}
