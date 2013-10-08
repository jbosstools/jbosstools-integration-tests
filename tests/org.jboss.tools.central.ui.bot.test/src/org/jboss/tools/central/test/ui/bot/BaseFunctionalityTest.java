package org.jboss.tools.central.test.ui.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.editor.Editor;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.tools.central.test.ui.bot.helper.SWTJBossCentralEditorExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.BrowserIsLoaded;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Perspective;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({CentralAllBotTests.class})
public class BaseFunctionalityTest extends SWTTestExt {
	
	/**
	 * Close usage report window
	 */
	@BeforeClass
	public static void setup(){
		open.perspective(Perspective.JAVA.LABEL);
		util.closeAllEditors(false);
		bot.menu("Help").menu(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
	}
	
	/**
	 * Tests whether JBoss central is accessible from Help menu
	 */
	@Test
	public void testIsInstalled(){
		try {
			bot.menu("Help").menu(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
		} catch (WidgetNotFoundException e) {
			e.printStackTrace(System.out);
			assertTrue("JBoss Cenral isn't in menu Help", false);
		}
		//JBoss Central should be visible right now
		assertTrue("JBoss Central is not active",bot.editorByTitle(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).isActive());
		bot.editorByTitle(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).close();
		try {
			bot.toolbarButtonWithTooltip(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
		}catch (WidgetNotFoundException e) {
			assertTrue("JBoss Central isn't accessible through toolbar", false);
		}
		assertTrue("JBoss Central is not active",bot.editorByTitle(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).isActive());
	}
	
	@Test
	public void homeWebTest(){
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		assertTrue("JBoss Central is not active",editor.isActive());
		SWTBotToolbarPushButton button = (SWTBotToolbarPushButton) editor.bot().toolbarButtonWithTooltip("JBoss Developer Studio Home");
		button.click();
		checkWebPage();
	}
	
	@Test
	public void newsWebTest(){
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		assertTrue("JBoss Central is not active",editor.isActive());
		SWTBotToolbarPushButton button = (SWTBotToolbarPushButton) editor.bot().toolbarButtonWithTooltip("News");
		button.click();
		checkWebPage();
	}
	
	@Test
	public void blogsWebTest(){
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		assertTrue("JBoss Central is not active",editor.isActive());
		SWTBotToolbarPushButton button = (SWTBotToolbarPushButton) editor.bot().toolbarButtonWithTooltip("JBoss Buzz");
		button.click();
		checkWebPage();
	}
	
//	@Test JBIDE-12145 Open twitter in internal browser
	public void twitterWebTest(){
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		assertTrue("JBoss Central is not active",editor.isActive());
		SWTBotToolbarPushButton button = (SWTBotToolbarPushButton) editor.bot().toolbarButtonWithTooltip("Twitter");
		button.click();
		checkWebPage();
	}
	
	@Test
	public void preferencesButtonTest(){
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		assertTrue("JBoss Central is not active",editor.isActive());
		editor.bot().toolbarButtonWithTooltip("Preferences").click();
		bot.waitUntil(new ShellIsActiveCondition("Preferences"));
		assertEquals("Preferences", bot.activeShell().getText());
		bot.activeShell().close();
	}
	
	@Test
	public void searchTest(){
		DefaultEditor e = new DefaultEditor();
		e.maximize();
		SWTJBossCentralEditorExt editor = new SWTJBossCentralEditorExt(bot.editorByTitle("JBoss Central").getReference(), bot);
		editor.setSearchText("test search string");
		assertTrue(editor.getSearchText().equals("test search string"));
		editor.performSearch();
		checkWebPage();
		e.maximize();
	}
	
	private void checkWebPage(){
		SWTBotBrowserExt browser = bot.browserExt();
		bot.waitUntil(new BrowserIsLoaded(browser), TaskDuration.LONG.getTimeout());
		Pattern pattern = Pattern.compile(".*<body></body>.*", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(browser.getText());
		assertFalse("Page cannot be empty", matcher.matches());
		bot.editorByTitle(bot.activeEditor().getTitle()).close();
	}
}
