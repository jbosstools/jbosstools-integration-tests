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

import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author odockal
 *
 */
public class ContentAssistPropertyTest extends AbstractJobXMLTest {

	public static final String BATCH_FILE = "job-property.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE, false);
	}
	
	@Test
	public void testOneProperty() {
		removeLine("name=\"myProperty2\"");
		verifyElementContentAssistOffering("jobProperties[\'property1", "[\'property1\']");
		assertThat(getProposalsFromSelectedText("name=\"myProperty1", "myProperty1"), hasItems("myProperty1", "myProperty2", "myPropertyRenamed"));
	}
	
	@Test
	public void testTwoProperty() {
		verifyElementContentAssistOffering("jobProperties[\'myProperty1", "[\'myProperty1\']");
		List<String> proposals = getProposalsFromSelectedText("name=\"myProperty2", "myProperty2");
		assertThat(proposals, hasItems("myProperty2", "myPropertyRenamed"));
		assertThat(proposals, not(hasItem("myProperty1")));
	}
	
	private void removeLine(String textToFind) {
		TextEditor source = (TextEditor) getSourcePage();
		source.selectLine(source.getLineOfText(textToFind));
		KeyboardFactory.getKeyboard().type(SWT.DEL);
		performSave(editor.getEditorPart());
	}
}
