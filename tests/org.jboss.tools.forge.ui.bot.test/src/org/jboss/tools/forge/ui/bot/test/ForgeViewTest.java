package org.jboss.tools.forge.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.part.PageBook;
import org.hamcrest.Matcher;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
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
	
	
	public void forgeStartStop() {
		
		if(isForgeRunning())
			stopForge();

		startForge();
		assertTrue(isForgeRunning());
		stopForge();
		assertTrue(!isForgeRunning());
	}
	
}
