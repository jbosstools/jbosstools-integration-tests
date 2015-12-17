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

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardXHTMLTemplatePage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test XHTML page Creation and Saving
 * 
 * @author Vladimir Pakan
 *
 */
public class XHTMLPageCreationTest extends VPEEditorTestCase {

	public static final String TEST_NEW_XHTML_FILE_NAME = "TestXHTML.xhtml";
	private static final String SAVE_COMMENT = "<!-- Save This -->\n";
	private static final String DO_NOT_SAVE_COMMENT = "<!-- Do not Save This -->\n";

	@Test
	public void testXHTMLPageCreation() throws Throwable {
		checkXHTMLPageCreation();
	}

	/**
	 * Test XHTML page Creation and Saving
	 */
	private void checkXHTMLPageCreation() {
		EditorHandler.getInstance().closeAll(false);
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages").select();
		NewXHTMLWizard newXHTMLWizard = new NewXHTMLWizard();
		newXHTMLWizard.open();
		// create new XHTML file
		NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage();
		newXHTMLFileWizardPage.selectParentFolder(JBT_TEST_PROJECT_NAME, "WebContent", "pages");
		newXHTMLFileWizardPage.setFileName(TEST_NEW_XHTML_FILE_NAME);
		newXHTMLWizard.next();
		NewXHTMLFileWizardXHTMLTemplatePage newXHTMLFileWizardXHTMLTemplatePage = new NewXHTMLFileWizardXHTMLTemplatePage();
		newXHTMLFileWizardXHTMLTemplatePage.setUseXHTMLTemplate(true);
		newXHTMLFileWizardXHTMLTemplatePage.setTemplate("Form Facelet Page");
		newXHTMLWizard.finish();
		ProjectItem piXhtmlTestPage = packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages",TEST_NEW_XHTML_FILE_NAME);
		piXhtmlTestPage.select();
		String checkResult = CheckFileChangesSaving.checkIt(new TextEditor(TEST_NEW_XHTML_FILE_NAME),piXhtmlTestPage, SAVE_COMMENT,true);
		assertNull(checkResult, checkResult);

		checkResult = CheckFileChangesSaving.checkIt(new TextEditor(TEST_NEW_XHTML_FILE_NAME),piXhtmlTestPage, DO_NOT_SAVE_COMMENT, false);
		assertNull(checkResult, checkResult);

		new TextEditor(TEST_NEW_XHTML_FILE_NAME).close();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages",TEST_NEW_XHTML_FILE_NAME).delete();		

	}
}