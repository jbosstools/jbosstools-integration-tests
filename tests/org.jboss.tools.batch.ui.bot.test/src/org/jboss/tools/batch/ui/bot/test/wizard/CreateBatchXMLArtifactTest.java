package org.jboss.tools.batch.ui.bot.test.wizard;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

public class CreateBatchXMLArtifactTest extends AbstractCreateArtifactTest {

	private static final String ARTIFACT_NAME = "my-artifact-name-batch-xml";
	
	@Override
	protected String getClassName() {
		return getArtifact() + "_batchXMLArtifact";
	}

	@Override
	protected BatchArtifacts getArtifact() {
		return BatchArtifacts.BATCHLET;
	}

	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.setBatchXML();
		page.setArtifactName(ARTIFACT_NAME);
	}
	
	@Test
	public void test() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		createArtifact();
		
		assertNoProblems();
		assertAnnotation();
		assertBatchXMLFileExists();
		assertBatchXMLFile();
	}
	
	private void assertAnnotation() {
		List<String> annotations = getEditor().getClassAnnotations(getClassName());
		
		assertThat(annotations.size(), is(0));
	}
	
	private void assertBatchXMLFileExists() {
		assertTrue(getProject().containsItem(new String[]{RESOURCES_FOLDER, META_INF_FOLDER, "batch.xml"}));
	}
	
	private void assertBatchXMLFile() {
		assertThat(getClassByID(ARTIFACT_NAME), is(getPackage() + "." + getClassName()));
	}
}
