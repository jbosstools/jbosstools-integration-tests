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
package org.jboss.tools.vpe.ui.bot.test.editor.tags;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests JSF Tags behavior
 * 
 * @author vlado pakan
 *
 */
public class JSFTagsTest extends VPEEditorTestCase {

	private static final String TEST_PAGE_NAME = "JSFTagsTest.jsp";

	private TextEditor jspEditor;
	private SWTBotWebBrowser webBrowser;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(JSFTagsTest.TEST_PAGE_NAME);
		openPage(JSFTagsTest.TEST_PAGE_NAME);
		jspEditor = new TextEditor(JSFTagsTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(JSFTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests h:commandLink Tag
	 */
	@Test
	public void testCommandLinkTag() {

		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:form id=\"form1\">\n"
				+ "        <h:commandLink value=\"Command Link\"/>\n" + "      </h:form>\n" + "    </f:view>\n"
				+ "  </body>\n" + "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "A", new String[] { "title" },
				new String[] { "h:commandLink value: Command Link" }, JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, "Command Link", JSFTagsTest.TEST_PAGE_NAME);
		// move h:commandLink from h:form tag and check correct behavior
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <h:commandLink value=\"Command Link\"/>\n" + "  </body>\n"
				+ "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorNotContain(webBrowser, "A", null, null, JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, "Command Link", JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser,
				": This link is disabled as it is not nested within a JSF form.", JSFTagsTest.TEST_PAGE_NAME);

	}

	@Override
	public void tearDown() throws Exception {
		jspEditor.close();
		super.tearDown();
	}

	/**
	 * Tests h:inputText Tag
	 */
	@Test
	public void testInputTextTag() {

		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:inputText/>\n" + "    </f:view>\n"
				+ "  </body>\n" + "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "title" }, new String[] { "h:inputText" },
				JSFTagsTest.TEST_PAGE_NAME);
		// check tag selection
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String selectedText = jspEditor.getSelectedText();
		final String hasToStartWith = "<h:inputText";
		assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" + "\nbut it is '"
				+ selectedText + "'", selectedText.trim().startsWith(hasToStartWith));
		// check text insertion
		webBrowser.setFocus();
		final String insertText = "insertText";
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "title", "value" },
				new String[] { "h:inputText value: " + insertText, insertText }, JSFTagsTest.TEST_PAGE_NAME);
		Assertions.assertSourceEditorContains(jspEditor.getText(), "<h:inputText value=\"" + insertText + "\"",
				JSFTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests h:inputTextArea Tag
	 */
	@Test
	public void testInputTextAreaTag() {

		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:inputTextarea/>\n" + "    </f:view>\n"
				+ "  </body>\n" + "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "TEXTAREA", new String[] { "title" }, new String[] { "h:inputTextarea" },
				JSFTagsTest.TEST_PAGE_NAME);
		// check tag selection
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("TEXTAREA"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String selectedText = jspEditor.getSelectedText();
		final String hasToStartWith = "<h:inputTextarea";
		assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" + "\nbut it is '"
				+ selectedText + "'", selectedText.trim().startsWith(hasToStartWith));
		// check text insertion
		webBrowser.setFocus();
		final String insertText = "insertText";
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "TEXTAREA", new String[] { "title" },
				new String[] { "h:inputTextarea value: " + insertText }, JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, insertText, JSFTagsTest.TEST_PAGE_NAME);
		Assertions.assertSourceEditorContains(jspEditor.getText(), "<h:inputTextarea value=\"" + insertText + "\"",
				JSFTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests h:outputText Tag
	 */
	@Test
	public void testOutputTextTag() {

		final String outputText = "Output Text";
		final String normalText = "Normal Text";
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:form id=\"form1\">\n"
				+ "        <h:outputText value=\"" + outputText + "\"/>" + normalText + "\n" + "      </h:form>\n"
				+ "    </f:view>\n" + "  </body>\n" + "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContainsNodeWithValue(webBrowser, outputText, JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, normalText, JSFTagsTest.TEST_PAGE_NAME);
		// check editing via Visual Pane
		jspEditor.setCursorPosition(jspEditor.getPositionOfText(outputText));
		webBrowser.setFocus();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		final String insertText = "inserted";
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		Assertions.assertSourceEditorContains(jspEditor.getText(),
				"<h:outputText value=\"" + insertText + outputText + "\"", JSFTagsTest.TEST_PAGE_NAME);
		for (int index = 0 ; index < outputText.length() + normalText.length() ; index++){
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		}
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		Assertions.assertSourceEditorContains(jspEditor.getText(), normalText + insertText, JSFTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests h:selectManyCheckbox Tag
	 */
	@Test
	public void testSelectManyCheckbox() {
		final String itemLabel = "item1";
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:form id=\"form1\">\n"
				+ "        <h:selectManyCheckbox value=\"checkbox\">\n" + "          <f:selectItem itemLabel=\""
				+ itemLabel + "\"/>\n" + "        </h:selectManyCheckbox>\n" + "      </h:form>\n" + "    </f:view>\n"
				+ "  </body>\n" + "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "type" }, new String[] { "checkbox" },
				JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, itemLabel, JSFTagsTest.TEST_PAGE_NAME);
		// check tag selection
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String selectedText = jspEditor.getSelectedText();
		final String hasToStartWith = "<f:selectItem itemLabel=\"" + itemLabel + "\"";
		assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" + "\nbut it is '"
				+ selectedText + "'", selectedText.trim().startsWith(hasToStartWith));
		// check text insertion
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("LABEL"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		webBrowser.setFocus();
		final String insertText = "insertText";
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContainsNodeWithValue(webBrowser, insertText + itemLabel, JSFTagsTest.TEST_PAGE_NAME);
		Assertions.assertSourceEditorContains(jspEditor.getText(),
				"<f:selectItem itemLabel=\"" + insertText + itemLabel + "\"", JSFTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests h:selectOneRadio Tag
	 */
	@Test
	public void testSelectOneRadio() {
		final String itemLabel = "item1";
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>\n" + "<html>\n" + "  <head>\n"
				+ "  </head>\n" + "  <body>\n" + "    <f:view>\n" + "      <h:form id=\"form1\">\n"
				+ "        <h:selectOneRadio value=\"radio\">\n" + "          <f:selectItem itemLabel=\"" + itemLabel
				+ "\"/>\n" + "        </h:selectOneRadio>\n" + "      </h:form>\n" + "    </f:view>\n" + "  </body>\n"
				+ "</html>");
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "INPUT", new String[] { "type" }, new String[] { "radio" },
				JSFTagsTest.TEST_PAGE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser, itemLabel, JSFTagsTest.TEST_PAGE_NAME);
		// check tag selection
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("INPUT"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		String selectedText = jspEditor.getSelectedText();
		final String hasToStartWith = "<f:selectItem itemLabel=\"" + itemLabel + "\"";
		assertTrue("Selected text in Source Pane has to start with '" + hasToStartWith + "'" + "\nbut it is '"
				+ selectedText + "'", selectedText.trim().startsWith(hasToStartWith));
		// check text insertion
		webBrowser.selectDomNode(webBrowser.getDomNodeByTagName("LABEL"), 0);
		AbstractWait.sleep(TimePeriod.getCustom(3));
		webBrowser.setFocus();
		final String insertText = "insertText";
		KeyboardFactory.getKeyboard().type(insertText);
		jspEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContainsNodeWithValue(webBrowser, insertText + itemLabel, JSFTagsTest.TEST_PAGE_NAME);
		Assertions.assertSourceEditorContains(jspEditor.getText(),
				"<f:selectItem itemLabel=\"" + insertText + itemLabel + "\"", JSFTagsTest.TEST_PAGE_NAME);
	}
}
