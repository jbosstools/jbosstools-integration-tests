package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn
 * 
 */

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
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;

public class MylynReqTest {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected final String expectedMylynElements[] = { "Tasks", "Local",
			"Bugs", "Eclipse.org", "Red Hat Bugzilla",
			"Atlassian Integrations Support", "JBoss Community" };
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";

	@Test
	public void TestIt() {

		WorkbenchShell ws = new WorkbenchShell();
		
		List<TreeItem> repoItems = TestSupport.mylynTestSetup1(log, true);	
		
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems, log);
				
		assertEquals ("Expecting 7 MyLyn items", repoItems.size(), 7);
		for (String elementName : expectedMylynElements) {
			assertTrue ("Mylyn element list incorrect", repoList.contains(elementName));
		}

		// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin
		// JBDS50_0140 Red Hat Bugzilla task repository is available and can be
		// connected
		log.info("Step 4 - Validate connection to the Red Hat Bugzilla repo");
		int elementIndex = repoList.indexOf("Red Hat Bugzilla");
		repoItems.get(elementIndex).doubleClick();
		// Bot.get().sleep(DELAY);

		new DefaultShell("Properties for Task Repository");
		log.info(new PushButton("Validate Settings").getText());
		log.info(new PushButton("Validate Settings").isEnabled());
		// Bot.get().sleep(DELAY);
		PushButton validate = new PushButton("Validate Settings");
		validate.click();

		while (!validate.isEnabled()) {
			Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		}

		log.info("["
				+ new LabeledText("Bugzilla Repository Settings").getText()
				+ "]");
		assertTrue("Repo Connection Properties Invalid",
				new LabeledText("Bugzilla Repository Settings").getText()
						.contains("Repository is valid"));
		new PushButton("Cancel").click();

		// JBDS50_XXXX Local tasks can be created, viewed and deleted
		// JBDS50_XXXX Task-Focused UI works
		log.info ("Step 5 - Create a new local task");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		elementIndex = repoList.indexOf("Local");
		repoItems.get(elementIndex).select();

		Bot.get().sleep(TimePeriod.LONG.getSeconds());

		new ShellMenu("File", "New" , "Other...").select();
		new DefaultShell("New");
		DefaultTree newElementTree = new DefaultTree();
		List <TreeItem> newItems = newElementTree.getAllItems();

		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		TestSupport.selectTreeItem (newItems, "Task", log);
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		new DefaultShell("JBoss - New Task - JBoss Developer Studio");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		Bot.get().styledText("New Task").setText(TASKNAME);
		Bot.get().styledText("Notes").setText(TASKNOTE);

		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		log.info("Saving the task");
		new ShellMenu("File", "Save").select();

		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		log.info ("Step 6 - Verify the new local task");
		new ShellMenu("Navigate", "Open Task...").select();
		new DefaultShell("Open Task");

		DefaultTable TaskTable = new DefaultTable();
		log.info(TaskTable.rowCount());

		assertTrue ("Task not found", TaskTable.cell(0, 0).equals(TASKNAME));
		new PushButton("Cancel").click();

	} /* method */

} /* class */
