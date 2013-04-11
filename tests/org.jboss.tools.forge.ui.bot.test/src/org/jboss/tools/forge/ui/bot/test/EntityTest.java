package org.jboss.tools.forge.ui.bot.test;

import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace=true)
public class EntityTest extends ForgeTest {

	private static final String ENTITY_CREATED = "Created @Entity [" + PACKAGE_NAME + "." + ENTITY_NAME + "]";
	private static final String FIELD_ADDED = "Added field to " + PACKAGE_NAME  + "." +
								ENTITY_NAME + ": @Column private String " + FIELD_NAME + ";";
	
	@Test
	public void newEntityTest(){
				
		createProject();
		createPersistence();
		createEntity(ENTITY_NAME, PACKAGE_NAME);
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole(ENTITY_CREATED , TIME_1S, TIME_20S*3));
		assertTrue(bot.editorByTitle(ENTITY_NAME + ".java").isActive());
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();	
	}
	
	@Test
	public void newFieldTest(){
		
		createProject();
		createPersistence();
		createEntity();
		createStringField(FIELD_NAME);
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole(FIELD_ADDED , TIME_1S, TIME_20S*3));
		assertTrue(bot.editorByTitle(ENTITY_NAME + ".java").isActive());
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		String packagePath = PACKAGE_NAME.replace(".", "/");
		String entityFilePath = projectLocation + "/src/main/java/" +	
								packagePath + "/" + ENTITY_NAME + ".java";
		
		try {
			String entityContent = ResourceUtils.readFile(entityFilePath);	
			assertTrue(entityContent.contains("private String " + FIELD_NAME + ";"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the '" + entityFilePath + "' failed!");
		}		
		
		SWTBotView outline = bot.viewByTitle("Outline");
		outline.show();
		
		SWTBotTreeItem entityItem = outline.bot().tree().getTreeItem(ENTITY_NAME);
		SWTBotTreeItem fieldItem = entityItem.getNode(FIELD_NAME + " : String");
		
		assertTrue(fieldItem.isSelected());

		cdWS();
		clear();
		pExplorer.deleteAllProjects();
		
	}
	
}
