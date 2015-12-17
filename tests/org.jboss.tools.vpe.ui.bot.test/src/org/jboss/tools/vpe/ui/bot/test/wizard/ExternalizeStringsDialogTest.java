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
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.jboss.reddeer.swt.api.ToolItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigEditor;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigSourceEditor;
import org.jboss.tools.common.reddeer.propertieseditor.PropertiesEditor;
import org.jboss.tools.common.reddeer.propertieseditor.PropertiesSourceEditor;
import org.jboss.tools.jst.web.ui.internal.editor.messages.JstUIMessages;

public class ExternalizeStringsDialogTest extends VPEAutoTestCase {

	private final String ENABLED_TEST_TEXT = "<html>Externalize Text</html>"; //$NON-NLS-1$
	private final String TOOL_TIP = (RunningPlatform.isOSX() ? "Externalize selected string..." //$NON-NLS-1$
			: "Externalize selected string...");
	private final String FOLDER_TEXT_LABEL = "Enter or select the parent folder:"; //$NON-NLS-1$
	private final String INCORRECT_TABLE_VALUE = "Table value is incorrect"; //$NON-NLS-1$
	private final String TOOLBAR_ICON_ENABLED = "Toolbar button should be enabled"; //$NON-NLS-1$
	private final String TOOLBAR_ICON_DISABLED = "Toolbar button should be disabled"; //$NON-NLS-1$
	private final String COMPLEX_TEXT = "!! HELLO ~ Input User, Name.Page ?" //$NON-NLS-1$
			+ " \r\n and some more text \r\n" //$NON-NLS-1$
			+ "@ \\# vc \\$ % yy^ &*(ghg ) _l-kk+mmm\\/fdg\\ " //$NON-NLS-1$
			+ "\t ;.df:,ee {df}df[ty]"; //$NON-NLS-1$
	private final String COMPLEX_KEY_RESULT = "HELLO_Input_User_Name_Page_and" + //$NON-NLS-1$
			"_some_more_text_vc_yy_ghg_l_kk_mmm_fdg_df_ee_df_df_ty"; //$NON-NLS-1$
	private final String COMPLEX_VALUE_RESULT = "\\r\\n!! HELLO ~ Input User, Name.Page ? \\r\\n and some more text " + //$NON-NLS-1$
			"\\r\\n@ \\# vc \\$ % yy^ &*(ghg ) _l-kk+mmm\\/fdg\\ \\t ;.df:,ee {df}df[ty]\\r\\n\\t\\t";; //$NON-NLS-1$

	public ExternalizeStringsDialogTest() {
		super();
	}

	@Test
	public void testExternalizeStringsDialog() throws Throwable {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		TextEditor editor = new TextEditor(TEST_PAGE);
		/*
		 * Select some text
		 */
		editor.selectText("User");
		/*
		 * Get toolbar button
		 */
		new DefaultToolItem(TOOL_TIP).click();
		;
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check properties key and value fields
		 */
		Text defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("User", defKeyText.getText());
		Text defValueText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		assertEquals("User", defValueText.getText());
		/*
		 * Check that "Next" button is disabled
		 */
		assertFalse("Checkbox should be unchecked.",
				new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE).isChecked());
		assertFalse("Next button should be disabled.", new NextButton().isEnabled());
		/*
		 * Select existed resource bundle
		 */
		Combo combo = new DefaultCombo();
		combo.setSelection(0);
		assertEquals("demo.Messages", combo.getSelection());
		/*
		 * Check table results
		 */
		Table table = new DefaultTable();
		/*
		 * The list should be sorted in the alphabetical order.
		 */
		assertEquals(INCORRECT_TABLE_VALUE, "header", table.getItem(0).getText(0));
		assertEquals(INCORRECT_TABLE_VALUE, "Hello Demo Application", table.getItem(0).getText(1));
		assertEquals(INCORRECT_TABLE_VALUE, "hello_message", table.getItem(1).getText(0));
		assertEquals(INCORRECT_TABLE_VALUE, "Hello", table.getItem(1).getText(1));
		assertEquals(INCORRECT_TABLE_VALUE, "prompt_message", table.getItem(2).getText(0));
		assertEquals(INCORRECT_TABLE_VALUE, "Name:", table.getItem(2).getText(1));
		/*
		 * Press OK and replace the text in the editor
		 */
		new OkButton().click();
		/*
		 * Check replaced text
		 */
		assertEquals("Replaced text is incorrect", 220, editor.getPositionOfText("#{Message.User}"));
		editor.close();
		/*
		 * Check that properties file has been updated
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("JavaSource", "demo", "Messages.properties")
				.open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("Messages.properties");
		PropertiesSourceEditor propertiesSopurceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSopurceEditor.selectLine(3);
		String line = propertiesSopurceEditor.getSelectedText();
		assertEquals("'Messages.properties' was updated incorrectly", "User=User", line);
	}

	@Test
	public void testExternalizingTheSameTextAgain() throws Throwable {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		TextEditor editor = new TextEditor(TEST_PAGE);
		/*
		 * Select some text
		 */
		editor.selectText("User");
		assertEquals("Replaced text is incorrect", "User", editor.getSelectedText());
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check the generated property key. It should be as is. The dialog
		 * should use the stored key-pair value. OK button should be enabled. No
		 * modifications to the properties file should be made.
		 */
		assertEquals("User", new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY).getText());
		new OkButton().click();
		editor.close();
		/*
		 * Check that properties file hasn't been modified.
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("JavaSource", "demo", "Messages.properties")
				.open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("Messages.properties");
		PropertiesSourceEditor propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSourceEditor.selectLine(3);
		String line = propertiesSourceEditor.getSelectedText();
		assertEquals("'Messages.properties' was updated incorrectly", "User=User", line);
		/*
		 * Change the property value to the new one, let say 'User1'. And
		 * externalize the same string again.
		 */
		propertiesSourceEditor.setText(propertiesSourceEditor.getText() + "1");
		propertiesEditor.save();
		propertiesEditor.close();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		editor = new TextEditor(TEST_PAGE);
		editor.activate();
		editor.selectText("User");
		assertEquals("Replaced text is incorrect", editor.getPositionOfText("User"), 220);
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * The value has been changed and now unique. But the same key is in the
		 * bundle. Thus the dialog should suggest "User_1" as a key value.
		 */
		Text defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("User_1", defKeyText.getText());
		Button btnOK = new OkButton();
		assertTrue("(OK) button should be enabled.", btnOK.isEnabled());
		/*
		 * Change the key to validate duplicate key situation
		 */
		defKeyText.setText("User");
		assertFalse("(OK) button should be disabled.", btnOK.isEnabled());
		defKeyText.setText("User_1");
		assertTrue("(OK) button should be enabled.", btnOK.isEnabled());
		btnOK.click();
		editor.close();
		/*
		 * Check that the new key has been added.
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("JavaSource", "demo", "Messages.properties")
				.open();
		propertiesEditor = new PropertiesEditor("Messages.properties");
		propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSourceEditor.selectLine(4);
		line = propertiesSourceEditor.getSelectedText();
		assertEquals("'Messages.properties' was updated incorrectly", "User_1=User", line);
	}

	@Test
	public void testCompoundKeyWithDot() throws Throwable {
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		TextEditor editor = new TextEditor(TEST_PAGE);
		editor.selectText("User");
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check properties key and value fields
		 */
		Text defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("User_1", defKeyText.getText());
		defKeyText.setText("user.compoundKey");
		Text defValueText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		assertEquals("User", defValueText.getText());
		assertFalse("Checkbox should be unchecked.",
				new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE).isChecked());
		/*
		 * Check that "Next" button is disabled
		 */
		assertFalse("Next button should be disabled.", new NextButton().isEnabled());
		/*
		 * Select existed resource bundle
		 */
		Combo combo = new DefaultCombo();
		combo.setSelection(0);
		assertEquals("demo.Messages", combo.getText());
		/*
		 * Press OK and replace the text in the editor
		 */
		new OkButton().click();
		/*
		 * Check replaced text
		 */
		assertEquals("Replaced text is incorrect", editor.getPositionOfText("#{Message['user.compoundKey']}"), 1);
		editor.close();
		/*
		 * Check that properties file has been updated
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("JavaSource", "demo", "Messages.properties")
				.open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("Messages.properties");
		PropertiesSourceEditor propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		/*
		 * Select the 4th line. In the 3rd line should be results from the
		 * previous test "User=User". If previous test failed -- it could crash
		 * this one as well.
		 */
		propertiesSourceEditor.selectLine(5);
		String line = propertiesSourceEditor.getSelectedText();
		assertEquals("'Messages.properties' was updated incorrectly", "user.compoundKey=User", line);
	}

	@Test
	public void testExternalizeStringsDialogInXhtml() throws Throwable {
		packageExplorer.getProject(FACELETS_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", FACELETS_TEST_PAGE)
				.open();
		TextEditor editor = new TextEditor(FACELETS_TEST_PAGE);
		/*
		 * Select some text
		 */
		editor.selectText("User");
		/*
		 * Get toolbar button
		 */
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check properties key and value fields
		 */
		Text defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("User", defKeyText.getText());
		Text defValueText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		assertEquals("User", defValueText.getText());
		/*
		 * Check that "Next" button is disabled
		 */
		assertFalse("Checkbox should be unchecked.",
				new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE).isChecked());
		/*
		 * Check that "Next" button is disabled
		 */
		assertFalse("Next button should be disabled.", new NextButton().isEnabled());
		/*
		 * Select existed resource bundle
		 */
		Combo combo = new DefaultCombo();
		combo.setSelection(0);
		assertEquals("resources", combo.getText());
		/*
		 * Check table results
		 */
		Table table = new DefaultTable();
		/*
		 * The list should be sorted in the alphabetical order.
		 */
		assertEquals(INCORRECT_TABLE_VALUE, "greeting", table.getItem(0).getText(0));
		assertEquals(INCORRECT_TABLE_VALUE, "Hello", table.getItem(0).getText(1));
		assertEquals(INCORRECT_TABLE_VALUE, "prompt", table.getItem(1).getText(0));
		assertEquals(INCORRECT_TABLE_VALUE, "Your Name:", table.getItem(1).getText(1));
		/*
		 * Press OK and replace the text in the editor
		 */
		new OkButton().click();
		/*
		 * Check replaced text
		 */
		assertEquals("Replaced text is incorrect", editor.getPositionOfText("Input #{msg.User} Name"), 513);
		/*
		 * Check that properties file has been updated
		 */
		packageExplorer.getProject(FACELETS_TEST_PROJECT_NAME).getProjectItem("JavaSource", "resources.properties")
				.open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("resources.properties");
		PropertiesSourceEditor propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSourceEditor.selectLine(3);
		String line = propertiesSourceEditor.getSelectedText();
		propertiesEditor.close();
		assertEquals("'resources.properties' was updated incorrectly", "User=User", line);
		editor.close();
	}

	@Test
	public void testNewFileInExternalizeStringsDialog() throws Throwable {
		createNewBundleToLastPage(JBT_TEST_PROJECT_NAME + "/WebContent/pages", "externalize.properties");
		/*
		 * 'OK' button should be enabled. Press it.
		 */
		new OkButton().click();
		TextEditor editor = new TextEditor();
		new WaitWhile(new JobIsRunning());
		/*
		 * Check that the text was replaced
		 */
		assertEquals("Replaced text is incorrect", 214, editor.getPositionOfText("#{.Input}"));
		/*
		 * Check that properties file has been created
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "pages", "externalize.properties").open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("externalize.properties");
		PropertiesSourceEditor propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSourceEditor.selectLine(0);
		String line = propertiesSourceEditor.getSelectedText();
		editor.close();
		propertiesEditor.close();
		assertEquals("Created file is incorrect", "Input=Input", line);
	}

	@Test
	public void testEmptySelectionInExternalizeStringsDialog() throws Throwable {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		TextEditor editor = new TextEditor(TEST_PAGE);
		/*
		 * Select some text
		 */
		editor.selectText(" ", 11); // <h:messages style - select space
		/*
		 * There is an exception caused by the fact that line delimiter was
		 * selected. But for this test it's ok, so just ignore this exception.
		 */
		ToolItem tiExternalize = new DefaultToolItem(TOOL_TIP);
		/*
		 * Check that the toolbar button is disabled
		 */
		assertFalse("Toolbar button should be disabled", tiExternalize.isEnabled());
		/*
		 * Select some text
		 */
		editor.setCursorPosition(21, 50);
		/*
		 * Send key press event to fire VPE listeners
		 */
		editor.activate();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		/*
		 * Activate the dialog
		 */
		tiExternalize.click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check that the property value text is auto completed.
		 */
		assertEquals("Say Hello!",
				new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE).getText());
		/*
		 * Close the dialog
		 */
		new CancelButton().click();
		/*
		 * Type some text outside the tag
		 */
		editor.activate();
		editor.insertText(13, 0, COMPLEX_TEXT);
		/*
		 * Select nothing and call the dialog -- the whole text should be
		 * selected.
		 */
		editor.setCursorPosition(13, 3);
		/*
		 * Activate the dialog
		 */
		tiExternalize.click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check that the property key and value text
		 */
		assertEquals(COMPLEX_KEY_RESULT,
				new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY).getText());
		assertEquals(COMPLEX_VALUE_RESULT,
				new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE).getText());
		/*
		 * Close the dialog
		 */
		new CancelButton().click();
		/*
		 * Check selection in the attribute's value
		 */
		editor.save();
		editor.setCursorPosition(19, 109);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		new WaitWhile(new JobIsRunning());
		/*
		 * Activate the dialog
		 */
		tiExternalize.click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check that the property value text is empty
		 */
		assertEquals("true", new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE).getText());
		/*
		 * Close the dialog
		 */
		new CancelButton().click();
	}

	@Test
	public void testNewFileCreationWithoutAnyExistedBundles() throws Throwable {
		/*
		 * Open hello.jsp file
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", "hello.jsp").open();
		TextEditor editor = new TextEditor("hello.jsp");
		/*
		 * Select some text
		 */
		editor.setCursorPosition(3, 0);
		editor.selectLine(3);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
		editor.insertText(3, 0, "Plain text");
		editor.setCursorPosition(3, 2);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		editor.save();
		/*
		 * Activate the dialog
		 */
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check properties key and value fields
		 */
		Text defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("Plain_text", defKeyText.getText());
		Text defValueText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		assertEquals("\\r\\n\\r\\nPlain text\\r\\n", defValueText.getText());
		/*
		 * Check that checkbox for the new file is selected
		 */
		assertTrue(new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE).isChecked());
		/*
		 * 'Next>' button should. Press it.
		 */
		new NextButton().click();
		/*
		 * Check the folder name
		 */
		assertEquals(JBT_TEST_PROJECT_NAME + "/JavaSource", new LabeledText(FOLDER_TEXT_LABEL).getText());
		/*
		 * Check the file name
		 */
		assertEquals("hello.properties", new LabeledText("File name:").getText());
		/*
		 * 'Next>' button should be enabled. Press it.
		 */
		new NextButton().click();
		/*
		 * Check that 'manually by user' radiobutton is selected
		 */
		assertTrue(
				"(" + JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_USER_DEFINED + ") " + "radio button should be enabled.", //$NON-NLS-3$
				new RadioButton(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_USER_DEFINED).isSelected());
		/*
		 * Create new file
		 */
		new OkButton().click();
		/*
		 * Check the file content
		 */
		editor.close();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("JavaSource", "hello.properties").open();
		PropertiesEditor propertiesEditor = new PropertiesEditor("hello.properties");
		PropertiesSourceEditor propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
		propertiesSourceEditor.selectLine(0);
		String line = propertiesSourceEditor.getSelectedText();
		assertEquals("Created file is incorrect", "Plain_text=\\r\\n\\r\\nPlain\\ text\\r\\n", line);
		/*
		 * Reopen the page, and check that the new file for existed properties
		 * file won't be created. Open hello.jsp file once again.
		 */
		propertiesEditor.close();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", "hello.jsp").open();
		editor = new TextEditor("hello.jsp");
		/*
		 * Select some text
		 */
		editor.setCursorPosition(3, 0);
		editor.selectLine(3);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
		editor.insertText(3, 0, "Plain text");
		editor.setCursorPosition(3, 2);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		editor.save();
		/*
		 * Activate the dialog
		 */
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Check that the property key and value text are auto completed.
		 */
		defKeyText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_KEY);
		assertEquals("Plain_text", defKeyText.getText());
		defValueText = new LabeledText(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_PROPERTIES_VALUE);
		assertEquals("\\r\\n\\r\\nPlain text", defValueText.getText());
		/*
		 * Check that checkbox for the new file is selected
		 */
		assertTrue(new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE).isChecked());
		/*
		 * 'Next>' button should. Press it.
		 */
		new NextButton().click();
		/*
		 * Check the folder name
		 */
		assertEquals(JBT_TEST_PROJECT_NAME + "/JavaSource", new LabeledText(FOLDER_TEXT_LABEL).getText());
		/*
		 * Check the file name
		 */
		assertEquals("hello.properties", new LabeledText("File name:").getText());
		/*
		 * 'Next>' button should be disabled.
		 */
		assertFalse("(Next>) button should be disabled.", new NextButton().isEnabled());
		/*
		 * Create new file
		 */
		assertFalse("(OK) button should be disabled.", new OkButton().isEnabled());
		new CancelButton().click();
	}

	@Test
	public void testNewBundleRegisteringInFacesConfig() throws Throwable {
		createNewBundleToLastPage("", "");
		/*
		 * Select 'in the faces-config.xml file' radio button
		 */
		new RadioButton(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_FACES_CONFIG).click();
		/*
		 * 'OK' button should be enabled. Press it.
		 */
		new OkButton().click();
		/*
		 * Check that the text was replaced
		 */
		TextEditor editor = new TextEditor(TEST_PAGE);
		editor.activate();
		assertEquals("Replaced text is incorrect", 214, editor.getPositionOfText("#{inputUserName.Input}"));
		/*
		 * Check that the resource-bundle has been added to the faces-config
		 */
		FacesConfigEditor facesConfigEditor = new FacesConfigEditor("faces-config.xml");
		FacesConfigSourceEditor facesConfigSourceEditor = facesConfigEditor.getFacesConfigSourceEditor();
		facesConfigSourceEditor.selectLine(23);
		assertEquals("24 line is not: <resource-bundle>", "<resource-bundle>",
				ExternalizeStringsDialogTest.trimSelectedLine(facesConfigSourceEditor));
		facesConfigSourceEditor.selectLine(24);
		assertEquals("25 line is not: <base-name>inputUserName</base-name>", "<base-name>inputUserName</base-name>",
				ExternalizeStringsDialogTest.trimSelectedLine(facesConfigSourceEditor));
		facesConfigSourceEditor.selectLine(25);
		assertEquals("26 line is not: <var>inputUserName</var>", "<var>inputUserName</var>",
				ExternalizeStringsDialogTest.trimSelectedLine(facesConfigSourceEditor));
		facesConfigSourceEditor.selectLine(26);
		assertEquals("27 line is not: </resource-bundle>", "</resource-bundle>",
				ExternalizeStringsDialogTest.trimSelectedLine(facesConfigSourceEditor));
		editor.close();
		facesConfigEditor.close();
	}

	@Test
	public void testNewBundleRegisteringViaLoadBundleTag() throws Throwable {
		createNewBundleToLastPage("", "inputUserName2");
		/*
		 * Select 'via <f:loadBundle> tag on the current page' radio button
		 */
		new RadioButton(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_LOAD_BUNDLE).click();
		/*
		 * 'OK' button should be enabled. Press it.
		 */
		new OkButton().click();
		/*
		 * Check that the text was replaced
		 */
		TextEditor editor = new TextEditor(TEST_PAGE);
		editor.activate();
		editor.selectLine(7);
		assertEquals("Replaced text is incorrect",
				"<title><f:loadBundle var=\"inputUserName\" basename=\"inputUserName2\" />#{inputUserName.Input} User Name Page</title>",
				ExternalizeStringsDialogTest.trimSelectedLine(editor));
		editor.close();
	}

	@Test
	public void testTaglibRegistrationInJSP() throws Throwable {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();

		TextEditor editor = new TextEditor(TEST_PAGE);
		editor.activate();
		editor.selectLine(0);
		assertEquals("JSF Core taglib should present on page",
				"<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>",
				ExternalizeStringsDialogTest.trimSelectedLine(editor));
		/*
		 * Rewrite the taglib
		 */
		KeyboardFactory.getKeyboard().type(" ");
		editor.save();
		editor.setCursorPosition(18);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		/*
		 * Make sure that taglib doesn't present
		 */
		assertTrue("JSF Core taglib should be removed", //$NON-NLS-1$
				(editor.getText().indexOf("<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\" %>") == -1)); //$NON-NLS-1$
		createNewBundleToLastPage("", "inputUserName4"); //$NON-NLS-1$ //$NON-NLS-2$
		/*
		 * Select 'via <f:loadBundle> tag on the current page' radio button
		 */
		new RadioButton(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_LOAD_BUNDLE).click();
		/*
		 * 'OK' button should be enabled. Press it.
		 */
		new OkButton().click();
		/*
		 * Check that the text was replaced
		 */
		editor.activate();
		editor.save();
		editor.selectLine(1);
		assertEquals("https://issues.jboss.org/browse/JBIDE-17920\nTaglig insertion failed!", //$NON-NLS-1$
				"<%@ taglib uri=\"http://java.sun.com/jsf/core\" prefix=\"f\"%>", //$NON-NLS-1$
				ExternalizeStringsDialogTest.trimSelectedLine(editor));
	}

	@Test
	public void testTaglibRegistrationInXHTML() throws Throwable {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(FACELETS_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", FACELETS_TEST_PAGE)
				.open();
		TextEditor editor = new TextEditor(FACELETS_TEST_PAGE);
		editor.activate();
		editor.selectLine(4);
		assertTrue("JSF Core taglib should be present on page",
				editor.getSelectedText().contains("xmlns:f=\"http://java.sun.com/jsf/core\""));
		/*
		 * Rewrite the taglib
		 */
		KeyboardFactory.getKeyboard().type(" ");
		editor.save();
		/*
		 * Make sure that taglib doesn't present
		 */
		assertTrue("JSF Core taglib should be removed",
				(editor.getText().indexOf("xmlns:f=\"http://java.sun.com/jsf/core\"") == -1));
		editor.selectText("User");
		/*
		 * Get toolbar button
		 */
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		bot.shell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE).activate();
		CheckBox checkBox = new CheckBox(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_NEW_FILE);
		checkBox.click();
		assertTrue("Checkbox should be checked.", checkBox.isChecked());
		new NextButton().click();
		/*
		 * 'Next>' button should be enabled. Press it.
		 */
		new NextButton().click();
		/*
		 * Select 'via <f:loadBundle> tag on the current page' radio button
		 */
		new RadioButton(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_LOAD_BUNDLE).click();
		/*
		 * 'OK' button should be enabled. Press it.
		 */
		new OkButton().click();
		/*
		 * Check that the text was replaced
		 */
		editor.activate();
		editor.selectLine(6);
		assertTrue("https://issues.jboss.org/browse/JBIDE-17920\nTaglig insertion failed!",
				editor.getSelectedText().contains("xmlns:f=\"http://java.sun.com/jsf/core\">"));
	}

	@Test
	public void testToolBarIconEnableState() throws Throwable {
		packageExplorer.getProject(FACELETS_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", FACELETS_TEST_PAGE)
				.open();
		TextEditor editor = new TextEditor(FACELETS_TEST_PAGE);
		editor.activate();
		editor.setText(ENABLED_TEST_TEXT);
		navigateToAndTestIcon(editor, new Point(0, 1), false);
		navigateToAndTestIcon(editor, new Point(0, 3), false);
		navigateToAndTestIcon(editor, new Point(0, 16), true);
		navigateToAndTestIcon(editor, new Point(0, 3), false);
	}

	private void navigateToAndTestIcon(TextEditor editor, Point point, boolean enabled) {
		/*
		 * Select some text
		 */
		editor.setCursorPosition(point.x, point.y);
		/*
		 * Send key press event to fire VPE listeners
		 */
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_RIGHT);
		/*
		 * Get toolbar button
		 */
		ToolItem tiExternalize = new DefaultToolItem(TOOL_TIP);
		if (enabled) {
			new WaitUntil(new WidgetIsEnabled(tiExternalize), TimePeriod.getCustom(3000), false);
		} else {
			new WaitWhile(new WidgetIsEnabled(tiExternalize), TimePeriod.getCustom(3000), false);
		}
		assertEquals(enabled ? TOOLBAR_ICON_ENABLED : TOOLBAR_ICON_DISABLED, enabled, tiExternalize.isEnabled());
	}

	/**
	 * Creates the new bundle till last page.
	 *
	 * @param folderPath
	 *            the folder path
	 * @param fileName
	 *            the file name
	 * @return the swt bot editor
	 */
	private void createNewBundleToLastPage(String folderPath, String fileName) {
		/*
		 * Open simple html file in order to get the VPE toolbar
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages", TEST_PAGE).open();
		TextEditor editor = new TextEditor(TEST_PAGE);
		/*
		 * Select some text
		 */
		editor.selectText("Input");
		/*
		 * Get toolbar button
		 */
		new DefaultToolItem(TOOL_TIP).click();
		new DefaultShell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
		/*
		 * Enable next page and check it
		 */
		CheckBox checkBox = new CheckBox();
		checkBox.toggle(true);
		assertTrue("Checkbox should be checked.", checkBox.isChecked());
		new NextButton().click();
		/*
		 * Create new file
		 */
		Text folderText = new LabeledText(FOLDER_TEXT_LABEL);
		assertNotNull("'" + FOLDER_TEXT_LABEL + "' text field is not found", folderText);
		if ((null != folderPath) && (folderPath.length() > 0)) {
			folderText.setText(folderPath);
		}
		Text fileNameField = new LabeledText("File name:");
		assertNotNull("'File Name:' text field is not found", fileNameField); //$NON-NLS-1$
		if ((null != fileName) && (fileName.length() > 0)) {
			fileNameField.setText(fileName);
		}
		/*
		 * 'Next>' button should be enabled. Press it.
		 */
		new NextButton().click();
	}

	private static String trimSelectedLine(TextEditor textEditor) {
		String selectedLine = textEditor.getSelectedText();
		String result = "";
		if (selectedLine != null && selectedLine.length() > 0) {
			result = selectedLine;
			if (result.endsWith("\\n")) {
				result = result.substring(0, result.length() - 1);
			}
			result = result.trim();
		}
		return result;
	}

}