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

import org.junit.Test;

/**
 * Checks batch job.xml file from within source view and 
 * validation of ref attribute
 * @author odockal
 *
 */
public class ValidateSourceRefAttributeTest extends AbstractJobXMLReferenceTest {
	
	@Test 
	public void testDecisionReference() {
		referenceCheck(DECIDER_REF);
		emptyReferenceCheck(DECIDER_REF);
	}
	
	@Test
	public void testBatchletReference() {
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
	
	private void referenceCheck(String referenceID) {
		referenceCheck(referenceID, 0, "ref=\"");
	}
}
