package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn
 * 
 * 
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.views.TaskListView;
import org.jboss.reddeer.eclipse.mylyn.tasks.ui.views.TaskRepositoriesView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.mylyn.reddeer.TestSupport;
import org.junit.Test;

public class MylynTestLocalRepo {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected ArrayList<String> expectedMylynElements = new ArrayList<String>();
	
	protected final String DEFAULT_TASKNAME = "New Task";	
	protected final String TASKNAME = DEFAULT_TASKNAME + " - a sample task in Mylyn Local Repo";
	protected final String UPDATED_TASKNAME = TASKNAME + " - updated";
	
	@Test
	public void testLocalRepo() {

		/* Verify Local task repository can be found */
		TaskRepositoriesView view = new TaskRepositoriesView();	
		view.open();
			
		List<TreeItem> repoItems = TestSupport.mylynTestSetup1();
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems);
				
		/* Create a new task in the local task repository */	
		log.info("Create a new task in the local task repository");
		view.createLocalTask(repoItems, repoList);
		
		/* Verify that the newly created task is populated with the expected default values */
		assertEquals ("Failed to find default task name", DEFAULT_TASKNAME, new DefaultStyledText(DEFAULT_TASKNAME).getText());
		assertFalse ("Complete button was incorrectly selected", new RadioButton("Complete").isSelected());
		assertTrue ("Incomplete button was incorrectly not selected", new RadioButton("Incomplete").isSelected());
		
		/* Set values in the new task, and verify the values */		
		new DefaultStyledText(DEFAULT_TASKNAME).setText(TASKNAME);	
		assertEquals ("Failed to find task name", TASKNAME, new DefaultStyledText(TASKNAME).getText());
					
		/* Save the newly created task to the local repo */
		log.info("Saving task " + TASKNAME + " to local repo");
		view.saveTask();
		
		/* Activate the newly created task */
		
		/* Find the task in the repo */
		TaskListView listView = new TaskListView();
		listView.open();
		
		/* Workaround for intermittent/inconsistent situation where newly created task is not visible.
		 * Seems to be a timing issue in the UI - dependent on CPU speed? Only seeing this sometimes
		 * on Jenkins - never locally.
		 */
		try {
			listView.getTask("Uncategorized", TASKNAME);
		}
		catch (org.jboss.reddeer.swt.exception.SWTLayerException E) {
			log.error("Newly created task not found - retrying");
			listView.close();
			listView.open();
			listView.getTask("Uncategorized", TASKNAME);
		}
		
		log.info("Activate the " + TASKNAME + " task");
		view.activateTask(TASKNAME);

		/* Edit the new task, save it, verify that it can be found again in the local repo */
		log.info("Editing the task " + TASKNAME);
		view.openTask(TASKNAME);
		
		new DefaultStyledText(TASKNAME).setText(UPDATED_TASKNAME);
		view.saveTask();
		
		/* Verify that the newly created task is populated with the expected default values */
		assertEquals ("Failed to find task name", UPDATED_TASKNAME, new DefaultStyledText(UPDATED_TASKNAME).getText());
		assertFalse ("Complete button was incorrectly selected", new RadioButton("Complete").isSelected());
		assertTrue ("Incomplete button was incorrectly not selected", new RadioButton("Incomplete").isSelected());
		
		listView.close();
		
		/* Mark the task as completed */
	
		/* Find the task in the repo */
		listView.open();
		listView.getTask ("Uncategorized", UPDATED_TASKNAME);
		
		view.openTask(UPDATED_TASKNAME);

		log.info("Mark the task as complete: " + UPDATED_TASKNAME);
		new RadioButton("Complete").click();
		view.saveTask();
		assertTrue ("Complete button was incorrectly not selected", new RadioButton("Complete").isSelected());
		
		listView.close();
		
		/* De-activate and then delete the new task, verify that it can not be found again in the local repo */
		listView.open();
		listView.getTask ("Uncategorized", UPDATED_TASKNAME);
		
		log.info("Deactivate the task: " + UPDATED_TASKNAME);
		view.deactivateTask();
		listView.close();
		
		/* Delete the task */	
		
		/* Find the task in the repo */
		listView.open();
		listView.getTask ("Uncategorized", UPDATED_TASKNAME);
		
		log.info("Delete the task: " + UPDATED_TASKNAME);
		view.deleteTask();
		
		/* Verify that the task has been deleted by confirming that it cannot be found */ 
		new DefaultTree();
		try {
			new DefaultTreeItem ("Uncategorized", UPDATED_TASKNAME);
		}
		catch (WaitTimeoutExpiredException E) {
			log.info ("Expected Exception as deleted task cannot be found" + E.getMessage() + " correctly trapped");
		}	
		
		listView.close();

		view.close();
		
	} /* method */
	
} /* class */
