package org.jboss.tools.forge.ui.bot.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.junit.Test;

public class EntityTest extends ForgeTest {

	private static final String ENTITY_NAME = "testentity";
	private static final String ENTITY_CREATED = "Created @Entity [" + PACKAGE_NAME + "." + ENTITY_NAME + "]";
	
	@Test
	public void newEntity(){
		
		createProject();
		createPersistence();
		
		getStyledText().setText("entity\n");
		getStyledText().setText(ENTITY_NAME + "\n");
		getStyledText().setText(PACKAGE_NAME + "\n");		
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ENTITY_CREATED , TIME_1S, TIME_20S*3));
		assertTrue(bot.editorByTitle(ENTITY_NAME + ".java").isActive());
		
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();	
	}
	
	
}
