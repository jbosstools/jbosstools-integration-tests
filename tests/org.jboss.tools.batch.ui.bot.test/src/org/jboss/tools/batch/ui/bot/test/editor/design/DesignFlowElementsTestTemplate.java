package org.jboss.tools.batch.ui.bot.test.editor.design;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditor;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorDesignPage;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public abstract class DesignFlowElementsTestTemplate extends AbstractBatchTest {

	protected static final String PACKAGE = "batch.test.editor.design";
	
	protected JobXMLEditor editor;
	
	@Before
	public void setupEditor(){
		getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).open();
		editor = new JobXMLEditor(JOB_XML_FILE);
		editor.activate();
	}
	
	protected static String getBatchArtifactID(String className){
		char c[] = className.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	} 
	
	protected JobXMLEditorDesignPage getDesignPage(){
		editor.activate();
		return editor.getDesignPage();
	}
	
	protected JobXMLEditorSourcePage getSourcePage(){
		editor.activate();
		return editor.getSourcePage();
	}
	
	protected void createBatchArtifact(BatchArtifacts artifact, String name) {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();

		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage();
		page.setSourceFolder(PROJECT_NAME + "/" + JAVA_FOLDER);
		page.setPackage(PACKAGE);
		page.setName(name);
		page.setArtifact(artifact);
		dialog.finish();
	}
	
	protected String appendIDSelector(String xPathElement, String id){
		return xPathElement + "[" + JobXMLEditorSourcePage.ID + "='" + id + "']";
	}
}
