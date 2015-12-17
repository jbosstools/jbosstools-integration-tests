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

import java.security.InvalidParameterException;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.test.JBTSWTBotTestCase;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Abstract Tag Test behavior
 * 
 * @author vlado pakan
 *
 */
public abstract class AbstractTagTest extends VPEEditorTestCase {

	private static final String TEST_PAGE_NAME_HTML = "TagTest.html";
	private static final String TEST_PAGE_NAME_JSP = "TagTest.jsp";
	private static final String TEST_PAGE_NAME_XHTML = "TagTest.xhtml";

	private TextEditor sourceEditor;
	private SWTBotWebBrowser visualEditor;
	private TestPageType testPageType;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		initTestPage();
		savePage();
	}

	/**
	 * 
	 * @param testPageType
	 * @param pageText
	 */
	protected void initTestPage(TestPageType testPageType, String pageText) {
		this.testPageType = testPageType;
		if (testPageType.equals(TestPageType.HTML)) {
			createHtmlPage(AbstractTagTest.TEST_PAGE_NAME_HTML);
			sourceEditor = new TextEditor(AbstractTagTest.TEST_PAGE_NAME_HTML);
			visualEditor = new SWTBotWebBrowser(AbstractTagTest.TEST_PAGE_NAME_HTML);
		} else if (testPageType.equals(TestPageType.JSP)) {
			createJspPage(AbstractTagTest.TEST_PAGE_NAME_JSP);
			sourceEditor = new TextEditor(AbstractTagTest.TEST_PAGE_NAME_JSP);
			visualEditor = new SWTBotWebBrowser(AbstractTagTest.TEST_PAGE_NAME_JSP);
		} else if (testPageType.equals(TestPageType.XHTML)) {
			createXhtmlPage(AbstractTagTest.TEST_PAGE_NAME_XHTML, VPEAutoTestCase.JBT_TEST_PROJECT_NAME, "WebContent",
					"pages");
			sourceEditor = new TextEditor(AbstractTagTest.TEST_PAGE_NAME_XHTML);
			visualEditor = new SWTBotWebBrowser(AbstractTagTest.TEST_PAGE_NAME_XHTML);
		} else {
			throw new InvalidParameterException("Invalid value of testPageType used: " + testPageType);
		}
		sourceEditor.setText(pageText);
	}

	@Override
	public void tearDown() throws Exception {
		sourceEditor.close();
		deleteTestPage();
		super.tearDown();
	}

	/**
	 * Runs Tag Testing
	 */
	@Test
	public void testTag() {
		verifyTag();
	}

	/**
	 * Initialize properly page content
	 */
	protected abstract void initTestPage();

	/**
	 * Verify tag
	 */
	protected abstract void verifyTag();

	/**
	 * Saves Page if it was changed and shows changed editor
	 */
	protected void savePage() {
		sourceEditor.activate();
		sourceEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
	}

	/**
	 * Returns actual Source part of VPE Editor
	 * 
	 * @return
	 */
	protected TextEditor getSourceEditor() {
		return sourceEditor;
	}

	/**
	 * Returns actual Visual part of VPE Editor
	 * 
	 * @return
	 */
	protected SWTBotWebBrowser getVisualEditor() {
		return visualEditor;
	}

	/**
	 * Returns actual Test Page File Name
	 * 
	 * @return
	 */
	protected String getTestPageFileName() {
		String fileName = null;

		if (testPageType.equals(TestPageType.HTML)) {
			fileName = AbstractTagTest.TEST_PAGE_NAME_HTML;
		} else if (testPageType.equals(TestPageType.JSP)) {
			fileName = AbstractTagTest.TEST_PAGE_NAME_JSP;
		} else if (testPageType.equals(TestPageType.XHTML)) {
			fileName = AbstractTagTest.TEST_PAGE_NAME_XHTML;
		} else {
			throw new InvalidParameterException("Invalid value of testPageType used: " + testPageType);
		}

		return fileName;
	}

	public void deleteTestPage() {
		packageExplorer.open();
		if (testPageType.equals(TestPageType.HTML)) {
			packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
					.getProjectItem("WebContent", "pages", getTestPageFileName()).delete();
		} else if (testPageType.equals(TestPageType.JSP)) {
			packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
					.getProjectItem("WebContent", "pages", getTestPageFileName()).delete();
		} else if (testPageType.equals(TestPageType.XHTML)) {
			packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
					.getProjectItem("WebContent", "pages", getTestPageFileName()).delete();
		}
	}

	/**
	 * Asserts if Problems View has no errors for test page
	 * 
	 * @param botExt
	 */
	protected void assertProbelmsViewNoErrorsForPage() {

		JBTSWTBotTestCase.assertProbelmsViewNoErrorsForPage(getTestPageFileName());

	}

}
