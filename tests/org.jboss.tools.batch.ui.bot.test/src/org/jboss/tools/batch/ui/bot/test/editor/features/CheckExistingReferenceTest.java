/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.editor.features;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Test;

/**
 * Test class for checking referenced artifact classes and properties in search.
 * 
 * @author odockal
 *
 */
public class CheckExistingReferenceTest extends AbstractFeatureBaseTest {

	private static final String EXCEPTION_STEP_ID = "My-exception-step";
	
	private static final String BATCHLET_STEP_ID = "My-batch-step";
	
	private static final String PROPERTY_STEP_ID = "My-prop-step";
	
	private static final String READER_CLASS = "MyReader";
	
	private static final String WRITER_CLASS = "MyWriter";
	
	private static final String READER_ID = getBatchArtifactID(READER_CLASS);
	
	private static final String WRITER_ID = getBatchArtifactID(WRITER_CLASS);
	
	@Test
	public void testArtifactReference() {
		createBatchArtifact(BatchArtifacts.BATCHLET, BATCHLET_ID);
		closeEditor(BATCHLET_JAVA_CLASS);
		// adding new step and batchlet via design view
		addStep(BATCHLET_STEP_ID);
		addBatchlet(BATCHLET_STEP_ID, getBatchArtifactID(BATCHLET_ID));
		editor.save();
		
		new WaitWhile(new JobIsRunning());
		
		assertNoProblems();
		
		assertTrue(searchForClassReference(JOB_XML_FILE, 
				new String[]{JAVA_FOLDER, getPackage(), BATCHLET_JAVA_CLASS}));
	}
	
	@Test
	public void testArtifactPropertyReference() {
		assertTrue(createBatchArtifactWithProperty(BatchArtifacts.BATCHLET, 
				BATCHLET_PROPERTY_ID, PROPERTY_NAME));
		// adding new step, batchlet with property via design view
		addStep(PROPERTY_STEP_ID);
		addBatchlet(PROPERTY_STEP_ID, getBatchArtifactID(BATCHLET_PROPERTY_ID));
		getDesignPage().addProperty(PROPERTY_STEP_ID, "Batchlet", PROPERTY_NAME, "test value");
		editor.save();
		
		new WaitWhile(new JobIsRunning());
		
		assertNoProblems();
		
		assertTrue("Property with name " + PROPERTY_NAME + " was not found in search results.", 
				searchForPropertyInFile(JOB_XML_FILE, PROPERTY_NAME, 
						new String[]{JAVA_FOLDER, getPackage(), BATCHLET_PROPERTY_JAVA_CLASS}));
	}
	
	@Test
	public void testExceptionClassReference() {
		// create necessary artifacts
		assertTrue(createExceptionClass(EXCEPTION_ID));
		new WaitWhile(new JobIsRunning());
		addDefaultSerialVersionID(EXCEPTION_JAVA_CLASS, 3);
		closeEditor(EXCEPTION_JAVA_CLASS);
		
		createBatchArtifact(BatchArtifacts.ITEM_READER, READER_CLASS);
		closeEditor(getFullFileName(READER_CLASS, "java"));
		createBatchArtifact(BatchArtifacts.ITEM_WRITER, WRITER_CLASS);
		closeEditor(getFullFileName(WRITER_CLASS, "java"));
		
		new WaitWhile(new JobIsRunning());
		
		assertNoProblems();
		
		// add new step, chunk and exception class artifact via design view
		addStep(EXCEPTION_STEP_ID);
		addChunk(EXCEPTION_STEP_ID);
		setReaderRef(EXCEPTION_STEP_ID, READER_ID);
		setWriterRef(EXCEPTION_STEP_ID, WRITER_ID);
		getDesignPage().addExceptionClass(EXCEPTION_STEP_ID, "Chunk", "Skippable Exception Classes", 
				"Skippable-exception-classes", getPackage() + "." + EXCEPTION_ID);
		editor.save();
		
		new WaitWhile(new JobIsRunning());
		
		assertNoProblems();
		
		assertTrue(searchForClassReference(JOB_XML_FILE, 
				new String[]{JAVA_FOLDER, getPackage(), EXCEPTION_JAVA_CLASS}));
	}
}
