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
 * validation of restart attribute
 * @author odockal
 *
 */
public class ValidateSourceRestartAttributeTest extends AbstractJobXMLSourceTest {

	private final String BATCH_FILE = "/resources/job-level.xml";
	
	private final String ELEMENT_ID = "my-id";
	
	private final String JOB_LEVEL_ID = "job-level-id";
	
	private final String FLOW_LEVEL_ID = "flow-level-id";
	
	private final String FLOW_ID = "my-flow";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test 
	public void testValidStepStopRestartAttr() {
		insertElementBefore("\t\t<step id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + JOB_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</step>",
						"</flow>");
		assertNoProblems();
	}
	
	@Test 
	public void testInvalidStepStopRestartAttr() {
		insertElementBefore("\t\t<step id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + FLOW_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</step>",
						"</flow>");
		assertNumberOfProblems(0, 1);
	}
	
	@Test 
	public void testValidFlowStopRestartAttr() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + JOB_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</flow>",
						"</flow>");
		assertNoProblems();
	}
	
	@Test 
	public void testInvalidFlowStopRestartAttr() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + FLOW_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</flow>",
						"</flow>");
		assertNumberOfProblems(0, 1);
	}

	@Test 
	public void testFlowStopRestartAttrAtSameLevel() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + FLOW_ID + "\"/>\r\n"
						+ "\t\t</flow>",
						"</job>");
		assertNoProblems();
	}
	
	@Test 
	public void testFlowStopRestartAttrAtDiffLevel() {
		insertElementBefore("\t\t<flow id=\"" + ELEMENT_ID + "\">\r\n"
						+ "\t\t\t<stop on=\"foo\" restart=\"" + FLOW_LEVEL_ID + "\"/>\r\n"
						+ "\t\t</flow>",
						"</job>");
		assertNumberOfProblems(0, 1);
	}
	
}
