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
package org.jboss.tools.jsf.ui.bot.test.smoke;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigEditor;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigSourceEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSFileWizardPage;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSWizardDialog;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * Test open on functionality of JSF components within jsp page
 * 
 * @author Vladimir Pakan
 *
 */
public class OpenOnTest extends JSFAutoTestCase {
	/**
	 * Test open on functionality of JSF components within jsp page
	 */
	@Test
	public void testOpenOn() {
		EditorHandler.getInstance().closeAll(true);
		openPage();
		checkOpenOn();
	}

	/**
	 * Test open on functionality of faces-config.xml file
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testFacesConfigOpenOn() throws Throwable {
		EditorHandler.getInstance().closeAll(true);
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "WEB-INF", "faces-config.xml").open();
		checkFacesConfigOpenOn();
	}

	/**
	 * Check Open On functionality for jsp page
	 */
	private void checkOpenOn() {
		// Check open on for #{Message.header} EL
		String expectedOpenedFileName = "Messages.properties";
		Editor openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 12, 40, expectedOpenedFileName);
		assertTrue("First Table Item in " + expectedOpenedFileName + " file is not selected",
				new DefaultTableItem(0).isSelected());
		openedEditor.close();
		// Check open on for #{Message.prompt_message} EL
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 17, 47, expectedOpenedFileName);
		assertTrue("Second Table Item in " + expectedOpenedFileName + " file is not selected",
				new DefaultTableItem(1).isSelected());
		openedEditor.close();
		// Check open on for "#{user.name} EL when text 'user' is selected
		expectedOpenedFileName = "User.java";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 18, 30, expectedOpenedFileName);
		String selectedTextInEditor = new TextEditor(expectedOpenedFileName).getSelectedText();
		String expectedSelectedTextInEditor = "User";
		assertTrue(
				"Selected text in editor has to be " + expectedSelectedTextInEditor + " but is " + selectedTextInEditor,
				selectedTextInEditor.equalsIgnoreCase(expectedSelectedTextInEditor));
		openedEditor.close();
		// Check open on for "#{user.name} EL when text 'name' is selected
		expectedOpenedFileName = "User.java";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(TEST_PAGE, 18, 35, expectedOpenedFileName);
		selectedTextInEditor = new TextEditor(expectedOpenedFileName).getSelectedText();
		expectedSelectedTextInEditor = "getName";
		assertTrue(
				"Selected text in editor has to be " + expectedSelectedTextInEditor + " but is " + selectedTextInEditor,
				selectedTextInEditor.equalsIgnoreCase(expectedSelectedTextInEditor));
		openedEditor.close();
	}

	/**
	 * Check Open On functionality for faces-config.xml file
	 */
	private void checkFacesConfigOpenOn() {
		// Check open on for demo.User managed bean
		FacesConfigEditor fce = new FacesConfigEditor("faces-config.xml");
		FacesConfigSourceEditor facesConfigSourceEditor = fce.getFacesConfigSourceEditor();
		String expectedOpenedFileName = "User.java";
		Editor openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(facesConfigSourceEditor, 7, 30,
				expectedOpenedFileName);
		String selectedTextInSourceEditor = new TextEditor(expectedOpenedFileName).getSelectedText();
		String expectedSelectedTextInEditor = "User";
		assertTrue(
				"Selected text in editor has to be " + expectedSelectedTextInEditor + " but is "
						+ selectedTextInSourceEditor,
				selectedTextInSourceEditor.equalsIgnoreCase(expectedSelectedTextInEditor));
		openedEditor.close();
		// Check open on for name property of demo.User managed bean
		expectedOpenedFileName = "User.java";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(facesConfigSourceEditor, 10, 21, expectedOpenedFileName);
		selectedTextInSourceEditor = new TextEditor(expectedOpenedFileName).getSelectedText();
		expectedSelectedTextInEditor = "getName";
		assertTrue(
				"Selected text in editor has to be " + expectedSelectedTextInEditor + " but is "
						+ selectedTextInSourceEditor,
				selectedTextInSourceEditor.equalsIgnoreCase(expectedSelectedTextInEditor));
		openedEditor.close();
		// Check open on for java.lang.String class
		expectedOpenedFileName = "String.class";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(facesConfigSourceEditor, 11, 31, expectedOpenedFileName);
		openedEditor.close();
		// Check open on for URI /pages/inputUserName.jsp within <from-view-id>
		// tag
		expectedOpenedFileName = "inputUserName.jsp";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(facesConfigSourceEditor, 16, 28, expectedOpenedFileName);
		openedEditor.close();
		// Check open on for URI /pages/hello.jsp within <to-view-id> tag
		expectedOpenedFileName = "hello.jsp";
		openedEditor = OpenOnHelper.checkOpenOnFileIsOpened(facesConfigSourceEditor, 19, 25, expectedOpenedFileName);
		openedEditor.close();

	}

	/**
	 * Test Open On functionality for Composite Component
	 */
	@Test
	public void testOpenOnForCompositeComponent() {
		EditorHandler.getInstance().closeAll(true);
		createJSF2Project(JSF2_TEST_PROJECT_NAME);
		openPage(JSF2_TEST_PAGE, JSF2_TEST_PROJECT_NAME);
		Editor edTestPage = new DefaultEditor(JSF2_TEST_PAGE);
		// Check open on for <ez:input
		String expectedOpenedFileName = "input.xhtml";
		Editor compositeComponentEditor = OpenOnHelper.checkOpenOnFileIsOpened(JSF2_TEST_PAGE, 15, 10,
				expectedOpenedFileName);
		edTestPage.close();
		// Check open on for cc.attrs.submitlabel
		OpenOnHelper.checkOpenOnFileIsOpened(expectedOpenedFileName, 18, 75,
				expectedOpenedFileName);
		String selectedText = new TextEditor(expectedOpenedFileName).getSelectedText();
		String expectedSelectedText = "<composite:attribute name=\"submitlabel\"/>";
		assertTrue("Selected text in editor has to be " + expectedSelectedText + " but it is " + selectedText,
				selectedText.equalsIgnoreCase(expectedSelectedText));
		compositeComponentEditor.close();
	}
	 
	/**
	 * Test Open On functionality for Referenced Template within JSF2 project
	 */
	@Test
	public void testOpenOnForReferencedTemplateJsf2() {
		EditorHandler.getInstance().closeAll(true);
		createJSF2Project(JSF2_TEST_PROJECT_NAME);
		openPage(JSF2_TEST_PAGE, JSF2_TEST_PROJECT_NAME);
		// Check open on for <ez:input
		String expectedOpenedFileName = "common.xhtml";
		Editor compositeComponentEditor = OpenOnHelper.checkOpenOnFileIsOpened(JSF2_TEST_PAGE, 7, 41,
				expectedOpenedFileName);
		compositeComponentEditor.close();
	}

	/**
	 * Test Open On functionality for Referenced Template within JSF facelets
	 * project
	 */
	@Test
	public void testOpenOnForReferencedTemplateFacelets() {
		EditorHandler.getInstance().closeAll(true);
		openPage(FACELETS_TEST_PAGE, FACELETS_TEST_PROJECT_NAME);
		// Check open on for <ez:input
		String expectedOpenedFileName = "common.xhtml";
		Editor compositeComponentEditor = OpenOnHelper.checkOpenOnFileIsOpened(FACELETS_TEST_PAGE, 9, 45, expectedOpenedFileName);
		compositeComponentEditor.close();
	}

	/**
	 * Test Open On functionality for CSS file linked via <h:outputStyleSheet>
	 */
	@Test
	public void testOpenOnForHOutputStyleSheet() {
		createJSF2Project(JSF2_TEST_PROJECT_NAME); // create css file SWTBotExt
		packageExplorer.open();
		try {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources");
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
			new ShellMenu("File", "New", "Folder").select();
			new DefaultShell(IDELabel.Shell.NEW_FOLDER);
			new LabeledText("Folder name:").setText("resources");
			new FinishButton().click();
		}
		try {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources", "css");
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources").select();
			new ShellMenu("File", "New", "Folder").select();
			new DefaultShell(IDELabel.Shell.NEW_FOLDER);
			new LabeledText("Folder name:").setText("css");
			new FinishButton().click();
		}
		final String cssFileName = "OpenOnTest.css";
		try {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources", "css",
					cssFileName);
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JSF2_TEST_PROJECT_NAME).getProjectItem("WebContent", "resources", "css")
					.select();
			NewCSSWizardDialog newCSSWizardDialog = new NewCSSWizardDialog();
			newCSSWizardDialog.open();
			NewCSSFileWizardPage newCSSFileWizardPage = new NewCSSFileWizardPage();
			newCSSFileWizardPage.selectParentFolder(JSF2_TEST_PROJECT_NAME, "WebContent", "resources", "css");
			newCSSFileWizardPage.setFileName(cssFileName);
			newCSSWizardDialog.finish();
		}
		EditorHandler.getInstance().closeAll(true);
		// create test page
		final String testPageName = "OpenOnHOutpuStyle.xhtml";
		createXhtmlPage(testPageName, JSF2_TEST_PROJECT_NAME, "WebContent", "pages");
		openPage(testPageName, JSF2_TEST_PROJECT_NAME);
		TextEditor xhtmlEditor = new TextEditor(testPageName);
		xhtmlEditor.setText(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n"
						+ "      xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n"
						+ "      xmlns:h=\"http://java.sun.com/jsf/html\">\n"
						+ "  <ui:composition>\n"
						+ "    <h:outputStylesheet library=\"css\" name=\"" + cssFileName + "\"/>"
						+ "  </ui:composition>\n" + "</html>");
		xhtmlEditor.save();
		// check OpenOn
		Editor cssEditor = OpenOnHelper.checkOpenOnFileIsOpened(xhtmlEditor, 5, 47, cssFileName);
		cssEditor.close();
		xhtmlEditor.close();
	}
}