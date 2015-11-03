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
package org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSFileWizardPage;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSWizardDialog;
import org.junit.Test;

public class JBIDE3148and4441Test extends JSFAutoTestCase {

	private static String CSS_FILE_NAME = "JBIDE3148"; //$NON-NLS-1$
	private static String CSS_CLASS_NAME = "cssclass"; //$NON-NLS-1$

	@Test
	public void testJBIDE3148and4441() {
		packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", CSS_FILE_NAME + ".css")
					.open();
			new TextEditor(CSS_FILE_NAME + ".css").setText("@CHARSET \"UTF-8\";");
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
			NewCSSWizardDialog newCSSWizardDialog = new NewCSSWizardDialog();
			newCSSWizardDialog.open();
			new NewCSSFileWizardPage().setFileName(CSS_FILE_NAME);
			newCSSWizardDialog.finish();
		}
		TextEditor editor = new TextEditor(CSS_FILE_NAME + ".css");
		String editorText = editor.getText();
		editorText = "cssclass{\r\tcolor:red;\r\tbackground-color:green;\r}"
			+ "\n\rcssclass{\r\tcolor:green;\r\tbackground-color:red;\r}"
			+  editorText; 
		editor.setText(editorText);
		editor.save();
		new ContextMenu("Open CSS Dialog").select();
		new DefaultShell("CSS Class");
		new LabeledCombo("Style class:").setSelection(CSS_CLASS_NAME); //$NON-NLS-1$
		new DefaultTabItem("Text/Font").activate(); //$NON-NLS-1$
		new LabeledCombo("Text Decoration:").setSelection("underline"); //$NON-NLS-1$ //$NON-NLS-2$
		new LabeledCombo("Font Weight:").setSelection("bold"); //$NON-NLS-1$ //$NON-NLS-2$
		// Test edit attrs of the second class
		new LabeledCombo("Style class:").setSelection(CSS_CLASS_NAME + "(2)"); //$NON-NLS-1$ //$NON-NLS-2$
		new DefaultTabItem("Text/Font").activate(); //$NON-NLS-1$
		new LabeledCombo("Text Decoration:").setSelection("overline"); //$NON-NLS-1$ //$NON-NLS-2$
		new LabeledCombo("Font Weight:").setSelection("lighter"); //$NON-NLS-1$ //$NON-NLS-2$
		new PushButton("Apply").click(); //$NON-NLS-1$
		new OkButton().click();
		// Test check CSS file content
		assertTrue(
				"Content of CSS file in Editor is not as expected.\n" + //$NON-NLS-1$
						"Content: " + bot.editorByTitle(CSS_FILE_NAME + ".css").toTextEditor().getText(), //$NON-NLS-1$ //$NON-NLS-2$
				JBIDE3148and4441Test.testCssFileEditorContent(editor,
						"cssclass{", //$NON-NLS-1$
						"color: red;", //$NON-NLS-1$
						"background-color: green;", //$NON-NLS-1$
						"font-weight: bold;", //$NON-NLS-1$
						"text-decoration: underline", //$NON-NLS-1$
						"}", //$NON-NLS-1$
						"cssclass{", //$NON-NLS-1$
						"color: green;", //$NON-NLS-1$
						"background-color: red;", //$NON-NLS-1$
						"font-weight: lighter;", //$NON-NLS-1$
						"text-decoration: overline", //$NON-NLS-1$
						"}@CHARSET \"UTF-8\";")); //$NON-NLS-1$
		bot.editorByTitle(CSS_FILE_NAME + ".css").close(); //$NON-NLS-1$

	}

	private static boolean testCssFileEditorContent(TextEditor cssFileEditor, String... lines) {

		CssFileParser parserCssFileEditor = new CssFileParser();
		for (int line = 0 ; line < cssFileEditor.getNumberOfLines() ; line++) {
			parserCssFileEditor.addLine(cssFileEditor.getTextAtLine(line));
		}
		CssFileParser parserExceptedCssFile = new CssFileParser(lines);

		return parserCssFileEditor.compare(parserExceptedCssFile);

	}

}
