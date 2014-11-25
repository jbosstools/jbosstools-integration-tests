package org.jboss.tools.mylyn.reddeer;

/* Support routines for Mylyn tests */
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Display;
import org.apache.log4j.Logger;
import org.eclipse.ui.IViewReference;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.lookup.WorkbenchLookup;

public class TestSupport {
	
	/* Test Setup part 1 */
	public static List<TreeItem> mylynTestSetup1 (Logger log) {		
		
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
	
	public static void closeSecureStorageIfOpened () {
		try{
			new DefaultShell("Secure Storage").close();
		} catch (SWTLayerException swtle){
			// do nothing shell was not opened
		}	
	}
	
} /* class */
