package org.jboss.tools.mylyn.ui.bot.test;

/* Support routines for Mylyn tests */

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.apache.log4j.Logger;
import org.eclipse.ui.IViewReference;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.lookup.WorkbenchLookup;

public class TestSupport {
	
	protected final static long DELAY = 3000l;

	public static void closeWelcome () {	
		/* close Welcome screen - copied from: class RedDeerTest */
		for (IViewReference viewReference : WorkbenchLookup.findAllViews()) {
			if (viewReference.getPartName().equals("Welcome")) {
				final IViewReference iViewReference = viewReference;
				Display.syncExec(new Runnable() {
					@Override
					public void run() {
						iViewReference.getPage().hideView(iViewReference);
					}
				});
				break;
			}
		}
	}
	
	
	/* Test Setup part 1 */
	public static List<TreeItem> mylynTestSetup1 (Logger log) {		
		
		closeWelcome ();
				
		log.info("*** Step - Open the Mylyn View");
		new ShellMenu("Window", "Show View", "Other...").select();

		/* Verify that the expected repos are defined */
		log.info("***Step - Verify that the Mylyn Features are Present");
		DefaultTreeItem taskRepositories = new DefaultTreeItem ("Mylyn", "Task Repositories");
		taskRepositories.select();		
		
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
				
		AbstractWait.sleep(TimePeriod.NORMAL.getSeconds());
		
		DefaultTree RepoTree = new DefaultTree();
		List<TreeItem> repoItems = RepoTree.getAllItems();
		
		return repoItems;
		
	} /* method */
	
	/* Test Setup part 2 
	 * 
	 * This is divided into 2 parts to enable tests to receive both List of repos, 
	 * and an ArrayList of the same items
	 * 
	 * */
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
