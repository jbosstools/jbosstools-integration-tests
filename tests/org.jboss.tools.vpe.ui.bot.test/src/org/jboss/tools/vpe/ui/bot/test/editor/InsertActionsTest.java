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

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;

/**
 * Tests JSP file Insert Actions
 * 
 * @author vlado pakan
 *
 */
public class InsertActionsTest extends VPEEditorTestCase {
	
	private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
			+ "<html>\n" + "  <body>\n" + "  plain text inserted\n" + "    <h:outputText value=\"Studio\" />\n"
			+ "    <h:inputText/>\n" + "  </body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "InsertActionsTest.jsp";

	private TextEditor jspTextEditor;
	private SWTBotWebBrowser webBrowser;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(InsertActionsTest.TEST_PAGE_NAME);
		jspTextEditor = new TextEditor(InsertActionsTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(InsertActionsTest.TEST_PAGE_NAME);

	}

	/**
	 * Insert Tag After Selected Tag
	 */
	@Test
	public void testInsertTagAfter() {

		nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT, "INPUT");

		webBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL, SWTBotWebBrowser.JSF_MENU_LABEL,
				SWTBotWebBrowser.HTML_MENU_LABEL, SWTBotWebBrowser.H_COMMAND_BUTTON_TAG_MENU_LABEL);

		jspTextEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// Check if tag <h:commandButton> was properly added.
		Assertions.assertSourceEditorContains(VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText()),
				"<h:inputText/><h:commandButton>", InsertActionsTest.TEST_PAGE);
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "type", "title" },
				new String[] { "submit", "h:commandButton" }, InsertActionsTest.TEST_PAGE);
		assertProbelmsViewNoErrorsForPage(InsertActionsTest.TEST_PAGE);

	}

	/**
	 * Insert Tag Before Selected Tag
	 */
	@Test
	public void testInsertTagBefore() {

		nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT, "INPUT");

		webBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL, SWTBotWebBrowser.JSF_MENU_LABEL,
				SWTBotWebBrowser.HTML_MENU_LABEL, SWTBotWebBrowser.H_COMMAND_BUTTON_TAG_MENU_LABEL);

		jspTextEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// Check if tag <h:commandButton> was properly added
		Assertions.assertSourceEditorContains(VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText()),
				"<h:commandButton></h:commandButton><h:inputText/>", InsertActionsTest.TEST_PAGE);
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "type", "title" },
				new String[] { "submit", "h:commandButton" }, InsertActionsTest.TEST_PAGE);
		assertProbelmsViewNoErrorsForPage(InsertActionsTest.TEST_PAGE);

	}

	/**
	 * Insert Tag Into Selected Tag
	 */
	@Test
	public void testInsertTagInto() {

		nsIDOMNode node = initJspPageBeforeInserting(
				"<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" + "<html>\n" + "  <body>\n"
						+ "  plain text inserted\n" + "    <form>\n" + "    </form>\n" + "  </body>\n" + "</html>",
				"FORM");

		webBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_INTO_MENU_LABEL, SWTBotWebBrowser.JSF_MENU_LABEL,
				SWTBotWebBrowser.HTML_MENU_LABEL, SWTBotWebBrowser.H_COMMAND_BUTTON_TAG_MENU_LABEL);

		jspTextEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// Check if tag <h:commandButton> was properly added
		Assertions.assertSourceEditorContains(VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText()),
				"<form><h:commandButton></h:commandButton></form>", InsertActionsTest.TEST_PAGE);
		assertVisualEditorContains(webBrowser, "FORM", null, null, InsertActionsTest.TEST_PAGE);
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "type", "title" },
				new String[] { "submit", "h:commandButton" }, InsertActionsTest.TEST_PAGE);
		assertProbelmsViewNoErrorsForPage(InsertActionsTest.TEST_PAGE);

	}

	/**
	 * Insert Tag Around Selected Tag
	 */
	@Test
	public void testInsertTagAround() {

		nsIDOMNode node = initJspPageBeforeInserting(InsertActionsTest.PAGE_TEXT, "INPUT");

		webBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL, SWTBotWebBrowser.JSF_MENU_LABEL,
				SWTBotWebBrowser.HTML_MENU_LABEL, SWTBotWebBrowser.H_FORM_TAG_MENU_LABEL);

		jspTextEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		// Check if tag <h:form> was properly added
		Assertions.assertSourceEditorContains(VPEEditorTestCase.stripHTMLSourceText(jspTextEditor.getText()),
				"<h:form><h:inputText/></h:form>", InsertActionsTest.TEST_PAGE);
		assertVisualEditorContains(webBrowser, "FORM", null, null, InsertActionsTest.TEST_PAGE);
		assertProbelmsViewNoErrorsForPage(InsertActionsTest.TEST_PAGE);

	}

	/**
	 * Inits JSP Page before Tag will be inserted
	 */
	private nsIDOMNode initJspPageBeforeInserting(String pageText, String nodeText) {

		jspTextEditor.setText(pageText);
		jspTextEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		nsIDOMNode node = webBrowser.getDomNodeByTagName(nodeText, 0);
		webBrowser.selectDomNode(node, 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));

		return node;

	}

	@Override
	public void tearDown() throws Exception {
		jspTextEditor.close();
		super.tearDown();
	}

}
