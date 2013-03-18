package org.jboss.tools.mylyn.ui.bot.test;

/* Support routines for Mylyn tests */

import java.util.ArrayList;
import java.util.List;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.ViewTree;
import org.jboss.reddeer.swt.util.Bot;
import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;

import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class TestSupport {
	
	protected final static long DELAY = 3000l;


	
	/* Test Setup part 1 */
	public static List<TreeItem> mylynTestSetup1 (Logger log) {		
		
		/* Added to enable the tests to run from trunk with Kepler - Feb 2013 */
		Bot.get().sleep(30000l);
		log.info("Look for and close the welcome tab");	
		List<SWTBotView> theViews = Bot.get().views();
		for (SWTBotView theView : theViews) {
			log.info ("Looking for Welcome - Found view: " + theView.getTitle());
			if (theView.getTitle().equals("Welcome")) {
				log.info("Found Welcome view - closing");
				Bot.get().viewByTitle("Welcome").close();
			}			
		}		
				
		log.info("*** Step - Open the Mylyn View");
		new ShellMenu("Window", "Show View", "Other...").select();
		new DefaultShell("Show View");

		/* Verify that the expected repos are defined */
		log.info("***Step - Verify that the Mylyn Features are Present");
		DefaultTree FeatureTree = new DefaultTree();
		DefaultTreeItem taskRepositories = new DefaultTreeItem ("Mylyn", "Task Repositories");
		taskRepositories.select();		
//		List<TreeItem> featureItems = FeatureTree.getAllItems();
//		selectTreeItem(featureItems, "Task Repositories", log);
		
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
		
		DefaultTree RepoTree = new DefaultTree();
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
