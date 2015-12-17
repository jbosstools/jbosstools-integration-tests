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

import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests editing of EL value
 * 
 * @author vlado pakan
 *
 */
public class EditingELValueTest extends PageDesignTestCase {

	private static final String GREETING_PAGE_NAME = "greeting.xhtml";
	private static final String INPUT_NAME_PAGE_NAME = "inputname.xhtml";
	private static final String EL_VARIABLE_NAME = "request.contextPath";
	private TextEditor jspGreetingPageEditor;
	private TextEditor jspInputNamePageEditor;
	private String greetingPageOrigText;
	private String inputNamePageOrigText;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
	}

	/**
	 * Tests editing of EL value
	 */
	@Test
	public void testEditingELValue() {
		openPage(EditingELValueTest.GREETING_PAGE_NAME, VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
		openPage(EditingELValueTest.INPUT_NAME_PAGE_NAME, VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
		jspGreetingPageEditor = new TextEditor(EditingELValueTest.GREETING_PAGE_NAME);
		greetingPageOrigText = jspGreetingPageEditor.getText();
		jspInputNamePageEditor = new TextEditor(EditingELValueTest.INPUT_NAME_PAGE_NAME);
		inputNamePageOrigText = jspGreetingPageEditor.getText();
		jspGreetingPageEditor.setText(jspGreetingPageEditor.getText().replaceFirst("/templates/common.xhtml",
				"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml"));
		jspGreetingPageEditor.save();
		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser(EditingELValueTest.GREETING_PAGE_NAME),
				"Template file is not found: \"templates/common.xhtml\"", EditingELValueTest.GREETING_PAGE_NAME);
		jspInputNamePageEditor.setText(jspInputNamePageEditor.getText().replaceFirst("/templates/common.xhtml",
				"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml"));
		jspInputNamePageEditor.save();
		assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser(EditingELValueTest.INPUT_NAME_PAGE_NAME),
				"Template file is not found: \"templates/common.xhtml\"", EditingELValueTest.INPUT_NAME_PAGE_NAME);
		// Opens Page Design Options Dialog
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		new PushButton("Add").click();
		new DefaultShell("Add EL Reference");
		new LabeledText("Value").setText("/");
		new LabeledText("El Name*").setText(EditingELValueTest.EL_VARIABLE_NAME);
		new FinishButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
		jspGreetingPageEditor.close();
		jspInputNamePageEditor.close();
		openPage(EditingELValueTest.GREETING_PAGE_NAME, VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
		openPage(EditingELValueTest.INPUT_NAME_PAGE_NAME, VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
		// Checks Visual Representation of Pages
		SWTBotWebBrowser webBrowserGreetingPage = new SWTBotWebBrowser(EditingELValueTest.GREETING_PAGE_NAME);
		assertVisualEditorNotContainNodeWithValue(webBrowserGreetingPage,
				" Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"",
				EditingELValueTest.GREETING_PAGE_NAME);
		SWTBotWebBrowser webBrowserInputNamePage = new SWTBotWebBrowser(EditingELValueTest.INPUT_NAME_PAGE_NAME);
		assertVisualEditorNotContainNodeWithValue(webBrowserInputNamePage,
				" Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"",
				EditingELValueTest.INPUT_NAME_PAGE_NAME);
		// Delete EL Variable from Pages
		jspGreetingPageEditor = new TextEditor(EditingELValueTest.GREETING_PAGE_NAME);
		jspGreetingPageEditor.setText(jspGreetingPageEditor.getText()
				.replaceFirst("\\#\\{" + EditingELValueTest.EL_VARIABLE_NAME + "\\}", "/"));
		jspGreetingPageEditor.save();
		assertVisualEditorNotContainNodeWithValue(webBrowserGreetingPage,
				" Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"",
				EditingELValueTest.GREETING_PAGE_NAME);
		jspInputNamePageEditor = new TextEditor(EditingELValueTest.INPUT_NAME_PAGE_NAME);
		jspInputNamePageEditor.setText(jspInputNamePageEditor.getText()
				.replaceFirst("\\#\\{" + EditingELValueTest.EL_VARIABLE_NAME + "\\}", "/"));
		jspInputNamePageEditor.save();
		assertVisualEditorNotContainNodeWithValue(webBrowserInputNamePage,
				" Template file is not found: \"#{" + EditingELValueTest.EL_VARIABLE_NAME + "}templates/common.xhtml\"",
				EditingELValueTest.INPUT_NAME_PAGE_NAME);
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		boolean elFound = false;
		try {
			new DefaultTable().getItem(EditingELValueTest.EL_VARIABLE_NAME,1);
			elFound = true;
		} catch (SWTLayerException swtle){
			elFound = false;
		}
		assertTrue("EL Substitution for EL Name " + EditingELValueTest.EL_VARIABLE_NAME + " is not defined.",
				elFound);
		new OkButton().click();
	}

	@Override
	public void tearDown() throws Exception {
		deleteAllELSubstitutions();
		if (jspGreetingPageEditor != null) {
			jspGreetingPageEditor.activate();
			deleteAllELSubstitutions();
			jspGreetingPageEditor.setText(greetingPageOrigText);
			jspGreetingPageEditor.save();
			jspGreetingPageEditor.close();
		}
		if (jspInputNamePageEditor != null) {
			jspInputNamePageEditor.activate();
			deleteAllELSubstitutions();
			jspInputNamePageEditor.setText(inputNamePageOrigText);
			jspInputNamePageEditor.save();
			jspInputNamePageEditor.close();
		}
		super.tearDown();
	}
}
