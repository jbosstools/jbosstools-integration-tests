package org.jboss.tools.arquillian.ui.bot.test.cruiserView;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.TreeContainsItem;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.arquillian.ui.bot.reddeer.view.ArquilliaCruiserView;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wait.CentralIsLoaded;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.junit.Before;
import org.junit.Test;

/**
 * Arquillia Cruiser View tests 
 * 
 * @author Len DiMaggio
 *
 */

public class BasicArquilliaCruiserTest {

	private static Logger log = new Logger(BasicArquilliaCruiserTest.class);
	private static InternalBrowser centralBrowser;
	private static JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();
	
	private static final String STRING_1 = ".addAsResource(\"META-INF/test-persistence.xml\", \"META-INF/persistence.xml\")" ;
	private static final String STRING_2 = "// Kilroy was here";
	private static final String QUICKSTART_SEARCH_STRING = "the `kitchensink` quickstart";
	private static final String PROJECT_NAME = "jboss-kitchensink";
	private static final String CENTRAL_STRING = "Red Hat Central";
	
	/* Copied from: org.jboss.tools.central.test.ui.reddeer.HTML5Test */
	@Before
	public void setup() {
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_STRING).click();
		// activate central editor
		new DefaultEditor(CENTRAL_STRING);
		new WaitUntil(new CentralIsLoaded());
		centralBrowser = new InternalBrowser();
		jsHelper.setBrowser(centralBrowser);
	}

	/**
	 * Test to verify dynamic updating of asset tree in Arquillia View
	 *  
	 * Test steps:
	 * Import the quickstart
	 * Select the project in the project explorer view
	 * Open the Arquillia Cruiser view
	 * Select the project in the Arquillia Cruiser view
	 * Find persistence.xml in the Arquillia Cruiser view
	 * Open MemberRegistration.java
	 * Delete this line: .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
	 * Save file, verify persistence.xml is removed from Arquillia Cruiser view
	 * Restore edit, verify persistence.xml is back in Arquillia Cruiser view
	 *  
	 * @throws InterruptedException
	 */
	
	@Test
	public void dynamicUpdateTest () {

		/* Import the quickstart */
		jsHelper.searchFor(QUICKSTART_SEARCH_STRING);
		String[] examples = jsHelper.getExamples();
		assertTrue("One example should be found", examples.length == 1);
		importExample(examples[0]);
		jsHelper.clearSearch();
		
		/* Select the project in the project explorer */
		log.step("Select the project in the project explorer");
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(PROJECT_NAME).select();

		/* Locate the project in the Arquillia Cruiser View */
		log.step("Locate the project in the Arquillia Cruiser View");
		ArquilliaCruiserView arquilliaCruiserView = new ArquilliaCruiserView();   
		arquilliaCruiserView.open();
				
		/* Verify that the persistence.xml file is present */
		log.step("Verify that the persistence.xml file is present");
		new WaitUntil((new TreeContainsItem(arquilliaCruiserView.getTree(), 
				"jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java", "createTestArchive(test.war)", 
				"WEB-INF", "classes", "META-INF", "persistence.xml")), TimePeriod.LONG, true);
		assertTrue ("presistence.xml file should be located in Arquillia View", new TreeContainsItem(arquilliaCruiserView.getTree(), 
				"jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java", "createTestArchive(test.war)", 
				"WEB-INF", "classes", "META-INF", "persistence.xml").test());

		/* Remove the reference to persistence.xml from the MemberRegistrationTest.java file */
		log.step("Remove the reference to persistence.xml from the MemberRegistrationTest.java file");
		arquilliaCruiserView.getTreeItem("jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java").doubleClick();
		editFile (STRING_1, STRING_2);
		
		System.out.println("Expected size = " + arquilliaCruiserView.getTree().getAllItems().size());
				
		/* And confirm that persistance.xml file is dynamically removed from the Arquillia Cruiser View */
		log.step("Confirm that persistance.xml file is dynamically removed from the Arquillia Cruiser View");
		
		arquilliaCruiserView.close();
		arquilliaCruiserView.open();
		projectExplorer.getProject("jboss-kitchensink").select();
		
		assertFalse ("presistence.xml file should be located in Arquillia View", new TreeContainsItem(arquilliaCruiserView.getTree(), 
				"jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java", "createTestArchive(test.war)", 
				"WEB-INF", "classes", "META-INF", "persistence.xml").test());

		/* Restore the reference to persistence.xml from the MemberRegistrationTest.java file */
		log.step("Restore the reference to persistence.xml from the MemberRegistrationTest.java file");
		arquilliaCruiserView.getTreeItem("jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java").doubleClick();
		editFile (STRING_2, STRING_1);
		
		/* And confirm that persistance.xml file is dynamically restored to the Arquillia Cruiser View */
		log.step("Confirm that persistance.xml file is dynamically restored to the Arquillia Cruiser View");
		
		new WaitUntil((new TreeContainsItem(arquilliaCruiserView.getTree(), 
				"jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java", "createTestArchive(test.war)", 
				"WEB-INF", "classes", "META-INF", "persistence.xml")), TimePeriod.LONG, true);
		assertTrue ("presistence.xml file should be located in Arquillia View", new TreeContainsItem(arquilliaCruiserView.getTree(), 
				"jboss-kitchensink", "src/test/java", "org.jboss.as.quickstarts.kitchensink.test", "MemberRegistrationTest.java", "createTestArchive(test.war)", 
				"WEB-INF", "classes", "META-INF", "persistence.xml").test());
	}
	
	/**
	 * Replace a string in a TextEditor
	 * 
	 * @param originalText
	 * @param editedText
	 */
	private void editFile (String originalText, String editedText) {
		TextEditor theEditor = new TextEditor();
		theEditor.setText(theEditor.getText().replace(originalText, editedText));
		theEditor.save();
	}

	/**
	 * Import an example in JBoss Central 
	 * Copied from: org.jboss.tools.central.test.ui.reddeer.HTML5Test
	 * 
	 * @param exampleName
	 */	
	private void importExample(String exampleName) {
		log.step("Importing example: " + exampleName);
		jsHelper.clickExample(exampleName);
		NewProjectExamplesWizardDialogCentral wizardDialog = new NewProjectExamplesWizardDialogCentral();
		wizardDialog.finish(exampleName);
	}
	
}
