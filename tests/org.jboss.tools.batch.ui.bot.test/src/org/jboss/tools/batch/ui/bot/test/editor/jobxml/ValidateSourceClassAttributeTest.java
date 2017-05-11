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
 * tests exception referencing from class attribute
 * @author odockal
 *
 */
public class ValidateSourceClassAttributeTest extends AbstractJobXMLTest {

	private final String INCLUDE_EXCEPTION = getFullyQualifiedClassName("IncludeException");
	
	private final String EXCLUDE_EXCEPTION = getFullyQualifiedClassName("ExcludeException");
	
	private final String BATCH_FILE = "/resources/job-class.xml";
	
	@Override
	protected String getPackage() {
		return "src.main.java";
	}
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test
	public void testIncludeClassException() {
		classCheck(INCLUDE_EXCEPTION, 0);
		emptyClassCheck(INCLUDE_EXCEPTION, 0);
	}
	
	@Test
	public void testExceludeClassException() {
		classCheck(EXCLUDE_EXCEPTION, 0);
		emptyClassCheck(EXCLUDE_EXCEPTION, 0);		
	}
	
	@Test
	public void testIncludeExcludeException() {
		classCheck(INCLUDE_EXCEPTION, 1);
		emptyClassCheck(INCLUDE_EXCEPTION, 1);
		classCheck(EXCLUDE_EXCEPTION, 1);
		emptyClassCheck(EXCLUDE_EXCEPTION, 1);		
	}
	
	private void classCheck(String referenceID, int index) {
		referenceCheck(referenceID, index, "class=\"");
	}	
	
	protected void emptyReferenceCheck(String referenceID, int index) {
		emptyReferenceCheck(referenceID, index, "ref=\"");
	}	
	
	protected void emptyClassCheck(String referenceID, int index) {
		emptyReferenceCheck(referenceID, index, "class=\"");
	}
	
}
