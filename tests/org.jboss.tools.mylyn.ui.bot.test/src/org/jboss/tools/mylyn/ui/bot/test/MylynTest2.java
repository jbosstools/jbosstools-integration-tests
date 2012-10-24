package org.jboss.tools.mylyn.ui.bot.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;

public class MylynTest2 {
	
	protected final Logger log = Logger.getLogger(this.getClass());
	protected final String expectedMylynElements[] = {"Tasks", "Local", "Bugs", "Eclipse.org", "Red Hat Bugzilla", "Atlassian Integrations Support", "JBoss Community"};
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";
	protected final long DELAY = 3000l;
		
	/* To locate one item by name in a list */
	public void selectTreeItem (List <TreeItem> theTreeItems, String matchingName) {
		for (TreeItem item : theTreeItems) {
			log.info(item.getText());
			if (item.getText().equals(matchingName)) {
				item.select();
				break;
			}
		}			
	} /* method */	
	
	@Test
	public void TestIt() {
		
	WorkbenchShell ws = new WorkbenchShell();
	
	log.info ("*** Step 1 - Close the Usage Shell");	
	new DefaultShell("JBoss Developer Studio Usage");   
	Bot.get().sleep(DELAY);
	new PushButton("Yes").click();
	
	log.info ("*** Step 2 - Open the Mylyn View");	
	//Bot.get().sleep(DELAY);
	////new DefaultShell("JBoss - JBoss Developer Studio");
    
	// JBDS50_XXXX Mylyn plugin can be installed from Central without errors 
	//Bot.get().sleep(DELAY);
	new ShellMenu("Window", "Show View" , "Other...").select();
	new DefaultShell("Show View");

	/* Verify that the expected repos are defined */	
	log.info ("Step 3 - Verify that the Mylyn Features are Present");	
	DefaultTree FeatureTree = new DefaultTree();
	List <TreeItem> featureItems = FeatureTree.getAllItems();
	selectTreeItem (featureItems, "Task Repositories");
	new PushButton("OK").click();
	
	/* We need the feature names in an array of strings */
	ArrayList<String> repoList = new ArrayList<String>();
	DefaultTree RepoTree = new DefaultTree();
	List <TreeItem> repoItems = RepoTree.getAllItems();
	int i = 0;
	for (TreeItem item : repoItems) {
		log.info(item.getText());
		repoList.add(i++, item.getText());
	}
	
	assertEquals ("Expecting 7 MyLyn items", repoItems.size(), 7);
	for (String elementName : expectedMylynElements) {
		assertTrue ("Mylyn element list incorrect", repoList.contains(elementName));
	}

	// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin 
	// JBDS50_0140 Red Hat Bugzilla task repository is available and can be connected 		
	log.info ("Step 4 - Validate connection to the Red Hat Bugzilla repo");	
	//Bot.get().sleep(DELAY);
	
	int elementIndex = repoList.indexOf("Red Hat Bugzilla");
	repoItems.get(elementIndex).doubleClick();
	//Bot.get().sleep(DELAY);
	
	new DefaultShell("Properties for Task Repository");
	log.info(new PushButton("Validate Settings").getText());
	log.info(new PushButton("Validate Settings").isEnabled());
	//Bot.get().sleep(DELAY);
	PushButton validate = new PushButton("Validate Settings");
	validate.click();

	while (!validate.isEnabled()) {
		Bot.get().sleep(DELAY);
	}				
	
	log.info("[" + new LabeledText("Bugzilla Repository Settings").getText()+"]");
	assertTrue ("Repo Connection Properties Invalid", new LabeledText("Bugzilla Repository Settings").getText().contains("Repository is valid"));
	new PushButton("Cancel").click();
	
	// JBDS50_XXXX Local tasks can be created, viewed and deleted 
	// JBDS50_XXXX Task-Focused UI works 
	log.info ("Step 5 - Create a new local task");	
	//Bot.get().sleep(DELAY);
	
	elementIndex = repoList.indexOf("Local");
	repoItems.get(elementIndex).select();

	new ShellMenu("File", "New" , "Other...").select();
	new DefaultShell("New");
	DefaultTree newElementTree = new DefaultTree();
	List <TreeItem> newItems = newElementTree.getAllItems();
	
	//Bot.get().sleep(DELAY);
	selectTreeItem (newItems, "Task");
	//Bot.get().sleep(DELAY);
	new PushButton("Next >").click();
	new PushButton("Finish").click();
	//Bot.get().sleep(DELAY);
	
	new DefaultShell("JBoss - New Task - JBoss Developer Studio");
	//Bot.get().sleep(DELAY);
	
	Bot.get().styledText("New Task").setText(TASKNAME);
	Bot.get().styledText("Notes").setText(TASKNOTE);
	
	//Bot.get().sleep(DELAY);

	log.info("Saving the task");
	new ShellMenu("File", "Save").select();
	
//	Bot.get().sleep(DELAY);
	
	log.info ("Step 6 - Verify the new local task");		
	new ShellMenu("Navigate", "Open Task...").select();	
	new DefaultShell("Open Task");
	
	DefaultTable TaskTable = new DefaultTable();
	log.info(TaskTable.rowCount());
	
	assertTrue ("Task not found", TaskTable.cell(0, 0).equals(TASKNAME));
	new PushButton("Cancel").click();
	
	} /* method */

} /* class */
