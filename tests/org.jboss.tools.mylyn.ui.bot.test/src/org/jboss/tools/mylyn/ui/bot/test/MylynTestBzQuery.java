package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn
 * Test to verify that a Bugzilla simple query can be made as an anonymous user.
 * The query should return only (1) bugzilla: 
 * 		826087: Bugzilla mail layout change for 'new bugs' is a usability regression (this isn't about HTML)		
 * 
 */
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.views.TaskListView;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.views.TaskRepositoriesView;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.wizards.TaskRepositoryWizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TreeItemHasMinChildren;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.mylyn.reddeer.TestSupport;
import org.junit.Test;


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
	public void testRHBugzilla() {
		testBugzillaQuery("Red Hat Bugzilla",
				"MylynRHBugzillaQueryTest - test query",
				"Bugzilla mail layout change for 'new bugs' is a usability regression (this isn't about HTML)",
				"826087");
	} /* test */

	/* Test for eclipse bugzilla */
	@Test
	public void testEclipseBugzilla() {
		testBugzillaQuery("Eclipse.org",
				"MylynEclipseBugzillaQueryTest - test query",
				"JBoss agent doesn't work remotely",
				"188417");
	} /* test */

	/* 
	 * Generalized test method
	 * Performs anonymous bugzilla query 
	 */
	public void testBugzillaQuery(String targetRepo, String queryName, String bugzillaSummary, String bugzilla) {

		TaskRepositoriesView view = new TaskRepositoriesView();

		view.open();

		String fullBugzillaString = bugzilla + ": " + bugzillaSummary;

		List<TreeItem> repoItems = TestSupport.mylynTestSetup1();
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems);

		// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin
		// JBDS50_0140 Red Hat Bugzilla task repository is available and can be
		// connected
		log.info("Step 4 - Validate connection to the Red Hat Bugzilla repo");

		int elementIndex = repoList.indexOf(targetRepo);
		log.info("Found: " + repoItems.get(elementIndex).getText());

		repoItems.get(elementIndex).select();	
		new ShellMenu("File", "Properties").select();  
		TestSupport.closeSecureStorageIfOpened();

		try {
			new WaitUntil(new ShellIsActive("Refreshing repository configuration"), TimePeriod.LONG); 
		}
		catch (Exception E) {
			log.info ("Problem with 'Refreshing repository configuration' shell not seen");
		}		

		TaskRepositoryWizardDialog theRepoDialog = new TaskRepositoryWizardDialog();
		log.info(theRepoDialog.getTitle());

		theRepoDialog.validateSettings();

		log.info("["
				+ new LabeledText("Bugzilla Repository Settings").getText()
				+ "]");
		assertTrue("Repo Connection Properties Invalid",
				new LabeledText("Bugzilla Repository Settings").getText()
				.contains("Repository is valid"));


		theRepoDialog.cancel();
		log.info("Step - Create a anonymous bugzilla query");

		elementIndex = repoList.indexOf(targetRepo);
		repoItems.get(elementIndex).select();

		new ShellMenu("File", "New", "Other...").select();
		new DefaultShell("New");

		new DefaultTree();
		DefaultTreeItem theTask = new DefaultTreeItem ("Tasks", "Query");
		theTask.select();		

		new PushButton("Next >").click();

		new DefaultShell("New Query");
		new PushButton("Next >").click();

		new DefaultShell("Edit Query");
		new RadioButton("Create query using form").click();
		new PushButton("Next >").click();
		new WaitUntil(new ShellIsActive("Edit Query"), TimePeriod.VERY_LONG); 

		new DefaultShell("Edit Query");
		new LabeledText("Title:").setText(queryName);
		
		new LabeledCombo("Summary:").setText(bugzillaSummary);
		new PushButton("Finish").click();

		/* Verify that the expected repos are defined */
		log.info("Step - Verify that the Mylyn query is Present");

		/* Open the Task List view */
		TaskListView listView = new TaskListView();
		listView.open();

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
				new WaitUntil(new TreeItemHasMinChildren(i, 1), TimePeriod.VERY_LONG); 

				List <TreeItem> theQueryResults = i.getItems();
				for (TreeItem q : theQueryResults) {
					log.info("Looking for query results: " + fullBugzillaString + " found: " + q.getText());
					if (q.getText().contains(fullBugzillaString)) {
						foundQueryResults = true;
						log.info("Found query results: " + fullBugzillaString);
					}
				}
				break;
			}
		}
		assertTrue("Found query: " + queryName, foundQuery);
		assertTrue("Found query results: " + fullBugzillaString, foundQueryResults);
		
	    /* Notification shell, and a shell with a null name, are intermittently left open
	     * when Red Deer attempts to close all shells:
	     * https://github.com/jboss-reddeer/reddeer/blob/master/plugins/org.jboss.reddeer.junit.extension/src/org/jboss/reddeer/junit/extension/after/test/impl/CloseAllShellsExt.java
	     * See Red Deer issue: https://github.com/jboss-reddeer/reddeer/issues/1300
	     * 
	     */
		if (new ShellIsAvailable("Notification").test()) {
            log.info("Closing shell - Notification");
		    new DefaultShell("Notification").close();
		}
        if (new ShellIsAvailable("").test()) {
            log.info("Closing shell - null title");
            new DefaultShell("").close();
        }
		
		view.close();
		listView.close();

	} /* method */

} /* class */
