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

import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks batch job.xml file from within source view and its property features
 * @author odockal
 *
 */
public class ValidateSourceBatchPropertyTest extends AbstractJobXMLSourceTest {

	private final String BATCH_FILE = "/resources/job-property.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
	@Test
	public void testPropertyNotFound() {
		checkProperty("myProperty2", "x", 0, 1);
	}
	
	@Test
	public void testPropertyNotUsedAndNotFound() {
		checkProperty("myProperty1", "x", 0, 2);
	}
	
	@Test
	public void testPropertyNotUsed() {
		checkProperty("property1", "x", 0, 2);		
	}
	
	@Test
	public void testMissingPropertyAttributes() {
		checkProperty("name=\"myProperty2\"/>", "<property/>", 2, 1);
	}
	
	private void checkProperty(String textToFind, String textToInsert, int errorNum, int warningNum) {
		TextEditor source = (TextEditor) getSourcePage();
		source.insertText(source.getPositionOfText(textToFind) + textToFind.length(), textToInsert);
		editor.save();
		assertNumberOfProblems(errorNum, warningNum);
	}
	
}
