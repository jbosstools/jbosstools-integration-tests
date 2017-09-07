package org.jboss.tools.mylyn.ui.bot.test;

/*
 * Prototype test for Mylyn
 * 
 * 
 */
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.condition.ActiveShellExists;
import org.eclipse.reddeer.eclipse.mylyn.tasks.ui.views.TaskRepositoriesView;
import org.eclipse.reddeer.eclipse.mylyn.tasks.ui.wizards.TaskRepositoryWizardDialog;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.mylyn.reddeer.TestSupport;
import org.junit.Test;

public class MylynTestValidate {

	protected final Logger log = Logger.getLogger(this.getClass());
	
	protected ArrayList<String> expectedMylynElements = new ArrayList<String>();	
	protected final String TASKNAME = "a sample task in Mylyn";
	protected final String TASKNOTE = "a sample note for a sample task in Mylyn";

	@Test
	public void testValidate() {

		TaskRepositoriesView view = new TaskRepositoriesView();
		
		view.open();
			
		expectedMylynElements.add("Tasks");
		expectedMylynElements.add("Local");
		expectedMylynElements.add("Bugs");
		expectedMylynElements.add("Eclipse.org");
		expectedMylynElements.add("Red Hat Bugzilla");
		expectedMylynElements.add("Atlassian Integrations Support");
		expectedMylynElements.add("JBoss Community");
		
		List<TreeItem> repoItems = TestSupport.mylynTestSetup1();
		ArrayList<String> repoList = TestSupport.mylynTestSetup2(repoItems);
				
		for (String elementName : repoList) {
			assertTrue ("Mylyn element list incorrect - cannot find: " + elementName, expectedMylynElements.contains(elementName));
		}
		
		// JBDS50_0135 User can connect Bugzilla via Mylyn connectors plugin
		// JBDS50_0140 Red Hat Bugzilla task repository is available and can be
		// connected
		log.info("Step 4 - Validate connection to the Red Hat Bugzilla repo");

		int elementIndex = repoList.indexOf("Eclipse.org");
		log.info("Found: " + repoItems.get(elementIndex).getText());
		
		repoItems.get(elementIndex).select();	
		new ShellMenuItem("File", "Properties").select();  
		TestSupport.closeSecureStorageIfOpened();
	
		try {
			new WaitUntil(new ActiveShellExists(), TimePeriod.VERY_LONG); 
		}
		catch (Exception E) {
			log.info ("Problem with 'Refreshing repository configuration' shell not seen");
		}		
			
		TaskRepositoryWizardDialog theRepoDialog = new TaskRepositoryWizardDialog();
		log.info(theRepoDialog.getMessage());

		theRepoDialog.validateSettings();

		log.info("["
				+ new LabeledText("Bugzilla Repository Settings").getText()
				+ "]");
		assertTrue("Repo Connection Properties Invalid",
				new LabeledText("Bugzilla Repository Settings").getText()
						.contains("Repository is valid"));
			
		theRepoDialog.cancel();
		
		view.close();
		
	} /* method */

} /* class */
