package org.jboss.tools.forge.ui.bot.console.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace=true)
public class InstallPluginTest extends ForgeConsoleTestBase {

	private void prepare(){
		cdWS();
		clear();
	}
	
	@Test
	public void installJBossAS7Test(){
		
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-jboss-as.git] " + 
									"successfully.";
		prepare();
		installPlugin("jboss-as-7");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_20S*3));
	}
	
	@Test
	public void installHibernateToolsTest(){
		
		final String ASSERT_TEXT =  "***SUCCESS*** Installed from " + 
									"[https://github.com/forge/plugin-hibernate-tools.git] " + 
									"successfully.";
		
		prepare();
		installPlugin("hibernate-tools");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_20S*3));
	}		
	
	
}
