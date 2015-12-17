/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import java.util.Iterator;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test Markers position and attributes regarding to VPE components within jsp
 * page
 * 
 * @author Vladimir Pakan
 *
 */
public class MarkersTest extends VPEEditorTestCase {
	private TextEditor editor;
	private String originalEditorText;

	/**
	 * Test open on functionality of JSF components within jsp page
	 */
	@Test
	public void testMarkers() {
		String textToSelect = "<%@ taglib";
		String insertText = "yyaddedxx";
		int[] expectedMarkerLines = new int[4];
		String[] expectedMarkerDesc = new String[4];
		int cursorOffset = editor.getPositionOfText(textToSelect) + textToSelect.length();
		editor.setCursorPosition(cursorOffset);
		editor.insertText(cursorOffset,insertText);
		expectedMarkerLines[0] = editor.getCursorPosition().x + 1;
		expectedMarkerDesc[0] = "^Unknown tag \\(jsp:directive\\.taglib" + insertText + "\\).*";
		textToSelect = "<%@ taglib uri=\"http://java.sun.com/jsf/html";
		cursorOffset = editor.getPositionOfText(textToSelect) + textToSelect.length();
		editor.setCursorPosition(cursorOffset);
		editor.insertText(cursorOffset,insertText);
		expectedMarkerLines[1] = editor.getCursorPosition().x + 1;
		expectedMarkerDesc[1] = "^Can not find the tag library descriptor for \"http://java.sun.com/jsf/html"
				+ insertText + ".*";
		textToSelect = "<h1";
		cursorOffset = editor.getPositionOfText(textToSelect) + textToSelect.length();
		editor.setCursorPosition(cursorOffset);
		editor.insertText(cursorOffset,insertText);
		expectedMarkerLines[2] = editor.getCursorPosition().x + 1;
		expectedMarkerDesc[2] = "^Unknown tag \\(h1" + insertText + "\\).*";
		textToSelect = "</h:inputText";
		cursorOffset = editor.getPositionOfText(textToSelect) + textToSelect.length();
		editor.setCursorPosition(cursorOffset);
		editor.insertText(cursorOffset,insertText);
		expectedMarkerLines[3] = editor.getCursorPosition().x + 1;
		expectedMarkerDesc[3] = "^Missing start tag for \"h:inputText" + insertText + ".*";
		editor.save();
		new WaitWhile(new JobIsRunning());
		// Check markers
		List<Marker> markers = editor.getMarkers();
		for (int index = 0; index < expectedMarkerLines.length; index++) {
			boolean notFound = true;
			Iterator<Marker> itMarkers = markers.iterator();
			while (itMarkers.hasNext() && notFound){
				Marker marker = itMarkers.next();
				if (marker.getLineNumber() == expectedMarkerLines[index]
						&& marker.getText().matches(expectedMarkerDesc[index])){
					notFound = false;
				}
			}
			assertFalse("Resource: " + TEST_PAGE +
			        " doesn't have marker on line " + expectedMarkerLines[index] +
			        " with descritpion matching regular expression '" + expectedMarkerDesc[index] + "'",
			      notFound);
		}
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		openPage(TEST_PAGE);
		editor = new TextEditor(TEST_PAGE);
		originalEditorText = editor.getText();
	}

	@Override
	public void tearDown() throws Exception {
		if (editor != null) {
			editor.setText(originalEditorText);
			editor.save();
			editor.close();
		}
		super.tearDown();
	}
}