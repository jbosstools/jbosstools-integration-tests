/*******************************************************************************

 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Tests multiple selection in source pane of VPE and her proper visual
 * representation within visual pane of VPE
 * 
 * @author vlado pakan
 *
 */
public class MultiSelectionTest extends VPEEditorTestCase {

	private static final String OUTPUT_TEXT_0_TEXT = "!@#_OUTPUT-TEXT_0_#@!";
	private static final String OUTPUT_TEXT_1_TEXT = "!@#_OUTPUT-TEXT_1_#@!";

	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
			+ "<html>\n" + "  <body>\n" + "<h:outputText value=\"" + MultiSelectionTest.OUTPUT_TEXT_0_TEXT + "\"/>"
			+ "<h:outputText value=\"" + MultiSelectionTest.OUTPUT_TEXT_1_TEXT + "\"/>\n" + "  </body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "MultiSelectionTest.jsp";

	private TextEditor jspEditor;
	private SWTBotWebBrowser webBrowser;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(MultiSelectionTest.TEST_PAGE_NAME);
		jspEditor = new TextEditor(MultiSelectionTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(MultiSelectionTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests Multiple Selection
	 */
	@Test
	public void testMultipleSeletion() {

		jspEditor.activate();
		jspEditor.setText(MultiSelectionTest.PAGE_TEXT);
		jspEditor.save();
		jspEditor.selectText(MultiSelectionTest.OUTPUT_TEXT_1_TEXT);

		assertSelectedNodeHasText(webBrowser, MultiSelectionTest.OUTPUT_TEXT_1_TEXT);

		jspEditor.selectText(MultiSelectionTest.OUTPUT_TEXT_0_TEXT);

		assertSelectedNodeHasText(webBrowser, MultiSelectionTest.OUTPUT_TEXT_0_TEXT);

		jspEditor.selectText("<h:outputText value=\"!@#_OUTPUT-TEXT_0_#@!\"/><h:outputText value=\"!@#_OUTPUT-TEXT_1_#@!\"/>");
		
		assertTrue("Multiple selection doesn't contain proper nodes.", webBrowser.selectionContainsNodes(false,
				MultiSelectionTest.OUTPUT_TEXT_0_TEXT, MultiSelectionTest.OUTPUT_TEXT_1_TEXT));
		
	}

	@Override
	public void tearDown() throws Exception {
		jspEditor.close();
		super.tearDown();
	}

	/**
	 * Asserts selected node in Visual pane has text textToHave
	 * 
	 * @param webBrowser
	 * @param nodeNameToContain
	 */
	private static void assertSelectedNodeHasText(SWTBotWebBrowser webBrowser, String textToHave) {

		nsIDOMNode selectedNode = webBrowser.getSelectedDomNode();

		assertNotNull("There is no selected node within Visual pane of VPE", selectedNode);
		assertNotNull("Selected node within Visual pane of VPE has no value", selectedNode.getNodeValue());

		final String nodeValue = selectedNode.getNodeValue();

		assertTrue(
				"Selected node within Visual pane has value:\n" + nodeValue + "\nbut has to have value:\n" + textToHave,
				textToHave.equals(nodeValue));

	}
}
