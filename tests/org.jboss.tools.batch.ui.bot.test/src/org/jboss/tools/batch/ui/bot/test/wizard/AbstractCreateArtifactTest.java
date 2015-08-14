package org.jboss.tools.batch.ui.bot.test.wizard;

import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.batch.reddeer.editor.JavaClassEditor;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.After;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

public abstract class AbstractCreateArtifactTest extends AbstractBatchTest {

	protected abstract String getPackage();
	
	protected abstract String getClassName();
	
	protected abstract BatchArtifacts getArtifact();

	protected abstract void createArtifactHook(NewBatchArtifactWizardPage page);
	
	@After
	public void tearDown(){
		new DefaultEditor(getClassName() + ".java").close();
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
}
