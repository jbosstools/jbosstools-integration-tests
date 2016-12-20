/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.completion;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.editor.ExtendedTextEditor;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test operates on JAX-RS completion
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
public class RESTfulCompletionTest extends RESTfulTestBase{

	private static final String GET_METHOD_PATH = "/{userId}";
	private static final String CORRECT_PATH_PARAM = "userId";
	private static final String INCORRECT_PATH_PARAM = "someId";
	private static final String PATH_PARAM_NAVIGATION = "@PathParam(";
	private static final List<String> EXP_NON_EMPTY_COMPLETION_RESULT = 
			Arrays.asList("userId - JAX-RS Mapping");
	private static final List<String> EXP_EMPTY_COMPLETION_RESULT = 
			Arrays.asList("No Default Proposals");

	@Override
	protected String getWsProjectName() {
		return "restEmpty";
	}

	@Test
	public void testWithEmptyPrefix() {
		prepareSimpleRestService(GET_METHOD_PATH, "");

		testContentAssistantProposal(PATH_PARAM_NAVIGATION.length() + 1,
				EXP_NON_EMPTY_COMPLETION_RESULT);
	}

	@Test
	public void testWithValidPrefixAtTheEnd() {
		prepareSimpleRestService(GET_METHOD_PATH, CORRECT_PATH_PARAM);

		testContentAssistantProposal(PATH_PARAM_NAVIGATION.length()
				+ CORRECT_PATH_PARAM.length() + 1,
				EXP_NON_EMPTY_COMPLETION_RESULT);
		
	}

	@Test
	public void testWithValidPrefixInTheBeginning() {
		prepareSimpleRestService(GET_METHOD_PATH, CORRECT_PATH_PARAM);

		testContentAssistantProposal(PATH_PARAM_NAVIGATION.length() + 1,
				EXP_NON_EMPTY_COMPLETION_RESULT);
	}

	@Test
	public void testWithInvalidPrefixAtTheEnd() {
		prepareSimpleRestService(GET_METHOD_PATH, INCORRECT_PATH_PARAM);

		testContentAssistantProposal(PATH_PARAM_NAVIGATION.length()
				+ INCORRECT_PATH_PARAM.length() + 1,
				EXP_EMPTY_COMPLETION_RESULT);
		
	}

	@Test
	public void testWithInvalidPrefixInTheBeginning() {
		prepareSimpleRestService(GET_METHOD_PATH, INCORRECT_PATH_PARAM);

		testContentAssistantProposal(PATH_PARAM_NAVIGATION.length() + 1,
				EXP_NON_EMPTY_COMPLETION_RESULT);
	}

	@Test
	public void testWithAllInvalidParamSelection() {
		prepareSimpleRestService(GET_METHOD_PATH, INCORRECT_PATH_PARAM);

		testContentAssistantProposal(INCORRECT_PATH_PARAM, EXP_NON_EMPTY_COMPLETION_RESULT);
	}

	private void testContentAssistantProposal(String textToSelect,
			List<String> expected) {
		TextEditor editor = new TextEditor(getWsName() + ".java");
		editor.selectText(INCORRECT_PATH_PARAM);

		assertContentAssistantProposal(editor, expected);
	}

	private void testContentAssistantProposal(int position,
			List<String> expected) {
		ExtendedTextEditor editor = new ExtendedTextEditor(getWsName() + ".java");
		int line = editor.getLineNum(StringContains.containsString(PATH_PARAM_NAVIGATION));
		int column = editor.getTextAtLine(line).indexOf(PATH_PARAM_NAVIGATION);
		editor.setCursorPosition(line, column + position);

		assertContentAssistantProposal(editor, expected);
	}

	private void assertContentAssistantProposal(TextEditor editor, List<String> expected) {
		AbstractWait.sleep(TimePeriod.SHORT);

		ContentAssistant assistant = editor.openContentAssistant();
		List<String> proposals = assistant.getProposals();
		assistant.close();

		assertTrue("Content assist should contain "
						+ Arrays.toString(expected.toArray())
						+ "\nbut there are "
						+ Arrays.toString(proposals.toArray()),
				proposals.containsAll(expected));
	}
}
