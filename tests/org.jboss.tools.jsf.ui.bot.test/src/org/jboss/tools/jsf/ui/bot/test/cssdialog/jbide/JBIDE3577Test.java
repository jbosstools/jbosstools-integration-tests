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

public class JBIDE3577Test extends JSFAutoTestCase{

	private static String CSS_FILE_NAME = "JBIDE3577"; //$NON-NLS-1$
	private static String CSS_CLASS_NAME = "cssclass"; //$NON-NLS-1$
	@Test
	public void testJBIDE3577(){
		packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",CSS_FILE_NAME+".css").open();
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
			+  editorText; 
		editor.setText(editorText);
		editor.save();
		new ContextMenu("Open CSS Dialog").select();
		new DefaultShell("CSS Class");
		new LabeledCombo("Style class:").setSelection(CSS_CLASS_NAME);
		new DefaultTabItem("Preview").activate();
		new PushButton("Clear").click();
		new PushButton("Apply").click();
		new OkButton().click();
		assertEquals(JSFAutoTestCase.stripCSSText("cssclass{\r\t\r}@CHARSET \"UTF-8\";"), 
		    JSFAutoTestCase.stripCSSText(editor.getText()));
		editor.close(); 
	}
}
