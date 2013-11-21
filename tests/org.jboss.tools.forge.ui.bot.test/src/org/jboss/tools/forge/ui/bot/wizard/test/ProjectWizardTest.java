package org.jboss.tools.forge.ui.bot.wizard.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeWizardTestBase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.Ignore;
import org.junit.Test;

@Require(clearWorkspace=true, perspective="JBoss")
public class ProjectWizardTest extends ForgeWizardTestBase {

	@Test
	public void createProjectTest(){
		
		//New project wizard is accessible only from Entities From Tables wizard
		SWTBotWizard w = openWizard("Entities From Tables"); 
		w.bot().button("New...", 0).click(); //open new project wizard
		
		SWTBotWizard pw = new SWTBotWizard();
		pw.bot().text(0).setText(W_PROJECT_NAME);
		pw.finishWithWait();
		
		w.cancel(); //close Entities From Tables wizard
		assertTrue(pExplorer.existsResource(W_PROJECT_NAME));
	}
	
	@Test
	public void validProjectNameTest(){
		
		//New project wizard is accessible only from Entities From Tables wizard
		SWTBotWizard w = openWizard("Entities From Tables"); 
		w.bot().button("New...", 0).click(); //open new project wizard
		
		SWTBotWizard pw = new SWTBotWizard();
		pw.bot().text(0).setText(""); 
		
		if(pw.canFinish()){
			pw.bot().activeShell().close();
			fail();
		}
		pw.close();
		w.cancel(); //close Entities From Tables wizard
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
