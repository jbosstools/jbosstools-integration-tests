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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;

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
