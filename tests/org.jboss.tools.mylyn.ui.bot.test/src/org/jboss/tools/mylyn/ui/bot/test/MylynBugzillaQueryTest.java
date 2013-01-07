package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn
 * Test to verify that a Bugzilla simple query can be made as an anonymous user.
 * The query should return only (1) bugzilla: 
 * 		826087: Bugzilla mail layout change for 'new bugs' is a usability regression (this isn't about HTML)		
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
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.tree.ViewTree;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
//import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class MylynBugzillaQueryTest {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected final String expectedMylynElements[] = { "Tasks", "Local",
			"Bugs", "Eclipse.org", "Red Hat Bugzilla",
			"Atlassian Integrations Support", "JBoss Community" };

	/* Simple test to verify the operation of anonymous queries of the RH and eclipse bugzilla
	 * repos through Mylyn. 
	 * 
	 * Workarounds to deal with SWTBot/Mylyn issues:
	 * 
	 * 1) The query name as displayed by Mylyn can take either form:
	 *     MylynBugzillaQueryTest - test query   [Red Hat Bugzilla]
	 *     MylynBugzillaQueryTest - test query
	 * 
	 * This test checks for the shorter form as it is a subset of the longer form. 
	 * 
	 * 2) SWTBot has problems with the tree of queries displayed by Mylyn. The query names are
	 * generally displayed, but the resulting set of bugzillas (in this case of this test, these
	 * sets consist of only one bugzilla) are not. It sometimes helps to expand/double-click 
	 * the query - but a better workaround is to enter the bugzilla number in the display filter
	 * text box. 
	 * 
	 * Note that these problems are specific to SWTBot - they are not seen in manual use. 
	 * 
	 */
	
	/* Test for RH bugzilla */
	@Test
	public void TestRHBugzilla() {
		TestBugzillaQuery("Red Hat Bugzilla",
				"MylynRHBugzillaQueryTest - test query",
				"Bugzilla mail layout change for 'new bugs' is a usability regression (this isn't about HTML)",
				"826087");
	} /* test */

	/* Test for eclipse bugzilla */
	@Test
	public void TestEclipseBugzilla() {
		TestBugzillaQuery("Eclipse.org",
				"MylynEclipseBugzillaQueryTest - test query",
				"JBoss agent doesn't work remotely",
				"188417");
	} /* test */
	
	/* 
	 * Generalized test method
	 * Performs anonymous bugzilla query 
	 */
	public void TestBugzillaQuery(String targetRepo, String queryName, String bugzillaSummary, String bugzilla) {

		String fullBugzillaString = bugzilla + ": " + bugzillaSummary;
		
		WorkbenchShell ws = new WorkbenchShell();

		List<TreeItem> repoItems = TestSupport.mylynTestSetup1(log, true);
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems, log);

		assertEquals("Expecting 7 MyLyn items", repoItems.size(), 7);
		for (String elementName : expectedMylynElements) {
			assertTrue("Mylyn element list incorrect",
					repoList.contains(elementName));
		}

		// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin
		// JBDS50_0140 Red Hat Bugzilla task repository is available and can be
		// connected
		log.info("Step 4 - Validate connection to the Red Hat Bugzilla repo");
		int elementIndex = repoList.indexOf(targetRepo);
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
		assertTrue("Repo Connection Properties Invalid", new LabeledText("Bugzilla Repository Settings").getText().contains("Repository is valid"));
		new PushButton("Cancel").click();

		log.info("Step 5 - Create a anonymous bugzilla query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		elementIndex = repoList.indexOf(targetRepo);
		repoItems.get(elementIndex).select();

		Bot.get().sleep(TimePeriod.LONG.getSeconds());

		new ShellMenu("File", "New", "Other...").select();
		new DefaultShell("New");
		ViewTree newElementTree = new ViewTree();
		List<TreeItem> newItems = newElementTree.getAllItems();
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		TestSupport.selectTreeItem(newItems, "Query", log);
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();

		new DefaultShell("New Query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());

		new DefaultShell("Edit Query");
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new RadioButton("Create query using form").click();
		new PushButton("Next >").click();
		new WaitUntil(new ButtonWithTextIsActive("Cancel"),
				TimePeriod.VERY_LONG);

		while (!new PushButton("Cancel").isEnabled()) {
			log.info("I am not ready" + new PushButton("Cancel").isEnabled());
			Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		}

		new DefaultShell("Edit Query");
			
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		
		/* Slightly different text on JBDS5/6 - assume that 5 is running, trap
		 * an exception use the catch block if it's JBDS6 (same for JBT3/4)
		 */
		try {
			new LabeledText("Query Title:").setText(queryName);
		}
		catch (org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException E) {
			new LabeledText("Title:").setText(queryName);
		}
		
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new DefaultCombo("Summary:").setText(bugzillaSummary);
		Bot.get().sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Finish").click();
		Bot.get().sleep(TimePeriod.LONG.getSeconds());
		
		new ShellMenu("Window", "Show View", "Other...").select();
		new DefaultShell("Show View");

		/* Verify that the expected repos are defined */
		log.info("***Step 3 - Verify that the Mylyn Features are Present");
		ViewTree FeatureTree = new ViewTree();
		List<TreeItem> featureItems = FeatureTree.getAllItems();
		TestSupport.selectTreeItem(featureItems, "Task List", log);
		Bot.get().sleep(TimePeriod.LONG.getSeconds());
		
		/* Slightly different text after update for 
		 * http://wiki.eclipse.org/Platform_UI/Juno_Performance_Investigation
		 * installed - see: 
		 * https://issues.jboss.org/browse/JBDS-2441
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=385272
		 */
		try {
			new PushButton("OK").click();
		}
		catch (org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException E) {
			new PushButton("ok").click();
		}		
		
		Bot.get().sleep(TimePeriod.LONG.getSeconds());

		/*
		 * A full 30 second delay is needed here - or else the test fails to
		 * locate the widget.
		 */
		Bot.get().sleep(30000l);
			
		/* Slightly different text on JBDS5/6 - assume that 5 is running, trap
		 * an exception use the catch block if it's JBDS6 (same for JBT3/4)
		 */
		try {
			new DefaultShell("JBoss - JBoss Central - JBoss Developer Studio");
		}
		catch (org.jboss.reddeer.swt.exception.SWTLayerException E) {
			new DefaultShell("JBoss - JBoss Developer Studio");
		}		
		
		Bot.get().sleep(30000l);
		ViewTree bugzillaTree = new ViewTree();
		List<TreeItem> bugzillaQueryItems = bugzillaTree.getAllItems();
		
		/*
		 * There seems to be an SWTBot problem here - the full query tree is not
		 * visible unless the first element is expanded - the workaround is to
		 * expand it.
		 */
		Bot.get().text(0).setText("");
		Bot.get().text(0).setText(bugzilla);	
		
		int TreeItemCounter = 0;
		for (TreeItem i : bugzillaQueryItems) {
			log.info("Found queryItem: " + TreeItemCounter + " " + i.getText());
			if (i.getText().contains(queryName)) {
				break;
			}
			else {
				TreeItemCounter++;
			}
		}
		
		log.info ("The counter is: " + TreeItemCounter);
		
		for (TreeItem i : bugzillaQueryItems) {
			log.warn("Mylyn queries before workaround: [" + i.getText() + "]");
		}
		
		TreeItem bugzillaQueryItem = bugzillaQueryItems.get(TreeItemCounter);
		TreeItem bugzillaItem = bugzillaQueryItems.get(TreeItemCounter + 1);
		
		if (!bugzillaItem.getText().equals(fullBugzillaString)) {

			while (!bugzillaItem.getText().equals(fullBugzillaString)) {
				log.warn("Query tree not full populated - SWTBot issue - will retry in 30 sec...");
				bugzillaQueryItem.select();
				bugzillaQueryItem.doubleClick();
				bugzillaQueryItem.expand();
				Bot.get().sleep(30000l);
				bugzillaQueryItems = bugzillaTree.getAllItems();
				bugzillaItem = bugzillaQueryItems.get(TreeItemCounter + 1);
			}

			bugzillaQueryItems = bugzillaTree.getAllItems();
			for (TreeItem i : bugzillaQueryItems) {
				log.info("Mylyn queries after workaround: [" + i.getText() + "]");
			}
		}

		assertTrue("Query name mismatch - expected: " + queryName + " got "
				+ bugzillaQueryItem.getText(), bugzillaQueryItem.getText().contains(queryName));
		assertTrue("Bugzilla summary mismatch - expected: " + fullBugzillaString
				+ " got " + bugzillaItem.getText(), bugzillaItem.getText().contains(fullBugzillaString));

		Bot.get().sleep(30000l);
		
	} /* method */

} /* class */
