package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.forge.reddeer.condition.ForgeConsoleHasText;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Test;

@CleanWorkspace
public class InstallPluginTest extends ForgeConsoleTestBase {

	
	@Test
	public void installJBossAS7Test(){
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-jboss-as.git] " + 
									"successfully.";
		installPlugin("jboss-as-7");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.getCustom(600)); //10m
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
	}
	
	@Test
	public void installHibernateToolsTest(){
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-hibernate-tools.git] " + 
									"successfully.";
		installPlugin("hibernate-tools");
		new WaitUntil(new ForgeConsoleHasText(ASSERT_TEXT), TimePeriod.getCustom(600));//10m
		assertTrue(fView.getConsoleText().contains(ASSERT_TEXT));
	}		
}
