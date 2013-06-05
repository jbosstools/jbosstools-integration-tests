package org.jboss.tools.forge.ui.bot.test.suite;


import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author psrna
 *
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present))
public class ForgeWizardTestBase extends SWTTestExt {
	
	public static String W_PROJECT_NAME = "test_wizard_project";
	
	protected static ProjectExplorer pExplorer = null;
	
	@BeforeClass
	public static void setup(){
		pExplorer = new ProjectExplorer();
		ForgeConsoleTestBase.openForgeView();
	}
	
	@AfterClass
	public static void cleanup(){
		pExplorer = new ProjectExplorer();
		pExplorer.deleteAllProjects();
		bot.sleep(TIME_5S);
	}

	public SWTBotWizard openWizard(String name){
		SWTBotNewObjectWizard w = new SWTBotNewObjectWizard();
		w.open(name, "JBoss Tools");
		util.waitForNonIgnoredJobs();
		return w;
	}
	
}
