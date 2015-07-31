package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.batch.reddeer.editor.JobXMLEditor;
import org.jboss.tools.batch.reddeer.editor.JobXMLEditorDesignTab;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardPage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

@OpenPerspective(JavaPerspective.class)
public class CreateJobXMLFileTest extends AbstractBatchTest {

	protected static final String JOB_ID = "batch-test";
	
	private static final Logger log = Logger.getLogger(CreateJobXMLFileTest.class);
	
	@Before
	public void importProject(){
		log.step("Import " + PROJECT_NAME);
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		
		WizardProjectsImportPage page = new WizardProjectsImportPage();
		page.setArchiveFile(Activator.getPathToFileWithinPlugin("projects/batch-test-project.zip"));
		page.selectProjects(PROJECT_NAME);
		
		dialog.finish();
		getProject().select();
	}
	
	@Test
	public void testCreateJobXMLFile(){
		createFile();
		
		assertFileExists();
		assertJobID();
		assertNoProblems();
	}

	private void createFile() {
		NewJobXMLFileWizardDialog dialog = new NewJobXMLFileWizardDialog();
		dialog.open();
		
		NewJobXMLFileWizardPage page = new NewJobXMLFileWizardPage();
		page.setFileName(BATCH_XML_FILE);
		page.setJobID(JOB_ID);
		
		dialog.finish();
	}
	
	private void assertFileExists() {
		assertTrue(getProject().containsItem(BATCH_XML_FILE_FULL_PATH));
	}

	private void assertJobID() {
		getProject().getProjectItem(BATCH_XML_FILE_FULL_PATH).open();
		JobXMLEditor editor = new JobXMLEditor(BATCH_XML_FILE);
		editor.activate();
		
		JobXMLEditorDesignTab tab = editor.openDesignTab();
		tab.selectJob();
		
		assertThat(tab.getJobID(), is(JOB_ID));
	}
}
