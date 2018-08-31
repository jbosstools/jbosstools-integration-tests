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
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author odockal
 *
 */
public class ContentAssistReferenceAttributeTest extends AbstractJobXMLTest {

	public static final String BATCH_FILE = "job-empty.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE, false);
	}
	
	@Test
	public void testBatchletOffering() {
		insertElementBefore("<step id=\"step-batchlet\"><batchlet ref=\"\"></batchlet></step>", "</job>");
		verifyElementContentAssistOffering("batchlet ref=\"", "batchlet", "batchletProperty");
	}
	
	@Test
	public void testDeciderOffering() {
		insertElementBefore("<decision ref=\"\" id=\"decider-id\"></decision>", "</job>");
		verifyElementContentAssistOffering("decision ref=\"", "decider");
	}
	
	@Test
	public void testJobListenersReferencing() {
		insertElementBefore("<listeners>\n<listener ref=\"\"></listener>\n</listeners>", "</job>");
		verifyElementContentAssistOffering("listener ref=\"", "jobListener", "customClassListener", "src.main.java.CustomListener");
	}
	
	@Test
	public void testStepListenerReferencing() {
		insertElementBefore("<step id=\"step-listener\"><listeners><listener ref=\"\"></listener></listeners></step>", "</job>");
		List<String> proposals = getProposalsFromCursor("listener ref=\"");
		assertThat(proposals, hasItems("stepListener"));
		assertThat(proposals, not(hasItem("chunkListener")));
	}
	
	@Test
	public void testStepChunkListenerReferencing() {
		insertElementBefore("<step id=\"step-chunk-listener\"><listeners><listener ref=\"\"></listener></listeners><chunk></chunk></step>", "</job>");
		List<String> proposals = getProposalsFromCursor("listener ref=\"");
		assertThat(proposals, hasItems("stepListener", "chunkListener"));
	}
	
	@Test
	public void testStepChunkReferences() {
		insertElementBefore(
				"<step id=\"step-chunk\">\n" + 
				"	<chunk>\n" + 
				"		<reader ref=\"\"></reader>\n" + 
				"		<processor ref=\"\"></processor>\n" + 
				"		<writer ref=\"\"></writer>\n" + 
				"		<checkpoint-algorithm ref=\"\"></checkpoint-algorithm>\n" + 
				"		<skippable-exception-classes>\n" + 
				"			<exclude class=\"src.main.\" />\n" + 
				"		</skippable-exception-classes>\n" + 
				"		<no-rollback-exception-classes>\n" + 
				"			<include class=\"src.main.\" />\n" + 
				"		</no-rollback-exception-classes>\n" + 
				"	</chunk>\n" + 
				"</step>", 
				"</job>");
		verifyElementContentAssistOffering("reader ref=\"", "reader");
		verifyElementContentAssistOffering("processor ref=\"", "processor");
		verifyElementContentAssistOffering("writer ref=\"", "writer");
		verifyElementContentAssistOffering("checkpoint-algorithm ref=\"", "checkpointAlgorithm");
		verifyElementContentAssistOffering("exclude class=\"src.main.", "ExcludeException");
		verifyElementContentAssistOffering("include class=\"src.main.", "IncludeException");
	}
	
	@Test
	public void testStepPartitionReferences() {
		insertElementBefore("<step id=\"step-partition\">\n" + 
				"		<partition>\n" + 
				"			<mapper ref=\"\"></mapper>\n" + 
				"			<collector ref=\"\"></collector>\n" + 
				"			<analyzer ref=\"\"></analyzer>\n" + 
				"			<reducer ref=\"\"></reducer>\n" + 
				"		</partition>\n" + 
				"	</step>",
				"</job>");
		verifyElementContentAssistOffering("mapper ref=\"", "mapper");
		verifyElementContentAssistOffering("collector ref=\"", "collector");
		verifyElementContentAssistOffering("analyzer ref=\"", "analyzer");
		verifyElementContentAssistOffering("reducer ref=\"", "reducer");
	}
}
