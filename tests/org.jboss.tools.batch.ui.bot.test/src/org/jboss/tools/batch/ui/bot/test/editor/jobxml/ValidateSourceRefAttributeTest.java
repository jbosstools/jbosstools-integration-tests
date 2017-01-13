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
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import org.junit.Before;
import org.junit.Test;

/**
 * Checks batch job.xml file from within source view and 
 * validation of ref attribute
 * @author odockal
 *
 */
public class ValidateSourceRefAttributeTest extends AbstractJobXMLSourceTest {
	
	private final String BATCHLET_REF = "batchlet";
	
	private final String STEP_LISTENER_REF = "stepListener";
	
	private final String READER_REF = "reader";
	
	private final String WRITER_REF = "writer";
	
	private final String JOB_LISTENER_REF = "jobListener";
	
	private final String CUSTOM_LISTENER_REF = "customClassListener";
	
	private final String CUSTOM_QUALIFIED_LISTENER_REF = "customListener";
	
	private final String PROCESSOR_REF = "processor";
	
	private final String CHECK_REF = "checkpointAlgorithm";
	
	private final String DECIDER_REF = "decider";
	
	private final String MAPPER_REF = "mapper";
	
	private final String REDUCER_REF = "reducer";
	
	private final String ANALYZER_REF = "analyzer";
	
	private final String COLLECTOR_REF = "collector";	
	
	private final String BATCH_FILE = "job-ref.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test 
	public void testDecisionReference() {
		referenceCheck(DECIDER_REF);
		emptyReferenceCheck(DECIDER_REF);
	}
	
	@Test
	public void testBatchletReferences() {
		referenceCheck(BATCHLET_REF);
		emptyReferenceCheck(BATCHLET_REF);
	}
	
	@Test
	public void testListenerReference() {
		referenceCheck(JOB_LISTENER_REF);
		emptyReferenceCheck(JOB_LISTENER_REF);
	}
	
	@Test
	public void testCustomListenerReference() {
		referenceCheck(CUSTOM_LISTENER_REF);
		emptyReferenceCheck(CUSTOM_LISTENER_REF);
	}
	
	@Test
	public void testCustomQualifiedListenerReference() {
		referenceCheck("src.main.java." + getMyArtifact(CUSTOM_QUALIFIED_LISTENER_REF));
		emptyReferenceCheck("src.main.java." + getMyArtifact(CUSTOM_QUALIFIED_LISTENER_REF));
	}
	
	@Test
	public void testChunkElementsReference() {
		referenceCheck(READER_REF);
		referenceCheck(WRITER_REF);
		referenceCheck(PROCESSOR_REF);
		referenceCheck(CHECK_REF);
	}
	
	@Test
	public void testPartitionElementsReference() {
		referenceCheck(MAPPER_REF);
		referenceCheck(REDUCER_REF);
		referenceCheck(COLLECTOR_REF);
		referenceCheck(ANALYZER_REF);		
	}
	
	@Test
	public void testStepListener() {
		referenceCheck(STEP_LISTENER_REF);
		emptyReferenceCheck(STEP_LISTENER_REF);
	}
}
