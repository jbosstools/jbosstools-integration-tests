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
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardDialog;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPPage;
import org.jboss.tools.jst.reddeer.jsp.ui.wizard.NewJSPFileWizardJSPTemplatePage;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test JSP page Creation and Saving
 * 
 * @author Vladimir Pakan
 *
 */
public class JSPPageCreationTest extends VPEEditorTestCase {

	public static final String TEST_NEW_JSP_FILE_NAME = "TestJSP.jsp";
	private static final String SAVE_COMMENT = "<!-- Save This -->\n";
	private static final String DO_NOT_SAVE_COMMENT = "<!-- Do not Save This -->\n";

	@Test
	public void testEditorJSPPageCreation() {
		checkJSPPageCreation();
	}

	/**
	 * Test JSP page Creation and Saving
	 */
	private void checkJSPPageCreation() {
		EditorHandler.getInstance().closeAll(false);
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages").select();
		// create new JSP file
		NewJSPFileWizardDialog newJSPFileWizardDialog = new NewJSPFileWizardDialog();
		newJSPFileWizardDialog.open();
		NewJSPFileWizardJSPPage newJSPFileWizardJSPPage = new NewJSPFileWizardJSPPage();
		newJSPFileWizardJSPPage.setFileName(TEST_NEW_JSP_FILE_NAME);
		newJSPFileWizardDialog.next();
		new NewJSPFileWizardJSPTemplatePage().setTemplate("New JSP File (html)");
		newJSPFileWizardDialog.finish();
		ProjectItem piJspTestPage = packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",
				"pages", TEST_NEW_JSP_FILE_NAME);
		String checkResult = CheckFileChangesSaving.checkIt(new TextEditor(TEST_NEW_JSP_FILE_NAME), piJspTestPage,
				SAVE_COMMENT, true);
		assertNull(checkResult, checkResult);

		checkResult = CheckFileChangesSaving.checkIt(new TextEditor(TEST_NEW_JSP_FILE_NAME), piJspTestPage,
				DO_NOT_SAVE_COMMENT, false);
		assertNull(checkResult, checkResult);

		new TextEditor(TEST_NEW_JSP_FILE_NAME).close();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_NEW_JSP_FILE_NAME)
				.delete();
	}

}