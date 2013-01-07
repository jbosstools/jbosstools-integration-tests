package org.jboss.tools.mylyn.ui.bot.test;

/* Support routines for Mylyn tests */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.ViewTree;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.apache.log4j.Logger;

public class TestSupport {
	
	protected final static long DELAY = 3000l;

	/* To locate one item by name in a list */
	public static void selectTreeItem(List<TreeItem> theTreeItems, String matchingName, Logger log) {
		for (TreeItem item : theTreeItems) {
			log.info(item.getText());
			if (item.getText().equals(matchingName)) {
				item.select();
				break;
			}
		}
		
	} /* method */	public static TreeItem returnTreeItem(List<TreeItem> theTreeItems, String matchingName, Logger log) {
		TreeItem retItem = null;
		for (TreeItem item : theTreeItems) {
			log.info(item.getText());
			if (item.getText().equals(matchingName)) {
				item.select();
				retItem = item;
				break;
			}
		}
		return retItem;
	} /* method */

	/* Test Setup part 1 */
	public static List<TreeItem> mylynTestSetup1 (Logger log, boolean checkForUsage) {		
		/* Close the initial "Usage" Dialog */
		
		if (checkForUsage) {
		
			log.info("*** Step 1 - Close the Usage Shell");
		
			/* Catch and ignore the exception - needed after the first test in the suite runs */
			try {
				new DefaultShell("JBoss Developer Studio Usage");
				Bot.get().sleep(DELAY);
				new PushButton("Yes").click();
			}
			catch (Exception E) {
				E.printStackTrace();
			}
		}		
		
		log.info("*** Step 2 - Open the Mylyn View");
		new ShellMenu("Window", "Show View", "Other...").select();
		new DefaultShell("Show View");

		/* Verify that the expected repos are defined */
		log.info("***Step 3 - Verify that the Mylyn Features are Present");
		ViewTree FeatureTree = new ViewTree();
		List<TreeItem> featureItems = FeatureTree.getAllItems();
		selectTreeItem(featureItems, "Task Repositories", log);
		
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
				
		Bot.get().sleep(30000l);
		
		ViewTree RepoTree = new ViewTree();
		List<TreeItem> repoItems = RepoTree.getAllItems();
		
		return repoItems;
		
	} /* method */
	
	/* Test Setup part 2 */
	public static ArrayList<String> mylynTestSetup2 (List<TreeItem> repoItems, Logger log) {
		ArrayList<String> repoList = new ArrayList<String>();
		int i = 0;
		for (TreeItem item : repoItems) {
			log.info(item.getText());
			repoList.add(i++, item.getText());
		}
		return repoList;
		
	} /* method */

} /* class */
