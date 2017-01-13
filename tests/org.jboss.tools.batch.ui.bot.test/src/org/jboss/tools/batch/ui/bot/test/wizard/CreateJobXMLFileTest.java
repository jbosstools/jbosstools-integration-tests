package org.jboss.tools.batch.ui.bot.test.wizard;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditor;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorDesignPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

@OpenPerspective(JavaPerspective.class)
public class CreateJobXMLFileTest extends AbstractBatchTest {
	
	private static final Logger log = Logger.getLogger(CreateJobXMLFileTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		initTestResources(log, "projects/" + getProjectName() + ".zip");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		removeProject(log);
	}
	
	@Test
	public void testCreateJobXMLFile(){
		assertFileExists();
		assertJobID();
		assertNoProblems();
	}
	
	private void assertFileExists() {
		assertTrue(getProject().containsItem(JOB_XML_FILE_FULL_PATH));
	}

	private void assertJobID() {
		getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).open();
		JobXMLEditor editor = new JobXMLEditor(JOB_XML_FILE);
		editor.activate();
		
		JobXMLEditorDesignPage tab = editor.getDesignPage();
		tab.selectJob();
		
		assertThat(tab.getJobID(), is(JOB_ID));
	}

	@Override
	protected String getPackage() {
		// TODO Auto-generated method stub
		return null;
	}
}
