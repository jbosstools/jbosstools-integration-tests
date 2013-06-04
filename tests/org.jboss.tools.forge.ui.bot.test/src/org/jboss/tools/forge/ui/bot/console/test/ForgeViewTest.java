package org.jboss.tools.forge.ui.bot.console.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
@Require(clearWorkspace=true)
public class ForgeViewTest extends ForgeConsoleTestBase {
	
	@Test
	public void forgeViewPresent () {
		openForgeView();
		assertTrue(isForgeViewActive());
			
	}
	
	@Test
	public void forgeStartStop() {
		
		if(isForgeRunning())
			stopForge();

		startForge();
		assertTrue(isForgeRunning());
		stopForge();
		assertTrue(!isForgeRunning());
	}
	
}
