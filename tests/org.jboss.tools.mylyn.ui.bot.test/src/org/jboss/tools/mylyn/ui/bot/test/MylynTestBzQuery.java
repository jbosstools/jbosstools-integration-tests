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
import org.jboss.reddeer.eclipse.ui.ide.RepoConnectionDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class MylynTestBzQuery {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected final String expectedMylynElements[] = { "Tasks", "Local",
			"Bugs", "Eclipse.org", "Red Hat Bugzilla",
			"Atlassian Integrations Support", "JBoss Community" };

	/* Simple test to verify the operation of anonymous queries of the RH and eclipse bugzilla
	 * repos through Mylyn. 
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

		TestSupport.closeWelcome();
		
		String fullBugzillaString = bugzilla + ": " + bugzillaSummary;
		
		List<TreeItem> repoItems = TestSupport.mylynTestSetup1(log);
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems, log);

		// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin
		// JBDS50_0140 Red Hat Bugzilla task repository is available and can be
		// connected
		log.info("Step 4 - Validate connection to the Red Hat Bugzilla repo");

		int elementIndex = repoList.indexOf(targetRepo);
		log.info("Found: " + repoItems.get(elementIndex).getText());
		
		repoItems.get(elementIndex).select();	
		new ShellMenu("File", "Properties").select();      
		
		RepoConnectionDialog theRepoDialog = new RepoConnectionDialog();
		log.info(theRepoDialog.getText());

		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		theRepoDialog.validateSettings();

		log.info("["
				+ new LabeledText("Bugzilla Repository Settings").getText()
				+ "]");
		assertTrue("Repo Connection Properties Invalid",
				new LabeledText("Bugzilla Repository Settings").getText()
						.contains("Repository is valid"));
			
		theRepoDialog.finish();
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		
		log.info("Step - Create a anonymous bugzilla query");
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		elementIndex = repoList.indexOf(targetRepo);
		repoItems.get(elementIndex).select();

		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		new ShellMenu("File", "New", "Other...").select();
		new DefaultShell("New");
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		
		new DefaultTree();
		DefaultTreeItem theTask = new DefaultTreeItem ("Tasks", "Query");
		theTask.select();		
		
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();

		new DefaultShell("New Query");
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Next >").click();
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		new DefaultShell("Edit Query");
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		new RadioButton("Create query using form").click();
		new PushButton("Next >").click();
		new WaitUntil(new ButtonWithTextIsActive("Cancel"),
				TimePeriod.VERY_LONG);

		while (!new PushButton("Cancel").isEnabled()) {
			log.info("I am not ready" + new PushButton("Cancel").isEnabled());
			AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		}

		new DefaultShell("Edit Query");
			
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		
		/* Slightly different text on JBT3/4)  */ 	
		log.info("GET Bundle");
		log.info("Version = " + org.eclipse.core.runtime.Platform.getProduct().getDefiningBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION));
		log.info("Product = " + org.eclipse.core.runtime.Platform.getProduct().getName());

		// Example version string = 4.3.0.v201302041400
		String extendedVersionString = org.eclipse.core.runtime.Platform.getProduct().getDefiningBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
		String theVersionString = extendedVersionString.substring(0, 1);
		
		if (org.eclipse.core.runtime.Platform.getProduct().getName().equals("JBoss Developer Studio")) {
			if (theVersionString.equals("5")) {
				log.info ("JBDS 5 is running");
				new LabeledText("Query Title:").setText(queryName);
			}
			else if (theVersionString.equals("6")) {
				log.info ("JBDS 6 is running");
				new LabeledText("Title:").setText(queryName);
			}
			else if (theVersionString.equals("7")) {
				log.info ("JBDS 7 is running");
				new LabeledText("Title:").setText(queryName);
			}
		}
		else {
			if (theVersionString.equals("3")) {
				log.info ("JBT 3 is running");
				new LabeledText("Query Title:").setText(queryName);
			}
			else if (theVersionString.equals("4")) {
				log.info ("JBT 4 is running");
				new LabeledText("Title:").setText(queryName);
			}
		}
		
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		new DefaultCombo("Summary:").setText(bugzillaSummary);
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		new PushButton("Finish").click();
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		new ShellMenu("Window", "Show View", "Other...").select();

		/* Verify that the expected repos are defined */
		log.info("Step - Verify that the Mylyn query is Present");

		/* Open the Task List view */
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		new DefaultTree();
		theTask = new DefaultTreeItem ("Mylyn", "Task List");
		theTask.select();	

		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());

		/*
		 * A full 30 second delay is needed here - or else the test fails to
		 * locate the widget.
		 */
		AbstractWait.sleep(30000l);

		/* Dealing with Red Deer incompatibilities on JBT */
		if (org.eclipse.core.runtime.Platform.getProduct().getName().equals("JBoss Developer Studio")) {
	
		/* Locate the list of created queries */
		DefaultTree bugzillaTree = new DefaultTree();
		List<TreeItem> bugzillaQueryItems = bugzillaTree.getAllItems();
		log.info("Total of query items found = " + bugzillaQueryItems.size());
		AbstractWait.sleep(30000l);
		
		/*
		 * There seems to be an eclipse problem here - the full query tree is not
		 * visible unless the first element is expanded - the workaround is to
		 * expand it.
		 */
		DefaultText eclipseText = new DefaultText();
		eclipseText.setText("");
		eclipseText.setText(bugzilla);
		
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
				AbstractWait.sleep(30000l);
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
		}
		
		else if (org.eclipse.core.runtime.Platform.getProduct().getName().equals("Eclipse Platform")) {
			
		/* Seeing different behavior with JBT - need to explicitly get the query list and results */
			DefaultTree theTree = new DefaultTree();
			List <TreeItem> theQueries =  theTree.getAllItems();
		
		for (TreeItem i : theQueries) {
			log.info(i.getText());
		}
		
		boolean foundQuery = false;
		boolean foundQueryResults = false;
		
		for (TreeItem i : theQueries) {				
			log.info("Looking for query: " + queryName + " found: " + i.getText());
			if (i.getText().contains(queryName)) {
				foundQuery = true;
				log.info("Found query: " + queryName);
					
				i.select();
				i.doubleClick();
				AbstractWait.sleep(30000l);
				
				List <TreeItem> theQueryResults = i.getItems();
				for (TreeItem q : theQueryResults) {
					log.info("Looking for query results: " + fullBugzillaString + " found: " + q.getText());
					if (q.getText().contains(fullBugzillaString)) {
						foundQueryResults = true;
						log.info("Found query results: " + fullBugzillaString);
					}
				}				
			}
		}
		assertTrue("Found query: " + queryName, foundQuery);
		assertTrue("Found query results: " + fullBugzillaString, foundQueryResults);

		}
		
	} /* method */

} /* class */
