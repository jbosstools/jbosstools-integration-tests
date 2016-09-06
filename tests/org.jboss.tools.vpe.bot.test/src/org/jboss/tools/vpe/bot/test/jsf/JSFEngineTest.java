/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test.jsf;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.jboss.tools.jst.reddeer.web.ui.editor.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.bot.test.ErrorInLog;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preview.editor.EngineDialog;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSFEngineTest extends VPETestBase {

	private String testPageName = "jstTestPage.xhtml";

	@BeforeClass
	public static void createProject() {
		String errorMessage;
		if(isLinux()){
			errorMessage = "You currently have Visual editor configured to have better HTML5";
		} else {
			errorMessage = "Visual Page Editor has experimental support for Windows 64-bit";
		}
		
		ErrorInLog el = new ErrorInLog(errorMessage,"org.jboss.tools.vpe.preview.core");
		List<ErrorInLog> errors = new ArrayList<ErrorInLog>();
		errors.add(el);
		errors.add(el);
		expectedErrors(errors);
		createWebProject();
	}

	@Test
	public void openXHTMLPageTest() {
		NewXHTMLWizard xw = new NewXHTMLWizard();
		xw.open();
		NewXHTMLFileWizardPage xp = new NewXHTMLFileWizardPage();
		xp.setFileName(testPageName);
		xp.selectParentFolder(PROJECT_NAME);
		xw.finish();
		if (isGTK2()) {

			EngineDialog ed = new EngineDialog();
			assertTrue(ed.isHTML5ButtonEnabled());
			assertTrue(ed.isJSFButtonEnabled());

			ed.stayWithHTML5();
			JSPMultiPageEditor editor = new JSPMultiPageEditor();
			assertEquals(testPageName, editor.getTitle());

			editor.close();

			ProjectExplorer pe = new ProjectExplorer();
			pe.open();
			pe.getProject(PROJECT_NAME).getProjectItem(testPageName).open();

			ed = new EngineDialog();
			assertTrue(ed.isHTML5ButtonEnabled());
			assertTrue(ed.isJSFButtonEnabled());

			ed.stayWithHTML5();
			editor = new JSPMultiPageEditor();
			assertEquals(testPageName, editor.getTitle());

		} else {

			JSPMultiPageEditor editor = new JSPMultiPageEditor();
			assertEquals(testPageName, editor.getTitle());

			editor.close();

			ProjectExplorer pe = new ProjectExplorer();
			pe.open();
			pe.getProject(PROJECT_NAME).getProjectItem(testPageName).open();

			editor = new JSPMultiPageEditor();
			assertEquals(testPageName, editor.getTitle());
		}

	}

}
