/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public abstract class VPEEditorTestCase extends VPEAutoTestCase {

	private String editorText;
	private TextEditor editor;

	protected String getEditorText() {
		return editorText;
	}

	protected void setEditorText(String textEditor) {
		this.editorText = textEditor;
	}

	protected TextEditor getEditor() {
		return editor;
	}

	protected void setEditor(TextEditor editor) {
		this.editor = editor;
	}

	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
				+ "resources/editor/" + testPage; //$NON-NLS-1$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() //$NON-NLS-1$
					+ "editor/" + testPage; //$NON-NLS-1$
		}
		return filePath;
	}

	protected String getPathToRootResources(String testPage) throws IOException {
		return super.getPathToResources(testPage);
	}

	protected void openPage() {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		// wait for page to be opened
		new TextEditor(TEST_PAGE);
	}

	void checkVPE(String testPage) throws Throwable {
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}

	@Override
	public void tearDown() throws Exception {

		// Restore page state before tests
		if (editor != null) {
			editor.activate();
			editor.setText(editorText);
			editor.save();
			AbstractWait.sleep(TimePeriod.getCustom(3));
		}
		super.tearDown();
	}

	/**
	 * Returns HTML Source striped from spaces, tabs and EOL
	 * 
	 * @return String
	 */
	protected static String stripHTMLSourceText(String editorText) {
		return editorText.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\b", "").replaceAll(" ", "")
				.replaceAll("\r", "").replaceAll("\f", "");
	}

	/**
	 * Asserts if Visual Editor contains node with particular attributes
	 * 
	 * @param webBrowser
	 * @param nodeNameToContain
	 * @param attributeNames
	 * @param attributeValues
	 * @param fileName
	 */
	protected static void assertVisualEditorContains(SWTBotWebBrowser webBrowser, String nodeNameToContain,
			String[] attributeNames, String[] attributeValues, String fileName) {

		assertTrue(
				"Visual Representation of file " + fileName + " has to contain " + nodeNameToContain
						+ " node but it doesn't",
				webBrowser.containsNodeWithNameAndAttributes(webBrowser.getDomDocument(), nodeNameToContain,
						attributeNames, attributeValues));

	}

	/**
	 * Asserts if Visual Editor contains node nodeNameToContain exactly
	 * numOccurrencies times
	 * 
	 * @param webBrowser
	 * @param nodeNameToContain
	 * @param numOccurrences
	 * @param fileName
	 */
	protected static void assertVisualEditorContainsManyNodes(SWTBotWebBrowser webBrowser, String nodeNameToContain,
			int numOccurrences, String fileName) {

		assertTrue(
				"Visual Representation of file " + fileName + " has to contain " + nodeNameToContain + " node "
						+ (numOccurrences) + " times but it doesn't",
				webBrowser.getDomNodeOccurenciesByTagName(nodeNameToContain) == numOccurrences);

	}

	/**
	 * Asserts if Visual Editor contains node with value valueToContain
	 * 
	 * @param webBrowser
	 * @param valueToContain
	 * @param fileName
	 */
	protected static void assertVisualEditorContainsNodeWithValue(SWTBotWebBrowser webBrowser, String valueToContain,
			String fileName) {

		assertTrue(
				"Visual Representation of file " + fileName + " has to contain node with " + valueToContain
						+ " value but it doesn't",
				webBrowser.containsNodeWithValue(webBrowser.getDomDocument(), valueToContain));

	}

	/**
	 * Asserts if Visual Editor doesn't contain node with particular attributes
	 * 
	 * @param webBrowser
	 * @param valueToContain
	 * @param fileName
	 */
	protected static void assertVisualEditorNotContainNodeWithValue(SWTBotWebBrowser webBrowser, String valueToContain,
			String fileName) {

		assertFalse(
				"Visual Representation of file " + fileName + " cannot contain node with " + valueToContain
						+ " value but it does",
				webBrowser.containsNodeWithValue(webBrowser.getDomDocument(), valueToContain));

	}

	/**
	 * Asserts if Visual Editor doesn't contain node with particular attributes
	 * 
	 * @param webBrowser
	 * @param nodeNameToContain
	 * @param attributeNames
	 * @param attributeValues
	 * @param fileName
	 */
	protected static void assertVisualEditorNotContain(SWTBotWebBrowser webBrowser, String nodeNameToContain,
			String[] attributeNames, String[] attributeValues, String fileName) {

		assertFalse(
				"Visual Representation of file " + fileName + " cannot contain " + nodeNameToContain
						+ " node but it does",
				webBrowser.containsNodeWithNameAndAttributes(webBrowser.getDomDocument(), nodeNameToContain,
						attributeNames, attributeValues));

	}

	/**
	 * Asserts if Visual Editor contains node nodeNameToContain exactly
	 * numOccurrencies times
	 * 
	 * @param webBrowser
	 * @param numOccurrences
	 * @param fileName
	 */
	protected static void assertVisualEditorContainsManyComments(SWTBotWebBrowser webBrowser, int numOccurrences,
			String fileName) {

		assertTrue("Visual Representation of file " + fileName + " has to contain " + numOccurrences
				+ " comment nodes but it doesn't", webBrowser.getCommentNodes().size() == numOccurrences);

	}

	/**
	 * Asserts if Visual Editor contains node nodeNameToContain exactly
	 * numOccurrencies times
	 * 
	 * @param webBrowser
	 * @param numOccurrences
	 * @param fileName
	 */
	protected static void assertVisualEditorContainsManyComments(Browser webBrowser, int numOccurrences,
			String fileName) {

		String browserContent = (String) webBrowser.evaluate("return document.documentElement.innerHTML");
		int numComments = 0;
		int lastIndex = 0;

		while (lastIndex != -1) {

			lastIndex = browserContent.indexOf("<!--", lastIndex);

			if (lastIndex != -1) {
				numComments++;
				lastIndex += "<!--".length();
			}
		}

		assertTrue("Visual Representation of file " + fileName + " has to contain " + numOccurrences
				+ " comment nodes but it doesn't", numComments == numOccurrences);

	}

	/**
	 * Asserts if Visual Editor contains comment with comment value
	 * 
	 * @param webBrowser
	 * @param commentValue
	 */
	protected void assertVisualEditorContainsCommentWithValue(Browser webBrowser, String commentValue) {

		boolean commentFound = false;

		String browserContent = (String) webBrowser.evaluate("return document.documentElement.innerHTML");
		int lastIndex = 0;

		while (lastIndex != -1 && !commentFound) {
			lastIndex = browserContent.indexOf("<!--", lastIndex);
			if (lastIndex != -1) {
				lastIndex += "<!--".length();
				int closingCommentIndex = browserContent.indexOf("-->", lastIndex);
				if (closingCommentIndex != -1) {
					String currentCommentValue = browserContent.substring(lastIndex, closingCommentIndex).trim();
					if (currentCommentValue.equals(commentValue)) {
						commentFound = true;
					}
					lastIndex = closingCommentIndex + "-->".length();
				} else {
					lastIndex = -1;
				}
			}
		}

		assertTrue("Visual Representation of page doesn't contain comment with value " + commentValue, commentFound);
	}
}
