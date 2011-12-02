package org.jboss.tools.forge.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.junit.Test;

/**
 * 
 * @author psrna
 *
 */
public class ProjectTest extends ForgeTest {

	private static final String PROJECT_NAME = "testproject";
	private static final String PACKAGE_NAME = "org.jboss.testproject";
	
	@Test
	public void newProject() {
		
		openForgeView();
		startForge();
		clear();
		
		getStyledText().setText("new-project\n");
		getStyledText().setText(PROJECT_NAME + "\n");
		getStyledText().setText(PACKAGE_NAME + "\n");
		getStyledText().setText("Y\n");
		
		bot.sleep(TIME_10S);
		
		String text = getStyledText().getText();
		assertTrue(text.contains("***SUCCESS*** Created project [" + PROJECT_NAME + "]"));
		assertTrue(pExplorer.existsResource(PROJECT_NAME));
		
	}
	
}
