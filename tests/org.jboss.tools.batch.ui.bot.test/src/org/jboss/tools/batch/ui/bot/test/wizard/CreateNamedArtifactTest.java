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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;

public class CreateNamedArtifactTest extends AbstractCreateArtifactTest {

	private static final String ARTIFACT_NAME = "my-artifact-name";
	
	@Override
	protected String getClassName() {
		return getArtifact() + "_namedArtifact";
	}

	@Override
	protected BatchArtifacts getArtifact() {
		return BatchArtifacts.BATCHLET;
	}

	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.setNamed();
		page.setArtifactName(ARTIFACT_NAME);
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
		
		assertThat(annotations.size(), is(1));
		assertThat(annotations, hasItem(NAMED_ANNOTATION));		

		assertThat(getEditor().getClassAnnotationValue(getClassName(), NAMED_ANNOTATION), is(ARTIFACT_NAME));
	}
	
	private void assertBatchXMLFileContent() {
		assertTrue(getClassByID(ARTIFACT_NAME).isEmpty());
	}
}
