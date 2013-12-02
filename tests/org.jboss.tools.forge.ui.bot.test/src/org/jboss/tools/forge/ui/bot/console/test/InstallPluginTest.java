package org.jboss.tools.forge.ui.bot.console.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace=true)
public class InstallPluginTest extends ForgeConsoleTestBase {

	
	@Test
	public void installJBossAS7Test(){
		
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-jboss-as.git] " + 
									"successfully.";
		installPlugin("jboss-as-7");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_60S*10));
	}
	
	@Test
	public void installHibernateToolsTest(){
		
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-hibernate-tools.git] " + 
									"successfully.";

		installPlugin("hibernate-tools");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_60S*10));
	}		
	
	
}
