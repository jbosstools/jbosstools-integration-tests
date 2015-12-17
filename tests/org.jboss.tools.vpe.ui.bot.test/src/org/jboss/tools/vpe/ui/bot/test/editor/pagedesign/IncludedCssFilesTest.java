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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSFileWizardPage;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSWizardDialog;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests functionality of Included CSS Files tab page of Page Design Options
 * Dialog
 * 
 * @author vlado pakan
 *
 */
public class IncludedCssFilesTest extends PageDesignTestCase {

	private static final String CSS_FILE_NAME = "includedCssFileTest.css";
	private static final String HTML_FILE_NAME = "includedCssFileTest.html";

	// Page Design option is not supported for HTML for now
	@Ignore
	@Test
	public void testIncludedCssFiles() throws IOException {
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages")
				.select();
		// add CSS File
		NewCSSWizardDialog newCSSWizardDialog = new NewCSSWizardDialog();
		newCSSWizardDialog.open();
		NewCSSFileWizardPage newCSSFileWizardPage = new NewCSSFileWizardPage();
		newCSSFileWizardPage.setFileName(IncludedCssFilesTest.CSS_FILE_NAME);
		newCSSWizardDialog.finish();
		new WaitWhile(new JobIsRunning());
		TextEditor cssEditor = new TextEditor(IncludedCssFilesTest.CSS_FILE_NAME);
		cssEditor.setText("h1 {\n" + "color: Red\n" + "}");
		cssEditor.save();
		cssEditor.close();
		// add HTML File
		createHtmlPage(IncludedCssFilesTest.HTML_FILE_NAME);
		new WaitWhile(new JobIsRunning());
		TextEditor htmlEditor = new TextEditor(IncludedCssFilesTest.HTML_FILE_NAME);
		htmlEditor.setText("<html>\n" + "  <body>\n" + "    <h1>Title</h1>\n" + "  </body>\n" + "</html>");
		htmlEditor.save();
		new WaitWhile(new JobIsRunning());
		// add CSS File Reference
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Included css files").activate();
		new PushButton("Add").click();
		new DefaultShell("Add CSS Reference");
		new LabeledText("CSS File Path*").setText(
				SWTUtilExt.getPathToProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME) + File.separator + "WebContent"
						+ File.separator + "pages" + File.separator + IncludedCssFilesTest.CSS_FILE_NAME);
		new FinishButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
		htmlEditor.activate();
		new DefaultCTabItem("Preview").activate();
		Browser browser0 = new InternalBrowser(0);
		Browser browser1 = new InternalBrowser(1);
		final String textToContain = "h1 {color: Red}";
		// browser0 is editable browser displayed on page Visual/Source
		boolean firstBrowserIsEditable = browser0.getText().contains("dragIcon");
		if (firstBrowserIsEditable) {
			assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
					browser1.getText().contains(textToContain));
		} else {
			assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
					browser0.getText().contains(textToContain));
		}
		// Test Visual Interpretation of CSS in Visual/Source Pane
		htmlEditor.activate();
		new DefaultCTabItem("Visual/Source").activate();
		if (firstBrowserIsEditable) {
			assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
					browser1.getText().contains(textToContain));
		} else {
			assertTrue("Preview Browser displayed Web Page Incorretly. There is no H1 tag with Red Color",
					browser0.getText().contains(textToContain));
		}
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Included css files").activate();
		new DefaultTableItem().select();
		new PushButton("Remove").click();
		new OkButton().click();
		htmlEditor.close();
	}
}
