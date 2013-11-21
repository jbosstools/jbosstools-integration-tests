package org.jboss.tools.forge.ui.bot.test.suite;


import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.After;
import org.junit.Before;
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
	protected static final long WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT = TIME_60S*5;
	
	protected static ProjectExplorer pExplorer = null;
	
	@Before
	public void setup(){
		pExplorer = new ProjectExplorer();
		ForgeConsoleTestBase.openForgeView();
	}
	
	@After
	public void cleanup(){
		pExplorer = new ProjectExplorer();
		pExplorer.deleteAllProjects();
		bot.sleep(TIME_5S);
	}

	public SWTBotWizard openWizard(String name){
		SWTBotNewObjectWizard w = new SWTBotNewObjectWizard();
		w.open(name, "JBoss Tools", "Forge");
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
		return w;
	}
	
	
}
