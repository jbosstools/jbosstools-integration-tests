package org.jboss.tools.mylyn.ui.bot.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Test;

public class MylynTest {
	
	@Test
	public void TestIt() {
	
	List<String> expectedTasks = Arrays.asList ("Local");
	List<String> expectedBugs = Arrays.asList("Eclipse.org", "Red Hat Bugzilla", "Atlassian Integrations Support", "JBoss Community");
	
	SWTWorkbenchBot bot = new SWTWorkbenchBot();
	
	bot.shell("JBoss Developer Studio Usage").bot().button("Yes").click();
		
	// JBDS50_XXXX Mylyn plugin can be installed from Central without errors 
	bot.menu("Window").menu("Show View").menu("Other...").click();
	SWTBotShell shell = bot.shell("Show View");  
	shell.activate();
	
	bot.sleep(10000l);
	
	/* Verify that the expected repos are defined */
	bot.tree().getTreeItem("Mylyn").expand().getNode("Task Repositories").select().click();
	bot.button("OK").click();
	
	assertEquals ("Expecting 2 groups of MyLyn Task Repos", bot.viewByTitle("Task Repositories").bot().tree().getAllItems().length, 2);
	assertEquals ("Expecting Bugs group of MyLyn Task Repos", bot.viewByTitle("Task Repositories").bot().tree().getAllItems()[0].getText(), "Tasks");
	assertEquals ("Expecting Tasks group of MyLyn Task Repos", bot.viewByTitle("Task Repositories").bot().tree().getAllItems()[1].getText(), "Bugs");
	
	SWTBotTreeItem [] theTaskitems = bot.viewByTitle("Task Repositories").bot().tree().getAllItems()[0].getItems();			
	List<SWTBotTreeItem> itemList = Arrays.asList(theTaskitems);
	for (SWTBotTreeItem item : itemList) {
		assertTrue ("Looking for task group " + item.getText(), expectedTasks.contains(item.getText()));
	}
	
	SWTBotTreeItem [] theBugitems = bot.viewByTitle("Task Repositories").bot().tree().getAllItems()[1].getItems();			
	itemList = Arrays.asList(theBugitems);
	for (SWTBotTreeItem item : itemList) {
		assertTrue ("Looking for bug group " + item.getText(), expectedBugs.contains(item.getText()));
	}		

	// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin 
	// JBDS50_0140 Red Hat Bugzilla task repository is available and can be connected 		
//	bot.viewByTitle("Task Repositories").bot().tree().getTreeItem("Bugs").getNode("Red Hat Bugzilla").select().click().contextMenu("Properties").click();	
	bot.viewByTitle("Task Repositories").bot().tree().getTreeItem("Bugs").getNode("Red Hat Bugzilla").select().click();
	bot.menu("File").menu("Properties").click();
	
	shell = bot.shell("Properties for Task Repository"); 
	
	shell.getText();
	SWTBot thebot = shell.bot();
	thebot.button ("Validate Settings").click();

	while (!thebot.textWithLabel("Label: ").isEnabled()) {
		bot.sleep(3000l);
	}		
	assertTrue ("Repo Connection Properties Invalid", thebot.textWithLabel("Bugzilla Repository Settings").getText().contains("Repository is valid."));
	bot.button("Cancel").click();
	
	// JBDS50_XXXX Local tasks can be created, viewed and deleted 
	// JBDS50_XXXX Task-Focused UI works 
	
//	bot.viewByTitle("Task Repositories").bot().tree().getTreeItem("Tasks").getNode("Local").contextMenu("New Task...").click();
	bot.viewByTitle("Task Repositories").bot().tree().getTreeItem("Tasks").getNode("Local").click();
	bot.menu("File").menu("New").menu("Other...").click();
	shell = bot.shell("New");  
	shell.activate();
	
	bot.sleep(10000l);
	
	/* Verify that the expected repos are defined */
	bot.tree().getTreeItem("Tasks").expand().getNode("Task").select().click();
	bot.button("Next >").click();
	
	bot.sleep(10000l);
	bot.tree().getTreeItem("Local").select().click();
	bot.sleep(10000l);
	
	bot.button("Finish").click();
	
	bot.sleep(30000l);
	
	bot.styledText("New Task").setText("A new deal");
	bot.styledText("Notes").setText("A loooooong comment");
	bot.sleep(30000l);
	
	bot.menu("File").menu("Save").click();
	bot.sleep(30000l);
	
	}

}
