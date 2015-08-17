package org.jboss.tools.batch.ui.bot.test.wizard;

import java.util.List;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateClassloaderArtifactTest extends AbstractCreateArtifactTest {

	@Override
	protected String getClassName() {
		return getArtifact() + "_classloaderArtifact";
	}

	@Override
	protected BatchArtifacts getArtifact() {
		return BatchArtifacts.BATCHLET;
	}

	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.setClassloader();

		assertFalse((new LabeledText("Artifact name:").isEnabled()));
	}
	
	@Test
	public void test(){
		createArtifact();
		
		assertNoProblems();
		assertAnnotation();
		assertBatchXMLFileContent();
	}
	
	private void assertAnnotation() {
		List<String> annotations = getEditor().getClassAnnotations(getClassName());
		
		assertThat(annotations.size(), is(0));
	}
	
	private void assertBatchXMLFileContent() {
		assertTrue(getClass(getClassName()).isEmpty());
	}
}
