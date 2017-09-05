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

import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks batch job.xml file from within source view 
 * and tests uniqueness of ID attributes of elements on the same level
 * @author odockal
 *
 */
public class ValidateSourceUniqueIDTest extends AbstractJobXMLTest {
	
	private final String STEP_ID = "myStep";
	
	private final String DECISION_ID = "myDecision";
	
	private final String SPLIT_ID = "mySplit";
	
	private final String FLOW_ID = "myFlow";
	
	private final String BATCH_JOB_ID = "job-id";
	
	private final String BATCH_FILE = "/resources/job-id.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test
	public void testUniqueFlowID() {
		checkMultipleOccurenceOfID("<flow id=\"" + FLOW_ID + "\"></flow>");
	} 

	@Test
	public void testUniqueSplitID() {
		checkMultipleOccurenceOfID("<split id=\"" + SPLIT_ID + "\"></split>");
	} 
	
	@Test
	public void testUniqueDecisionID() {
		checkMultipleOccurenceOfID("<decision ref=\"decider\" id=\"" + DECISION_ID + "\"></decision>");
	} 
	
	@Test
	public void testUniqueStepID() {
		checkMultipleOccurenceOfID("<step id=\"" + STEP_ID + "\"></step>");
	}
	
	@Test
	public void testUniqueJobID() {
		checkMultipleOccurenceOfID("<step id=\"" + BATCH_JOB_ID + "\"></step>");
	}
	
	@Test
	public void testUniqueIDDiffLevel() {
		TextEditor source = (TextEditor)getSourcePage();
		source.insertLine(source.getLineOfText(
				"</job>") - 1, 
				"<flow id=\"test-flow\">\r\n"
				+ "\t<step id=\"" + STEP_ID + "\"></step>\r\n"
				+ "\t<flow id=\"" + FLOW_ID + "\"></flow>\r\n"
				+ "\t<split id=\"" + SPLIT_ID + "\"></split>\r\n"
				+ "\t<decision ref=\"decider\" id=\"" + DECISION_ID + "\"></decision>\r\n"
				+ "</flow>");
		editor.save();
		assertNumberOfProblems(8, 0);
	}
	
	private void checkMultipleOccurenceOfID(String textToInsert) {
		TextEditor source = (TextEditor)getSourcePage();
		source.insertLine(source.getLineOfText("</job>") - 1, textToInsert);
		editor.save();
		assertNumberOfProblems(2, 0);
	}
}
