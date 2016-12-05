package org.jboss.tools.mylyn.reddeer;

/* Support routines for Mylyn tests */
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.equinox.security.ui.StoragePreferencePage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

public class TestSupport {
	
	private static final Logger log = Logger.getLogger(TestSupport.class);
	
	/* Test Setup part 1 */
	public static List<TreeItem> mylynTestSetup1 () {		
		
		log.debug("Open the Mylyn View");
		new ShellMenu("Window", "Show View", "Other...").select();

		/* Verify that the expected repos are defined */
		log.debug("Verify that the Mylyn Features are Present");
		DefaultTreeItem taskRepositories = new DefaultTreeItem ("Mylyn", "Task Repositories");
		taskRepositories.select();		
		
		/* Slightly different text after update for 
		 * http://wiki.eclipse.org/Platform_UI/Juno_Performance_Investigation
		 * installed - see:
		 * https://issues.jboss.org/browse/JBDS-2441
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=385272
		 */
		new PushButton(new WithTextMatcher(new RegexMatcher("OK|ok"))).click();
		
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
	public static ArrayList<String> mylynTestSetup2 (List<TreeItem> repoItems) {
		ArrayList<String> repoList = new ArrayList<String>();
		int i = 0;
		for (TreeItem item : repoItems) {
			log.debug("repo found: " + item.getText());
			repoList.add(i++, item.getText());
		}
		return repoList;
		
	} /* method */
	
	public static void closeSecureStorageIfOpened () {
		
		log.debug("Attempting to close the Secure Storage Dialog");
		
		try {
			new DefaultShell(new WithTextMatcher(new RegexMatcher("Secure Storage.*"))).close();
			log.debug("Closed the Secure Storage Dialog");
		} 
		catch (SWTLayerException swtle){
			log.error("Unable to close the Secure Storage Dialog - " + swtle.getMessage());
			swtle.printStackTrace();
		}	
	}
	

	public static void disableSecureStorage () {

		log.debug("Disabling the Secure Storage Dialog");

		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		StoragePreferencePage storagePage = new StoragePreferencePage();
		preferenceDialog.open();

		preferenceDialog.select(storagePage);
		for (TableItem item : storagePage.getMasterPasswordProviders()) {
			item.setChecked(false);
		}

		storagePage.apply();
		preferenceDialog.ok();
		log.debug("Disabled the Secure Storage Dialog");
		
	}
	
	
} /* class */
