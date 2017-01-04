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
package org.jboss.tools.ws.ui.bot.test.websocket;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

import java.util.*;

import static org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper.createClass;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

class StubMethodsHelper {

	/**
	 * Shell with this label is used. Initialized in first usage (requires opened editor).
	 */
	private static String CORRECT_CONTENT_ASSISTANT_LABEL = null;

	static void callAllStubProposals(TextEditor editor) {
		Set<String> remainingProposals = new HashSet<>(EXPECTED_PROPOSALS);

		for (String proposal : EXPECTED_PROPOSALS) {
			//Call proposal
			ContentAssistant contentAssistant = openContentAssistant(editor);
			contentAssistant.chooseProposal(proposal);

			//Update tmp collection
			remainingProposals.remove(proposal);

			//Asserts
			assertNoMarkerError(editor.getMarkers());
			assertThereAreOnlySpecifiedStubProposals(editor, remainingProposals);
			assertOutlineViewContainsNewMethod(editor, proposal);
		}
	}

	//ASSERTS:
	private static void assertNoMarkerError(List<Marker> markers) {
		for (Marker marker : markers) {
			if (marker.getText().contains("error")) {
				fail("There is an error in the markers! " + marker.getText());
			}
		}
	}

	/**
	 * Stub proposal should be in current proposals if and only if
	 * collection shouldBe contains it.
	 *
	 * @param editor   
	 * @param shouldBe
	 */
	static void assertThereAreOnlySpecifiedStubProposals(
			TextEditor editor, Collection<String> shouldBe) {

		//Preparing collections
		List<String> proposals = new ArrayList<>(EXPECTED_PROPOSALS);
		proposals.removeAll(shouldBe);
		List<String> shouldNotBe = proposals;

		ContentAssistant contentAssistant = openContentAssistant(editor);
		List<String> actualProposals = contentAssistant.getProposals();
		contentAssistant.close();

		//check
		assertTrue("There are not all expected proposals!", actualProposals.containsAll(shouldBe));
		assertTrue("There are proposals those should not be there!", Collections.disjoint(actualProposals, shouldNotBe));
	}

	private static void assertOutlineViewContainsNewMethod(TextEditor editor, String proposal) {
		String newMethodName = proposal.split("[(]")[0];

		for (TreeItem outlineItem : new OutlineView().outlineElements()) {
			if (SERVER_CLASS_NAME.equals(outlineItem.getText()) || CLIENT_CLASS_NAME.equals(outlineItem.getText())) {
				for(TreeItem classItem : outlineItem.getItems()){
					if (classItem.getText().startsWith(newMethodName)) {
						//OK
						editor.activate();
						return;
					}
				}
				fail("New method not found in Class. Method: " + newMethodName);
			}
		}
		fail("Class not found in Outline View!");
	}

	//Open ContentAssistant methods:
	/**
	 * In first call methods finds ContentAssistant with StubMethodsTest.Constants.EXPECTED_PROPOSALS
	 * and remembers its label in CORRECT_CONTENT_ASSISTANT_LABEL.
	 *
	 * @return opened content assistant
	 */
	static ContentAssistant openContentAssistant(TextEditor editor) {

		//first call
		if (CORRECT_CONTENT_ASSISTANT_LABEL == null) {
			findCorrectContentAssistantLabel(editor);
		}

		//finding the correct content assistant
		ContentAssistant contentAssistant = editor.openContentAssistant();
		String labelValue = new DefaultLabel(contentAssistant).getText();
		while (!CORRECT_CONTENT_ASSISTANT_LABEL.equals(labelValue)) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
			labelValue = new DefaultLabel(contentAssistant).getText();
		}
		return contentAssistant;
	}

	private static void findCorrectContentAssistantLabel(TextEditor editor) {
		ContentAssistant contentAssistant = editor.openContentAssistant();

		//studio behavior fix
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		contentAssistant = editor.openContentAssistant();

		String labelValue = new DefaultLabel(contentAssistant).getText();
		while (!contentAssistant.getProposals().containsAll(EXPECTED_PROPOSALS)) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
			labelValue = new DefaultLabel(contentAssistant).getText();
		}
		CORRECT_CONTENT_ASSISTANT_LABEL = labelValue;
		contentAssistant.close();
	}

	//Prepare class methods:
	static TextEditor prepareClass(String className) {
		switch (className) {
			case SERVER_CLASS_NAME:
				return prepareClass(SERVER_CLASS_NAME,
						"import javax.websocket.server.ServerEndpoint;",
						"@ServerEndpoint(\"/test/\")");
			case CLIENT_CLASS_NAME:
				return prepareClass(CLIENT_CLASS_NAME,
						"import javax.websocket.ClientEndpoint;",
						"@ClientEndpoint");
			default:
				throw new RuntimeException("Unknown class to prepare!");
		}
	}

	private static TextEditor prepareClass(
			String className, String importCommand, String annotation) {

		TextEditor editor = createClass(PROJECT_NAME, PROJECT_PACKAGE, className);
		DefaultStyledText text = new DefaultStyledText();
		text.insertText(1, 0, System.lineSeparator());
		text.insertText(2, 0, importCommand);
		text.insertText(3, 0, System.lineSeparator());
		text.insertText(3, 0, annotation);

		//set cursor position at the first line in the class, column 0
		int firstLineInClassIndex = getLineOfClassDefinition(editor) + 1;
		editor.setCursorPosition(firstLineInClassIndex, 0);

		return editor;
	}

	static int getLineOfClassDefinition(TextEditor editor) {
		return editor.getLineOfText("public class");
	}

	//Prefixes methods:
	static List<String> filterStubProposalsWithPrefix(String prefix) {
		List<String> matches = new ArrayList<>();
		for (String proposal : EXPECTED_PROPOSALS) {
			if (proposal.startsWith(prefix))
				matches.add(proposal);
		}
		return matches;
	}

	static void setPrefixIntoFirstClassLine(TextEditor editor, String prefix) {
		int firstLineInClassIndex = getLineOfClassDefinition(editor) + 1;
		new DefaultStyledText().insertText(firstLineInClassIndex, 0, prefix);
		
		//jump to the end of the line
		editor.setCursorPosition(firstLineInClassIndex, editor.getTextAtLine(firstLineInClassIndex).length());
	}

	static void clearFirstClassLine(TextEditor editor) {
		delFirstClassLine(editor);
		int firstLineInClassIndex = getLineOfClassDefinition(editor) + 1;
		new DefaultStyledText().insertText(firstLineInClassIndex, 0, System.lineSeparator());
	}

	static void delFirstClassLine(TextEditor editor) {
		int firstLineInClassIndex = getLineOfClassDefinition(editor) + 1;
		editor.selectLine(firstLineInClassIndex);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
	}

	static int countImports(TextEditor editor) {
		return StringUtils.countMatches(editor.getText(), "import ");
	}
}
