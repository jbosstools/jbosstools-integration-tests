/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.smoke;

import org.jboss.tools.jsf.reddeer.ui.editor.WebXmlEditor;
import org.jboss.tools.jsf.reddeer.ui.editor.WebXmlSourceEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * * Test web.xml file editor functionality
 * 
 * @author Vladimir Pakan
 * 
 */
public class WebXmlEditorTest extends JSFAutoTestCase {

	private static final String WEB_XML_FILE_NAME = "web.xml";
	private static final String SERVLET_NAME = "TestChangeServlet";
	private static final String DISPLAY_NAME = "Test Change Servlet";
	private static final String SERVLET_CLASS = "org.jboss.tests.TestChangeServlet.java";
	private static final String SERVLET_DESCRIPTION = "Dummy Servlet just for testing web.xml editor functionality";
	private static final String URL_PATTERN = "*xhtml";
	private WebXmlSourceEditor webXmlSourceEditor;
	private WebXmlEditor webXmlEditor;
	private String origWebXmlFileContent;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "WEB-INF", WebXmlEditorTest.WEB_XML_FILE_NAME).open();
		webXmlEditor = new WebXmlEditor(WebXmlEditorTest.WEB_XML_FILE_NAME);
		webXmlSourceEditor = webXmlEditor.getWebXmlSourceEditor();
		origWebXmlFileContent = webXmlSourceEditor.getText();
	}

	@Override
	public void tearDown() throws Exception {
		if (webXmlSourceEditor != null) {
			webXmlSourceEditor.setText(origWebXmlFileContent);
			webXmlSourceEditor.save();
			webXmlSourceEditor.close();
		}
		super.tearDown();
	}

	/**
	 * Test web.xml file editor
	 */
	@Test
	public void testWebXmlEditor() {
		// open web.xml file editor
		webXmlEditor.activateTreeTab();
		// check content of web.xml tree select each tree item and expand when
		// possible
		webXmlEditor.selectSessionConfigNode();
		webXmlEditor.selectWelcomeFileListNode();
		webXmlEditor.selectJspConfigNode();
		webXmlEditor.selectLoginConfigNode();
		webXmlEditor.selectLocaleEncodingMappingListNode();
		webXmlEditor.selectServletsNode().expand();
		webXmlEditor.addServlet(WebXmlEditorTest.SERVLET_NAME, WebXmlEditorTest.DISPLAY_NAME,
				WebXmlEditorTest.SERVLET_CLASS, WebXmlEditorTest.SERVLET_DESCRIPTION);
		webXmlEditor.save();
		String editorText = webXmlSourceEditor.getText();
		String textToContain = "<servlet-name>" + WebXmlEditorTest.SERVLET_NAME + "</servlet-name>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
		textToContain = "<display-name>" + WebXmlEditorTest.DISPLAY_NAME + "</display-name>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
		textToContain = "<servlet-class>" + WebXmlEditorTest.SERVLET_CLASS + "</servlet-class>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
		textToContain = "<description>" + WebXmlEditorTest.SERVLET_DESCRIPTION + "</description>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
		// try to add new servlet mapping
		webXmlEditor.addServletMapping(WebXmlEditorTest.SERVLET_NAME, WebXmlEditorTest.URL_PATTERN);
		webXmlEditor.save();
		editorText = webXmlSourceEditor.getText().replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "")
				.replaceAll(" ", "");
		textToContain = "<servlet-mapping><servlet-name>" + WebXmlEditorTest.SERVLET_NAME + "</servlet-name>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
		textToContain = "<url-pattern>" + WebXmlEditorTest.URL_PATTERN + "</url-pattern></servlet-mapping>";
		assertTrue("Web.xml editor has to contain text '" + textToContain + "' but it doesn't.",
				editorText.toLowerCase().contains(textToContain.toLowerCase()));
	}

}