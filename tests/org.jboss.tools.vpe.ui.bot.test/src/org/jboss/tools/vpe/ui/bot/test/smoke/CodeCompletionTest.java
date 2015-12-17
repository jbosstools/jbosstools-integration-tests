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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.helper.ContentAssistHelper;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test Code Completion functionality
 * 
 * @author Vladimir Pakan
 *
 */
public class CodeCompletionTest extends VPEEditorTestCase {
	static final private String HTML_PAGE_NAME = "CodeComletionPage.html";
	static final private String XHTML_PAGE_NAME = "CodeComletionPage.xhtml";
	private TextEditor jspEditor;
	private TextEditor htmlEditor;
	private TextEditor xhtmlEditor;
	private String originalJspEditorText;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		openPage(TEST_PAGE);
		jspEditor = new TextEditor(TEST_PAGE);
		originalJspEditorText = jspEditor.getText();
	}

	@Override
	public void tearDown() throws Exception {
		if (jspEditor != null) {
			jspEditor.setText(originalJspEditorText);
			jspEditor.save();
			jspEditor.close();
		}
		if (htmlEditor != null) {
			htmlEditor.save();
			htmlEditor.close();
		}
		if (xhtmlEditor != null) {
			xhtmlEditor.save();
			xhtmlEditor.close();
		}
		super.tearDown();
	}

	/**
	 * Tests Code Completion for JSP Page within <f:view> tag
	 */
	@Test
	public void testCodeCompletionOfJspPage() {
		String textForSelection = "<f:view>";
		int textForSelectionPos = jspEditor.getPositionOfText(textForSelection);
		jspEditor.setCursorPosition(textForSelectionPos + textForSelection.length());
		// Check content assist menu content
		ContentAssistHelper.checkContentAssistContent(jspEditor, jspEditor.getCursorPosition().x,
				jspEditor.getCursorPosition().y, CodeCompletionTest.getJspPageProposalList(), false);
		// Check content assist insertion
		String contentAssistToUse = "h:commandButton";
		ContentAssistHelper.assertContentAssistantContains(jspEditor.openContentAssistant(), contentAssistToUse, true);
		jspEditor.save();
		String expectedInsertedText = textForSelection + "<" + contentAssistToUse + " action=\"\" value=\"\" />";
		if (!jspEditor.getText().contains(expectedInsertedText)) {
			expectedInsertedText = textForSelection + "<" + contentAssistToUse + " value=\"\" action=\"\" />";
		}
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ jspEditor.getText(), jspEditor.getText().contains(expectedInsertedText));
		// close automatically opened code assist shell
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		// Check content assist for action attribute of <h:commandButton> tag
		jspEditor.setCursorPosition(textForSelectionPos + expectedInsertedText.indexOf("action=\"") + 8);
		ContentAssistHelper.checkContentAssistContent(jspEditor, jspEditor.getCursorPosition().x,
				jspEditor.getCursorPosition().y, CodeCompletionTest.getCommandButtonActionAttrProposalList(), false);
		// Check content assist for value attribute of <h:commandButton> tag
		jspEditor.setCursorPosition(textForSelectionPos + expectedInsertedText.indexOf("value=\"") + 7);
		ContentAssistHelper.checkContentAssistContent(jspEditor, jspEditor.getCursorPosition().x,
				jspEditor.getCursorPosition().y, CodeCompletionTest.getCommandButtonValueAttrProposalList(), false);
	}

	/**
	 * Tests Code Completion for HTML Page
	 */
	@Test
	public void testCodeCompletionOfHtmlPage() {
		createHtmlPage(CodeCompletionTest.HTML_PAGE_NAME);
		htmlEditor = new TextEditor(CodeCompletionTest.HTML_PAGE_NAME);
		htmlEditor.setText("<!DOCTYPE html>\n" + "<html>\n" + "  <head>\n" + "  </head>\n" + "  <body>\n"
				+ "  </body>\n" + "</html>");
		final String tagToSelect = "<body>";
		htmlEditor.setCursorPosition(htmlEditor.getPositionOfText(tagToSelect) + tagToSelect.length());
		ContentAssistant contentAssistant = htmlEditor.openContentAssistant();
		String missingProposalsString = CodeCompletionTest.getMisingProposalsString(contentAssistant.getProposals(),
				CodeCompletionTest.getHTML5ProposalList());
		contentAssistant.close();
		assertTrue(
				"There are missing Code Assist proposals for these HTML 5 tags: " + missingProposalsString.toString(),
				missingProposalsString.length() == 0);
	}

	/**
	 * Tests Code Completion for RichFaces tags
	 */
	@Test
	public void testCodeCompletionOfRichFacesTags() {
		createXhtmlPage(CodeCompletionTest.XHTML_PAGE_NAME);
		xhtmlEditor = new TextEditor(CodeCompletionTest.XHTML_PAGE_NAME);
		xhtmlEditor.setText(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
						+ "<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"\n"
						+ "  xmlns:h=\"http://java.sun.com/jsf/html\"\n"
						+ "  xmlns:f=\"http://java.sun.com/jsf/core\"\n"
						+ "  xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n"
						+ "  xmlns:a4j=\"http://richfaces.org/a4j\"\n" + "  xmlns:rich=\"http://richfaces.org/rich\">\n"
						+ "  <rich:\n" + "</ui:composition>");
		xhtmlEditor.save();
		final String tagToSelect = "<rich:";
		xhtmlEditor.setCursorPosition(xhtmlEditor.getPositionOfText(tagToSelect) + tagToSelect.length());
		ContentAssistant contentAssistant = xhtmlEditor.openContentAssistant();
		String missingProposalsString = CodeCompletionTest.getMisingProposalsString(contentAssistant.getProposals(),
				CodeCompletionTest.getRichFacesProposalList());
		contentAssistant.close();
		assertTrue(
				"There are missing Code Assist proposals for these HTML 5 tags: " + missingProposalsString.toString(),
				missingProposalsString.length() == 0);
	}

	/**
	 * Tests Code Completion for AngularJS tags
	 */
	@Test
	public void testCodeCompletionOfAngularJSTags() {
		createHtmlPage(CodeCompletionTest.HTML_PAGE_NAME);
		htmlEditor = new TextEditor(CodeCompletionTest.HTML_PAGE_NAME);
		htmlEditor.setText("<!DOCTYPE html>\n" + "<html>\n" + "  <head>\n" + "  </head>\n" + "  <body ng->\n"
				+ "    <input type=\"checkbox\" ng-model=\"checked\" class=\"\"></input>\n" + "  </body>\n"
				+ "</html>");
		htmlEditor.save();
		final String tagToSelect = "<body ng-";
		htmlEditor.setCursorPosition(htmlEditor.getPositionOfText(tagToSelect) + tagToSelect.length());
		ContentAssistant contentAssistant = htmlEditor.openContentAssistant();
		String missingProposalsString = CodeCompletionTest.getMisingProposalsString(contentAssistant.getProposals(),
				CodeCompletionTest.getAngularJSAttributesProposalList());
		contentAssistant.close();
		assertTrue("There are missing Code Assist proposals for these AngularJS attributes: " + missingProposalsString,
				missingProposalsString.length() == 0);
		// Check content assist insertion
		htmlEditor.activate();
		String contentAssistToUse = "ng-bind";
		ContentAssistHelper.assertContentAssistantContains(htmlEditor.openContentAssistant(),
				contentAssistToUse + " - ng", true);
		// close automatically opened code assist shell
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		htmlEditor.save();
		String expectedInsertedText = "<body " + contentAssistToUse + "=\"\">";
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ htmlEditor.getText(), htmlEditor.getText().contains(expectedInsertedText));
		contentAssistant = htmlEditor.openContentAssistant();
		// check ng-bing attribute value proposal list
		missingProposalsString = CodeCompletionTest.getMisingProposalsString(contentAssistant.getProposals(),
				CodeCompletionTest.getNgBindAttrValueProposalList());
		assertTrue("There are missing Code Assist proposals for class attribute: " + missingProposalsString,
				missingProposalsString.length() == 0);
		contentAssistant.close();
		htmlEditor.setCursorPosition(htmlEditor.getPositionOfText("class=\"\"") + 7);
		contentAssistant = htmlEditor.openContentAssistant();
		missingProposalsString = CodeCompletionTest.getMisingProposalsString(contentAssistant.getProposals(),
				CodeCompletionTest.getAngularJSClassAttributeValueProposalList());
		contentAssistant.close();
		assertTrue("There are missing Code Assist proposals for class attribute: " + missingProposalsString,
				missingProposalsString.length() == 0);
	}

	/**
	 * Returns list of expected Content Assist proposals for jsp page within
	 * <f:view> tag
	 * 
	 * @return
	 */
	private static List<String> getJspPageProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("New JSF EL Expression - Create a new attribute value with #{}");
		result.add("f:actionListener");
		result.add("f:attribute");
		result.add("f:convertDateTime");
		result.add("f:convertNumber");
		result.add("f:converter");
		result.add("f:facet");
		result.add("f:loadBundle");
		result.add("f:param");
		result.add("f:phaseListener");
		result.add("f:selectItem");
		result.add("f:selectItems");
		result.add("f:setPropertyActionListener");
		result.add("f:subview");
		result.add("f:validateDoubleRange");
		result.add("f:validateLength");
		result.add("f:validateLongRange");
		result.add("f:validator");
		result.add("f:valueChangeListener");
		result.add("f:verbatim");
		result.add("f:view");
		result.add("h:column");
		result.add("h:commandButton");
		result.add("h:commandLink");
		result.add("h:dataTable");
		result.add("h:form");
		result.add("h:graphicImage");
		result.add("h:inputHidden");
		result.add("h:inputSecret");
		result.add("h:inputText");
		result.add("h:inputTextarea");
		result.add("h:message");
		result.add("h:messages");
		result.add("h:outputFormat");
		result.add("h:outputLabel");
		result.add("h:outputLink");
		result.add("h:outputText");
		result.add("h:panelGrid");
		result.add("h:panelGroup");
		result.add("h:selectBooleanCheckbox");
		result.add("h:selectManyCheckbox");
		result.add("h:selectManyListbox");
		result.add("h:selectManyMenu");
		result.add("h:selectOneListbox");
		result.add("h:selectOneMenu");
		result.add("h:selectOneRadio");
		result.add("Message");
		result.add("user : User");
		result.add("jsp:attribute");
		result.add("jsp:body");
		result.add("jsp:element");
		result.add("jsp:fallback");
		result.add("jsp:forward");
		result.add("jsp:getProperty");
		result.add("jsp:include");
		result.add("jsp:output");
		result.add("jsp:param");
		result.add("jsp:params");
		result.add("jsp:plugin");
		result.add("jsp:setProperty");
		result.add("jsp:useBean");
		result.add("Facelets ui:Composition - Composition with one definition");
		result.add("JSP declaration(s) - JSP declaration(s) <%!..%>");
		result.add("Tag attribute directive - Tag attribute directive");
		result.add("comment - comment");
		result.add("dl - definition list");
		result.add("img - img     (map)");
		result.add("ol - ordered list");
		result.add("script - script     (commented)");
		result.add("style - style     (commented)");
		result.add("table - table");
		result.add("ul - unordered list");

		return result;
	}

	/**
	 * Returns list of expected Content Assist proposals for action attribute of
	 * <h:commandButton> tag
	 * 
	 * @return
	 */
	private static List<String> getCommandButtonActionAttrProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("hello");
		result.add("Message");
		result.add("user : User");
		result.add("user : User");

		return result;
	}

	/**
	 * Returns list of expected Content Assist proposals for value attribute of
	 * <h:commandButton> tag
	 * 
	 * @return
	 */
	private static List<String> getCommandButtonValueAttrProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("Message");
		result.add("user : User");

		return result;
	}

	/**
	 * Returns list of HTML 5 tags which should be in Code Assist proposals
	 * 
	 * @return
	 */
	private static List<String> getHTML5ProposalList() {
		List<String> result = new LinkedList<String>();
		result.add("article");
		result.add("aside");
		result.add("audio");
		result.add("command");
		result.add("canvas");
		result.add("details");
		result.add("hgroup");
		result.add("meter");
		result.add("progress");
		result.add("time");
		result.add("wbr");
		result.add("embed");
		result.add("datalist");
		result.add("keygen");
		result.add("output");
		result.add("figure");
		result.add("footer");
		result.add("header");
		result.add("mark");
		result.add("nav");
		result.add("ruby");
		result.add("section");
		result.add("video");
		return result;
	}

	/**
	 * Returns list of richFaces tags which should be in Code Assist proposals
	 * 
	 * @return
	 */
	private static List<String> getRichFacesProposalList() {
		List<String> result = new LinkedList<String>();
		result.add("rich:calendar");
		result.add("rich:tree");
		result.add("rich:inplaceSelect");
		result.add("rich:select");
		return result;
	}

	/**
	 * Returns list of AngularJS attributes which should be in Code Assist
	 * proposals
	 * 
	 * @return
	 */
	private static List<String> getAngularJSAttributesProposalList() {
		List<String> result = new LinkedList<String>();
		result.add("ng-app - ng");
		result.add("ng-bind - ng");
		result.add("ng-bind-html - ng");
		result.add("ng-bind-template - ng");
		result.add("ng-class - ng");
		result.add("ng-class-even - ng");
		result.add("ng-class-odd - ng");
		result.add("ng-click - ng");
		result.add("ng-cloak - ng");
		result.add("ng-controller - ng");
		result.add("ng-dblclick - ng");
		result.add("ng-form - ng");
		result.add("ng-hide - ng");
		result.add("ng-if - ng");
		result.add("ng-include - ng");
		result.add("ng-init - ng");
		result.add("ng-keydown - ng");
		result.add("ng-keypress - ng");
		result.add("ng-keyup - ng");
		result.add("ng-mousedown - ng");
		result.add("ng-mouseenter - ng");
		result.add("ng-mouseleave - ng");
		result.add("ng-mousemove - ng");
		result.add("ng-mouseover - ng");
		result.add("ng-mouseup - ng");
		result.add("ng-non-bindable - ng");
		result.add("ng-pluralize - ng");
		result.add("ng-repeat - ng");
		result.add("ng-show - ng");
		result.add("ng-style - ng");
		result.add("ng-switch - ng");
		result.add("ng-transclude - ng");
		return result;
	}

	/**
	 * Returns list of AngularJS class Code Assist proposals
	 * 
	 * @return
	 */
	private static List<String> getAngularJSClassAttributeValueProposalList() {
		List<String> result = new LinkedList<String>();
		result.add("ng-bind: {};");
		result.add("ng-class-even: {};");
		result.add("ng-class-odd: {};");
		result.add("ng-class: {};");
		result.add("ng-cloak");
		result.add("ng-form");
		result.add("ng-include: {};");
		result.add("ng-init: {};");
		result.add("ng-style: {};");
		result.add("ng-transclude: {};");
		return result;
	}

	/**
	 * Returns list of expected Content Assist proposals for value of attribute
	 * ng-bind
	 * 
	 * @return
	 */
	private static List<String> getNgBindAttrValueProposalList() {
		LinkedList<String> result = new LinkedList<String>();
		result.add("user : User");
		return result;
	}

	private static String getMisingProposalsString(List<String> currentProposals, List<String> expectedProposals) {
		StringBuffer sbMissingProposals = new StringBuffer("");
		for (String expectedItem : expectedProposals) {
			if (!currentProposals.contains(expectedItem)) {
				if (sbMissingProposals.length() != 0) {
					sbMissingProposals.append(",");
				}
				sbMissingProposals.append(expectedItem);
			}
		}
		return sbMissingProposals.toString();
	}
}