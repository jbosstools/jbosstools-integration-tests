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
 * validation of referencing of elements via next attribute
 * @author odockal
 *
 */
public class ValidateSourceLevelReferenctingTest extends AbstractJobXMLTest {

	private final String BATCH_FILE = "/resources/job-level.xml";
	
	private final String ELEMENT_ID = "my-id";
	
	private final String JOB_LEVEL_ID = "job-level-id";
	
	private final String FLOW_LEVEL_ID = "flow-level-id";
	
	private final String DECISION_REF = "decider";
	
	private final String FLOW_ID = "my-flow";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test
	public void testValidInnerStep() {
		insertElementBefore("\t\t<step id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></step>",
				"</flow>");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidInnerStep() {
		insertElementBefore("\t\t<step id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></step>",
				"</flow>");
		assertNumberOfProblems(0, 1);
	}
	
	@Test
	public void testValidInnerFlow() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></flow>",
				"</flow>");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidInnerFlow() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></flow>",
				"</flow>");
		assertNumberOfProblems(0, 1);
	}
	
	@Test
	public void testValidInnerSplit() {
		insertElementBefore("\t\t<split id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></split>",
				"</flow>");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidInnerSplit() {
		insertElementBefore("\t\t<split id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></split>",
				"</flow>");
		assertNumberOfProblems(0, 1);
	}
	
	@Test
	public void testValidInnerDecision() {
		insertElementBefore("\t\t<decision id=\"" + ELEMENT_ID + "\" "
				+ "ref=\"" + DECISION_REF +  "\">\r\n"
						+ "\t\t\t<next on=\"foo\" to=\"" + FLOW_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</decision>",
						"</flow>");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidInnerDecision() {
		insertElementBefore("\t\t<decision id=\"" + ELEMENT_ID + "\" "
				+ "ref=\"" + DECISION_REF +  "\">\r\n"
						+ "\t\t\t<next on=\"foo\" to=\"" + JOB_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</decision>",
						"</flow>");
		assertNumberOfProblems(0, 1);
	}
	
	@Test
	public void testStepOnJobLevel() {
		insertElementBefore("\t<step id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></step>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidStepOnJobLevel() {
		insertElementBefore("\t<step id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></step>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNumberOfProblems(0, 1);		
	}
	
	@Test
	public void testFlowOnJobLevel() {
		insertElementBefore("\t<flow id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></flow>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidFlowOnJobLevel() {
		insertElementBefore("\t<flow id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></flow>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNumberOfProblems(0, 1);		
	}
	
	@Test
	public void testSplitOnJobLevel() {
		insertElementBefore("\t<split id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + JOB_LEVEL_ID +  "\"></split>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidSplitOnJobLevel() {
		insertElementBefore("\t<split id=\"" + ELEMENT_ID + "\" "
				+ "next=\"" + FLOW_LEVEL_ID +  "\"></split>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNumberOfProblems(0, 1);		
	}
	
	@Test
	public void testDecisionOnJobLevel() {
		insertElementBefore("\t\t<decision id=\"" + ELEMENT_ID + "\" "
				+ "ref=\"" + DECISION_REF +  "\">\r\n"
				+ "\t\t\t<next on=\"foo\" to=\"" + JOB_LEVEL_ID + "\"/>\r\n"
				+ "\t\t</decision>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNoProblems();
	}
	
	@Test
	public void testInvalidDecisionOnJobLevel() {
		insertElementBefore("\t\t<decision id=\"" + ELEMENT_ID + "\" "
				+ "ref=\"" + DECISION_REF +  "\">\r\n"
				+ "\t\t\t<next on=\"foo\" to=\"" + FLOW_LEVEL_ID + "\"/>\r\n"
				+ "\t\t</decision>",
				"<flow id=\"" + FLOW_ID + "\">");
		assertNumberOfProblems(0, 1);		
	}
	
}
