package org.jboss.tools.forge.ui.bot.test;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.junit.Test;

@Require(clearWorkspace=true)
public class CommandTest extends ForgeTest {

	private void prepare(){
		cdWS();
		clear();
	}
	
	@Test
	public void cdWorkspaceTest(){
		
		String ws = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		
		prepare();
		createProject();
		assertTrue(!pwd().equals(ws));
		cdWS();
		assertTrue(pwd().equals(ws));
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();
		
	}
	
	@Test
	public void cdHomeTest(){
		
		String userHome = System.getProperty("user.home");
		
		prepare();
		assertTrue(!pwd().equals(userHome));
		getStyledText().setText("cd $\n");
		assertTrue(pwd().equals(userHome));
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();
		
	}
	
	@Test
	public void pickupDirectoryTest(){
		
		String ASSERT_TEXT = "Picked up type <DirectoryResource>: main";
		
		prepare();
		createProject();
		cdWS();
	
		getStyledText().setText("pick-up " + PROJECT_NAME + "/src/main" + "\n");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_20S*3));
		
		SWTBotTreeItem project = pExplorer.bot().tree().getTreeItem(PROJECT_NAME);		
		SWTBotTreeItem src = project.getNode("src");
		SWTBotTreeItem main = src.getNode("main");
	
		assertTrue(main.isSelected());
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();
	}
	
	@Test
	public void pickupFileTest(){
		
		String ASSERT_TEXT = "Picked up type <MavenPomResource>: pom.xml";
		
		prepare();
		createProject();
		cdWS();

		getStyledText().setText("pick-up " + PROJECT_NAME + "/pom.xml" + "\n");
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ASSERT_TEXT, TIME_1S, TIME_20S*3));
		assertTrue(bot.editorByTitle(PROJECT_NAME + "/pom.xml").isActive());
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();
	}
	
}