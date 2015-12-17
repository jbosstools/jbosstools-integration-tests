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

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Tests JSP file editing and synchronization between Source Editor and Visual
 * Editor
 * 
 * @author vlado pakan
 *
 */
public class JspFileEditingTest extends VPEEditorTestCase {

	public JspFileEditingTest() {
		super();
	}

	@Test
	public void testJspFileEditing() {

		new WorkbenchShell().maximize();

		insertTagUsingContextMenu();
		insertTagUsingPalette();

	}

	/**
	 * Inserts tag to html page using Context Menu of Visual Editor
	 */
	private void insertTagUsingContextMenu() {

		openPage();
		SWTBotWebBrowser swtBotWebBrowser = new SWTBotWebBrowser(TEST_PAGE);
		nsIDOMNode node = swtBotWebBrowser.getDomNodeByTagName("INPUT", 1);
		swtBotWebBrowser.selectDomNode(node, 0);
		swtBotWebBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL, SWTBotWebBrowser.JSF_MENU_LABEL,
				SWTBotWebBrowser.HTML_MENU_LABEL, SWTBotWebBrowser.H_OUTPUT_TEXT_TAG_MENU_LABEL);

		final TextEditor jspTextEditor = new TextEditor(TEST_PAGE);
		jspTextEditor.save();
		// Check if tag h:outputText was properly added
		String editorText = VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText());
		assertTrue("File " + TEST_PAGE + " has to contain string '<h:outputText></h:outputText>' but it doesn't",
				editorText.contains("<h:outputText></h:outputText>"));
	}

	/**
	 * Inserts tag to html page using JBoss Tools Palette
	 */
	private void insertTagUsingPalette() {

		openPage();
		openPalette();

		SWTBotWebBrowser swtBotWebBrowser = new SWTBotWebBrowser(TEST_PAGE);
		nsIDOMNode node = swtBotWebBrowser.getDomNodeByTagName("INPUT", 1);
		swtBotWebBrowser.selectDomNode(node, 0);

		SWTBotWebBrowser.activatePaletteTool("outputText");
		new DefaultShell("Insert Tag");
		String outputTextValue = "123 !! Test value !! 321";
		new DefaultTableItem("value").click();
		KeyboardFactory.getKeyboard().type(outputTextValue);
		new FinishButton().click();
		final TextEditor jspTextEditor = new TextEditor(TEST_PAGE);
		jspTextEditor.save();
		new DefaultToolItem(RunningPlatform.isOSX() ? "Refresh (⌘R)" : "Refresh").click();
		String editorText = jspTextEditor.getText();
		String testText = "<h:outputText value=\"" + outputTextValue + "\">";
		assertTrue("File " + TEST_PAGE + " has to contain string '" + testText + "' but it doesn't",
				editorText.contains(testText));
		// Insert text via Visual Editor to inserted h:outputText tag
		node = swtBotWebBrowser.getDomNodeByTagName(swtBotWebBrowser.getNsIDOMDocument(), "#text", 5);
		swtBotWebBrowser.selectDomNode(node, 5);
		String insertString = "ab9876CD";
		KeyboardFactory.getKeyboard().type(insertString);
		jspTextEditor.save();
		editorText = jspTextEditor.getText();
		outputTextValue = outputTextValue.substring(0, 5) + insertString + outputTextValue.substring(5);
		testText = "<h:outputText value=\"" + outputTextValue + "\">";
		assertTrue("File " + TEST_PAGE + " has to contain string '" + testText + "' but it doesn't",
				editorText.contains(testText));
		jspTextEditor.close();
	}

}