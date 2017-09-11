/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.junit.Test;

public class ValidateDesignRefAttributeTest extends AbstractJobXMLReferenceTest {
	
	@Test
	public void testBatchletReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "step", "Batchlet");
		DefaultText text = new DefaultText(new DefaultSection("Batchlet"), 0);
		designReferenceCheck(text, BATCHLET_REF);
	}
	
	@Test
	public void testDecisionReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "decision");
		DefaultText text = new DefaultText(new DefaultSection("Decision"), 1);
		designReferenceCheck(text, DECIDER_REF);
	}
	
	@Test
	public void testChunkReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "chunk-step", "Chunk");
		DefaultText text = new DefaultText(new DefaultSection("Processor"), 0);
		designReferenceCheck(text, PROCESSOR_REF);
		text = new DefaultText(new DefaultSection("Checkpoint Algorithm"), 0);
		designReferenceCheck(text, CHECK_REF);
	}	
	
	@Test
	public void testChunkReaderReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "chunk-step", "Chunk", "Reader");
		DefaultText text = new DefaultText(new DefaultSection("Reader"), 0);
		designReferenceCheck(text, READER_REF);
	}
	
	@Test
	public void testChunkWriterReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "chunk-step", "Chunk", "Writer");
		DefaultText text = new DefaultText(new DefaultSection("Writer"), 0);
		designReferenceCheck(text, WRITER_REF);
	}
	
	@Test
	public void testChunkProcessorReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "chunk-step", "Chunk", PROCESSOR_REF);
		DefaultText text = new DefaultText(new DefaultSection("Processor"), 0);
		designReferenceCheck(text, PROCESSOR_REF);
	}
	
	@Test
	public void testChunkCheckpointReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "chunk-step", "Chunk", CHECK_REF);
		DefaultText text = new DefaultText(new DefaultSection("Checkpoint Algorithm"), 0);
		designReferenceCheck(text, CHECK_REF);
	}
	
	@Test
	public void testListenerReference() {
		editor.getDesignPage().selectNode("Job", "Listeners", JOB_LISTENER_REF);
		DefaultText text = new DefaultText(new DefaultSection(getMyArtifact(JOB_LISTENER_REF)), 0);
		designReferenceCheck(text, JOB_LISTENER_REF);		
	}
	
	@Test
	public void testCustomListenerReference() {
		editor.getDesignPage().selectNode("Job", "Listeners", CUSTOM_LISTENER_REF);
		DefaultText text = new DefaultText(new DefaultSection(getMyArtifact(CUSTOM_LISTENER_REF)), 0);
		designReferenceCheck(text, CUSTOM_LISTENER_REF);
	}
	
	@Test
	public void testCustomQualifiedListenerReference() {
		editor.getDesignPage().selectNode("Job", "Listeners", "src.main.java." + getMyArtifact(CUSTOM_QUALIFIED_LISTENER_REF));
		DefaultText text = new DefaultText(new DefaultSection("Src.main.java." + getMyArtifact(CUSTOM_QUALIFIED_LISTENER_REF)), 0);
		designReferenceCheck(text, "src.main.java." + getMyArtifact(CUSTOM_QUALIFIED_LISTENER_REF));
	}
	
	@Test
	public void testStepListener() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "step-listener", "Listeners", STEP_LISTENER_REF);		
		DefaultText text = new DefaultText(new DefaultSection(getMyArtifact(getMyArtifact(STEP_LISTENER_REF))), 0);
		designReferenceCheck(text, STEP_LISTENER_REF);
	}

	@Test 
	public void testPartitionElementsReference() {
		editor.getDesignPage().selectNode("Job", "Flow Elements", "step-partition", "Partition");		
		DefaultText text = new DefaultText(new DefaultSection("Mapper"), 0);
		designReferenceCheck(text, MAPPER_REF);		
		text = new DefaultText(new DefaultSection("Collector"), 0);
		designReferenceCheck(text, COLLECTOR_REF);	
		text = new DefaultText(new DefaultSection("Analyzer"), 0);
		designReferenceCheck(text, ANALYZER_REF);
		text = new DefaultText(new DefaultSection("Reducer"), 0);
		designReferenceCheck(text, REDUCER_REF);	
	}
	
	private void designReferenceCheck(DefaultText text, String expected) {
		assertEquals(expected, text.getText());
		assertNoProblems();
		setText(text, "");
		assertNumberOfProblems(1, 0);
		setText(text, "bad_ref");
		assertNumberOfProblems(0, 1);	
		setText(text, expected);
		assertNoProblems();
		editor.activate();
	}
	
	private void setText(DefaultText text, String setText) {
		text.setText(setText);
		AbstractWait.sleep(TimePeriod.getCustom(1));
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

}
