package org.jboss.tools.forge.ui.bot.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
public class ForgeViewTest extends ForgeTest {
	
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
