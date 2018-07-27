/*******************************************************************************
 * Copyright (c) 2016-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.wizard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;
import org.xml.sax.SAXException;

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
		assertTrue(getProject().containsResource(new String[]{ JAVA_RESOURCES, RESOURCES_FOLDER, META_INF_FOLDER, "batch.xml"}));
	}
	
	private void assertBatchXMLFile() {
		assertThat(getClassByID(ARTIFACT_NAME), is(getPackage() + "." + getClassName()));
	}
}
