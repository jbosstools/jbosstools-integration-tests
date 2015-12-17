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
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
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
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests functionality of Included CSS Files tab page of Page Design Options
 * Dialog with JSP File
 * 
 * @author vlado pakan
 *
 */
public class IncludedCssFilesJSPTest extends PageDesignTestCase {

	private static final String CSS_FILE_NAME = "includedCssFileJSPTest.css";
	private static final String JSP_FILE_NAME = "includedCssFileJSPTest.jsp";
	
	@Test
	public void testIncludedCssFilesJSP() throws IOException {
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages")
				.select();
		// add CSS File
		NewCSSWizardDialog newCSSWizardDialog = new NewCSSWizardDialog();
		newCSSWizardDialog.open();
		NewCSSFileWizardPage newCSSFileWizardPage = new NewCSSFileWizardPage();
		newCSSFileWizardPage.setFileName(IncludedCssFilesJSPTest.CSS_FILE_NAME);
		newCSSWizardDialog.finish();
		new WaitWhile(new JobIsRunning());
		TextEditor cssEditor = new TextEditor(IncludedCssFilesJSPTest.CSS_FILE_NAME);
		cssEditor.setText(
				".post-info {\n" + "  color: blue;\n" + "}\n" + ".post-info a {\n" + "  color: orange;\n" + "}");
		cssEditor.save();
		cssEditor.close();
		// add HTML File
		createJspPage(IncludedCssFilesJSPTest.JSP_FILE_NAME);
		new WaitWhile(new JobIsRunning());
		TextEditor jspEditor = new TextEditor(IncludedCssFilesJSPTest.JSP_FILE_NAME);
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" + "  <body>\n"
				+ "    <p class=\"post-info\">Posted by\n" + "    <a href=\"index.html\">a href</a>\n" + "    <br>\n"
				+ "    <h:outputLink  value=\"url\" value=\"/index.jsp\">h:outputLink</h:outputLink>\n"
				+ "    <h:form>\n" + "      <h:commandLink value=\"h:commandLink\"/>\n" + "    </h:form>\n" + "  </p>\n"
				+ "</body>\n");
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		// add CSS File Reference
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Included css files").activate();
		new PushButton("Add").click();
		new DefaultShell("Add CSS Reference");
		new LabeledText("CSS File Path*").setText(
				SWTUtilExt.getPathToProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME) + File.separator + "WebContent"
						+ File.separator + "pages" + File.separator + IncludedCssFilesJSPTest.CSS_FILE_NAME);
		new FinishButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
		new WaitWhile(new JobIsRunning());
		SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(IncludedCssFilesJSPTest.JSP_FILE_NAME);
		assertVisualEditorContainsNodeWithValue(webBrowser,
				".post-info {  color: blue;}.post-info a {  color: orange;}", IncludedCssFilesJSPTest.JSP_FILE_NAME);
		assertVisualEditorContains(webBrowser, "P", new String[] { "class" }, new String[] { "post-info" },
				IncludedCssFilesJSPTest.JSP_FILE_NAME);
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Included css files").activate();
		new DefaultTableItem().select();
		new PushButton("Remove").click();
		new OkButton().click();
		jspEditor.close();
	}
}
