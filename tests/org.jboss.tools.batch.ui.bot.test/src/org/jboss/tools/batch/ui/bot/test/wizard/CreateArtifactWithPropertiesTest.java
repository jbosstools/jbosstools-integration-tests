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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;

public class CreateArtifactWithPropertiesTest extends AbstractCreateArtifactTest {

	private static final String PROPERTY = "myProperty";
	
	@Override
	protected String getClassName() {
		return getArtifact() + "_propertyArtifact";
	}

	protected BatchArtifacts getArtifact() {
		return BatchArtifacts.BATCHLET;
	}

	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.addProperty(PROPERTY);
		assertThat(page.getProperties(), hasItem(PROPERTY));

		page.removeProperty(PROPERTY);
		assertThat(page.getProperties(), not(hasItem(PROPERTY)));
		
		page.addProperty(PROPERTY);
		assertThat(page.getProperties(), hasItem(PROPERTY));
	}

	@Test
	public void test(){
		createArtifact();
		
		assertNoProblems();
		assertPropertyAnnotation();
	}

	private void assertPropertyAnnotation() {
		List<String> annotations = getEditor().getFieldAnnotations(PROPERTY);
		
		assertThat(annotations.size(), is(2));
		assertThat(annotations, hasItem("Inject"));		
		assertThat(annotations, hasItem("BatchProperty"));
	}
}
