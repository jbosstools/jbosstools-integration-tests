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
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.combo.ComboWithLabel;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
//import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;


public class MylynBugzillaQueryTest {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected final String expectedMylynElements[] = { "Tasks", "Local",
			"Bugs", "Eclipse.org", "Red Hat Bugzilla",
			"Atlassian Integrations Support", "JBoss Community" };
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";

	@Test
	public void TestIt() {

		WorkbenchShell ws = new WorkbenchShell();
		
		List<TreeItem> repoItems = TestSupport.mylynTestSetup1(log);	
		
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
		// Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		new DefaultShell("Properties for Task Repository");
		log.info(new PushButton("Validate Settings").getText());
		log.info(new PushButton("Validate Settings").isEnabled());
		// Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
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

		log.info ("Step 5 - Create a anonymous bugzilla query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		elementIndex = repoList.indexOf("Red Hat Bugzilla");
		repoItems.get(elementIndex).select();

		Bot.get().sleep(TimePeriod.LONG.getSeconds());

		new ShellMenu("File", "New" , "Other...").select();
		new DefaultShell("New");
		DefaultTree newElementTree = new DefaultTree();
		List <TreeItem> newItems = newElementTree.getAllItems();	
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		TestSupport.selectTreeItem (newItems, "Query", log);
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();
		
		new DefaultShell ("New Query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		
		new DefaultShell ("Edit Query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new RadioButton ("Create query using form").click();
		new PushButton("Next >").click();
		new WaitUntil (new ButtonWithTextIsActive ("Cancel"), TimePeriod.VERY_LONG);
		
		while (!new PushButton("Cancel").isEnabled()) {
			log.info("I am not ready" + new PushButton("Cancel").isEnabled());
			Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		}		
		
		new DefaultShell ("Edit Query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new LabeledText ("Title:").setText("MylynBugzillaQueryTest - test query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		// Look for bugzilla #826087
		new ComboWithLabel ("Summary:").setText("Bugzilla mail layout change for 'new bugs' is a usability regression (this isn't about HTML)");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Finish").click();		
		Bot.get().sleep(TimePeriod.LONG.getSeconds());
		
		new ShellMenu("Window", "Show View", "Other...").select();
		new DefaultShell("Show View");

		/* Verify that the expected repos are defined */
		log.info("***Step 3 - Verify that the Mylyn Features are Present");
		DefaultTree FeatureTree = new DefaultTree();
		List<TreeItem> featureItems = FeatureTree.getAllItems();
		TestSupport.selectTreeItem(featureItems, "Task List", log);
		new PushButton("OK").click();
		Bot.get().sleep(TimePeriod.LONG.getSeconds());
	

	} /* method */

} /* class */
