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

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.log.LogMessage;
import org.eclipse.reddeer.eclipse.ui.views.log.LogView;
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
	private static String noLogEntry = "No log entry found within maximum log size .*";
	private static List<ErrorInLog> errors;

	@BeforeClass
	public static void createProject() {
		String errorMessage;
		if (isLinux()) {
			errorMessage = "You currently have Visual editor configured to have better HTML5";
		} else if (isOSX()) {
			errorMessage = null;
		} else {
			errorMessage = "Visual Page Editor has experimental support for Windows 64-bit";
		}

		if (errorMessage != null) {
			ErrorInLog el = new ErrorInLog(errorMessage, "org.jboss.tools.vpe.preview.core");
			errors = new ArrayList<ErrorInLog>();
			errors.add(el);
			errors.add(el);
		}
		createWebProject();
		openErrorLog().deleteLog();
	}
	
	public void checkErrorLog() {
		List<LogMessage> msgs = openErrorLog().getErrorMessages();
		if (errors != null) {
			assertEquals(errors.size(), msgs.size());
			for (LogMessage lm : msgs) {
				for (ErrorInLog er : errors) {
					if (lm.getMessage().contains(er.getMessage()) && lm.getPlugin().equals(er.getPlugin())) {
						break;
					}
					fail("Unexpected error " + lm);
				}
			}
		} else {
			if (msgs.size() == 1) {
				if (!msgs.get(0).getMessage().matches(noLogEntry)) {
					fail("There's error in error log " + msgs.get(0));
				}
			} else {
				assertEquals(0, msgs.size());
			}
		}
	}

	private static LogView openErrorLog() {
		LogView lw = new LogView();
		lw.open();
		return lw;
	}

	@Test
	public void openXHTMLPageTest() {
		NewXHTMLWizard xw = new NewXHTMLWizard();
		xw.open();
		NewXHTMLFileWizardPage xp = new NewXHTMLFileWizardPage(xw);
		xp.setFileName(testPageName);
		xp.selectParentFolder(PROJECT_NAME);
		xw.finish();
		if (isOSX()) { // JSF editor is not supported on mac
			JSPMultiPageEditor editor = new JSPMultiPageEditor();
			assertFalse(editor.hasPreviewTab());
			assertFalse(editor.hasVisualSourceTab());
			assertTrue(editor.hasSourceTab());
		}

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
		checkErrorLog();

	}

}
