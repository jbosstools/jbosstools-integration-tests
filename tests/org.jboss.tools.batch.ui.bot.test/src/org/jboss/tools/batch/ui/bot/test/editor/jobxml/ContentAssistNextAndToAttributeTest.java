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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author odockal
 *
 */
public class ContentAssistNextAndToAttributeTest extends AbstractJobXMLTest {

	public static final String BATCH_FILE = "job-level.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE, false);
	}
	
	@Test
	public void testDecisionNextToAttribute() {
		insertElementBefore("		<decision ref=\"decider\" id=\"decision\">\n" + 
				"			<next on=\"FAILED\" to=\"\"/>\n" + 
				"		</decision>", "</flow>");
		assertProposals(getProposalsFromCursor("next on=\"FAILED\" to=\""));
	}
	
	@Test
	public void testFlowNextToAttribute() {
		insertElementBefore("<next on=\"COMPLETE\" to=\"\"/>", "</flow>");
		List<String> proposals = getProposalsFromCursor("next on=\"COMPLETE\" to=\"");
		assertThat(proposals, hasItems("step-job-level-id"));
		assertThat(proposals, not(hasItems("step-inner-level-id", "flow-inner-level-id", "split-inner-level-id")));
	}
	
	@Test
	public void testFlowNextAttribute() {
		insertElementBefore("<flow id=\"flow-next\" next=\"\"></flow>", "</flow>");
		assertProposals(getProposalsFromCursor("id=\"flow-next\" next=\""));		
	}
	
	@Test
	public void testSplitNextAttribute() {
		insertElementBefore("<split id=\"split-next\" next=\"\"></split>", "</flow>");
		assertProposals(getProposalsFromCursor("id=\"split-next\" next=\""));		
	}
	
	@Test
	public void testStepNextAttribute() {
		insertElementBefore("<step id=\"step-next\" next=\"\"></step>", "</flow>");
		assertProposals(getProposalsFromCursor("id=\"step-next\" next=\""));		
	}
	
	private void assertProposals(List<String> proposals) {
		assertThat(proposals, hasItems("step-inner-level-id", "flow-inner-level-id", "split-inner-level-id"));
		assertThat(proposals, not(hasItems("step-job-level-id", "flow-job-level-id")));
	}
	
}
