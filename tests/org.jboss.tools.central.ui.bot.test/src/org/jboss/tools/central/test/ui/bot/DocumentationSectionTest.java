package org.jboss.tools.central.test.ui.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.BrowserIsLoaded;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentationSectionTest extends SWTTestExt {

	@BeforeClass
	public static void setup(){
		bot.menu("Help").menu(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
		util.waitForAll();
	}
	
	@Test
	public void documentationSectionTest(){
		//Commented out because of bug in eclipse Juno on RHEL 6.3 https://bugs.eclipse.org/bugs/show_bug.cgi?id=387213
		//testHyperlinkToBrowser("Wiki");
		testHyperlinkToBrowser("JBoss developer website");
		testHyperlinkToBrowser("User Forum");
		testHyperlinkToBrowser("Developer Forum");
		testHyperlinkToBrowser("Product documentation");
		testHyperlinkToBrowser("Videos");
	}
	
	private void testHyperlinkToBrowser(String hyperlinkText){
		bot.hyperlink(hyperlinkText).click();
		SWTBotBrowserExt browser = bot.browserExt();
		bot.waitUntil(new BrowserIsLoaded(browser), TaskDuration.LONG.getTimeout());
		assertFalse("JBoss Central sould not be active editor right now", bot.activeEditor().getTitle().equals("JBoss Central"));
		//And also can't be empty page
		Pattern pattern = Pattern.compile(".*<body></body>.*", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(browser.getText());
		assertFalse("Page cannot be empty", matcher.matches());
		bot.activeEditor().close();
	}
	
	
}
