package org.jboss.tools.mylyn.reddeer;

/* Support routines for Mylyn tests */
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
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
		
		/* For JBoss Tools */
		String uiString = "Secure Storage"; 

		/* Determine if JBDS is running */
		IProduct prod = Platform.getProduct();
		if ((prod != null) && (prod.getId().startsWith("com.jboss.devstudio."))) {
			uiString = "Secure Storage Password";
		}	
		
		try {
			new DefaultShell(uiString).close();
			log.debug("Closed the Secure Storage Dialog");
		} 
		catch (SWTLayerException swtle){
			log.error("Unable to close the Secure Storage Dialog - " + swtle.getMessage());
			log.error (swtle);				
		}	
	}
	

	public static void disableSecureStorage () {

		log.debug("Disabling the Secure Storage Dialog");

		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		StoragePreferencePage storagePage = new StoragePreferencePage();
		preferenceDialog.open();

		preferenceDialog.select(storagePage);
		for (TableItem item : storagePage.getMasterPasswordProviders()) {
			item.setChecked(true);
		}

		storagePage.apply();
		preferenceDialog.ok();
		log.debug("Disabled the Secure Storage Dialog");
		
	}
	
	
} /* class */
