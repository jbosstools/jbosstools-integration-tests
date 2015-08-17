package org.jboss.tools.batch.ui.bot.test.wizard;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.batch.reddeer.editor.JavaClassEditor;
import org.jboss.tools.batch.reddeer.editor.MultiPageEditor;
import org.jboss.tools.batch.reddeer.editor.XMLEditor;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.After;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

public abstract class AbstractCreateArtifactTest extends AbstractBatchTest {

	protected static final String NAMED_ANNOTATION = "Named";
	
	protected abstract String getClassName();

	protected abstract BatchArtifacts getArtifact();

	protected abstract void createArtifactHook(NewBatchArtifactWizardPage page);

	protected String getPackage() {
		return "batch.test.wizard.artifact";
	}
	
	@After
	public void tearDown(){
		try {
			new DefaultEditor(getClassName() + ".java").close();
		} catch (CoreLayerException e) {
			// nothing to do
		}
	}

	protected void createArtifact() {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();

		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage();
		page.setPackage(getPackage());
		page.setName(getClassName());
		page.setArtifact(getArtifact());
		createArtifactHook(page);
		dialog.finish();
	}
	
	protected void assertEditorIsActivated() {
		assertTrue(getEditor().isActive());
	}

	protected void assertClassName() {
		assertThat(getEditor().getClassName(), is(getClassName()));
	}

	protected JavaClassEditor getEditor(){
		return new JavaClassEditor(getClassName() + ".java");
	}
	
	protected String getClassByID(String id) {
		if (!getProject().containsItem(BATCH_XML_FILE_FULL_PATH)){
			// the batch.xml file has not been created yet and the <ref value has not been inserted -> this is OK
			return "";
		}
		getProject().getProjectItem(BATCH_XML_FILE_FULL_PATH).open();
		
		MultiPageEditor editor = new MultiPageEditor(BATCH_XML_FILE);
		editor.activate();
		editor.selectPage("Source");

		String xPath = "/:batch-artifacts/:ref[@id=\"" + id + "\"]/@class";
		return new XMLEditor().evaluateXPath(xPath);
	}
	
	protected String getClass(String clazz) {
		if (!getProject().containsItem(BATCH_XML_FILE_FULL_PATH)){
			// the batch.xml file has not been created yet and the <ref value has not been inserted -> this is OK
			return "";
		}
		getProject().getProjectItem(BATCH_XML_FILE_FULL_PATH).open();
		
		MultiPageEditor editor = new MultiPageEditor(BATCH_XML_FILE);
		editor.activate();
		editor.selectPage("Source");

		String xPath = "/:batch-artifacts/:ref[@class=\"" + clazz + "\"]";
		return new XMLEditor().evaluateXPath(xPath);
	}
}
