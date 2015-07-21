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
package org.jboss.tools.jsf.ui.bot.test.templates;

import org.jboss.reddeer.common.platform.OS;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.CSSStyleDialogVariables;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jsf.ui.bot.test.UnknownTagDialogVariables;
import org.jboss.tools.ui.bot.ext.CompareUtils;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.junit.Test;

public class SetTemplateForUnknownTagTest extends JSFAutoTestCase {

	private static final String TAG_NAME = "h:unknowntag";//$NON-NLS-1$
	private static final String TAG_URI = "http://java.sun.com/jsf/html";//$NON-NLS-1$
	private static final String DISPALY_TAG = "b";//$NON-NLS-1$
	private static final String CHILDREN_ALLOWS = "yes";//$NON-NLS-1$
	private static final String TEMPLATE_VALUE = "myValue";

	@Test
	public void testSetTemplateForUnknownTag() throws Throwable {
		openTestPage();
		setEditor(new TextEditor(TEST_PAGE));
		setEditorText(getEditor().getText());
		getEditor().insertLine(13,
				"<" + SetTemplateForUnknownTagTest.TAG_NAME + "></" + SetTemplateForUnknownTagTest.TAG_NAME + ">");
		getEditor().save();
		getEditor().setCursorPosition(13, 5);
		setUpTemplate();
		editTemplate();
		removeTemplate();
	}

	private void setUpTemplate() throws Throwable {
		new DefaultToolItem(WidgetVariables.PREFERENCES).click();
		new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
		new DefaultTabItem(WidgetVariables.VPE_TEMPLATES_TAB).activate();
		new PushButton(IDELabel.Button.ADD).click();
		new DefaultShell(UnknownTagDialogVariables.DIALOG_TITLE);
		new LabeledText(UnknownTagDialogVariables.TAG_NAME_FIELD).setText(TAG_NAME);
		new LabeledText(UnknownTagDialogVariables.TAG_URI_FIELD).setText(TAG_URI);
		new LabeledText(UnknownTagDialogVariables.DISPLAY_TAG).setText(DISPALY_TAG);
		new LabeledCheckBox(UnknownTagDialogVariables.ALLOW_CHILDREN_CHECKBOX).toggle(true);
		new LabeledText(UnknownTagDialogVariables.VALUE_FIELD).setText(SetTemplateForUnknownTagTest.TEMPLATE_VALUE);
		new LabeledText(UnknownTagDialogVariables.TAG_STYLE_FIELD).setText("color:red");
		new PushButton(new WithTooltipTextMatcher(UnknownTagDialogVariables.EDIT_TAG_STYLE_TIP)).click();
		String returnValue = setStyles();
		new OkButton().click();
		new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
		new OkButton().click();
		getEditor().activate();
		assertTrue(
				"Atttributes are not as expected:\n" + "Expected: "
						+ "color:black;text-decoration:underline;font-family:Arial;" + "Value: " + returnValue,
				CompareUtils.compareStyleAttributes("color:black;text-decoration:underline;font-family:Arial;", //$NON-NLS-1$
						returnValue));
		new DefaultToolItem(RunningPlatform.isOperationSystem(OS.MACOSX) ? IDELabel.ToolbarButton.REFRESH_MAC_OS
				: IDELabel.ToolbarButton.REFRESH).click();
		assertVisualEditorContainsNodeWithValue(new InternalBrowser(), SetTemplateForUnknownTagTest.TEMPLATE_VALUE,
				TEST_PAGE);
	}

	private void editTemplate() throws Throwable {
		new DefaultToolItem(WidgetVariables.PREFERENCES).click();
		new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
		new DefaultTabItem(WidgetVariables.VPE_TEMPLATES_TAB).activate();
		DefaultTable table = new DefaultTable();
		table.select(0);
		try {
			checkTable(table);
			new PushButton(IDELabel.Button.EDIT).click();
			new DefaultShell(UnknownTagDialogVariables.DIALOG_TITLE);
			new LabeledText(UnknownTagDialogVariables.TAG_URI_FIELD).setText("");
			new LabeledCheckBox(UnknownTagDialogVariables.ALLOW_CHILDREN_CHECKBOX).toggle(true);
			new LabeledText(UnknownTagDialogVariables.VALUE_FIELD).setText("");
			new LabeledText(UnknownTagDialogVariables.TAG_STYLE_FIELD).setText("");
			new OkButton().click();
		} catch (Throwable t) {
			throw t;
		} finally {
			new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
			new OkButton().click();
		}
		getEditor().activate();
		new DefaultToolItem(RunningPlatform.isOperationSystem(OS.MACOSX) ? IDELabel.ToolbarButton.REFRESH_MAC_OS
				: IDELabel.ToolbarButton.REFRESH).click();
		assertVisualEditorNotContainNodeWithValue(new InternalBrowser(), SetTemplateForUnknownTagTest.TEMPLATE_VALUE,
				TEST_PAGE);
		assertVisualEditorNotContainString(new InternalBrowser(), "<" + SetTemplateForUnknownTagTest.TAG_NAME,
				TEST_PAGE);
	}

	private void removeTemplate() throws Throwable {
		new DefaultToolItem(WidgetVariables.PREFERENCES).click();
		new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
		new DefaultTabItem(WidgetVariables.VPE_TEMPLATES_TAB).activate();
		DefaultTable table = new DefaultTable();
		table.select(0);
		new PushButton(WidgetVariables.REMOVE_BUTTON).click();
		new DefaultShell(WidgetVariables.PREF_FILTER_SHELL_TITLE);
		new OkButton().click();
		getEditor().activate();
		new DefaultToolItem(RunningPlatform.isOperationSystem(OS.MACOSX) ? IDELabel.ToolbarButton.REFRESH_MAC_OS
				: IDELabel.ToolbarButton.REFRESH).click();
		assertVisualEditorNotContainNodeWithValue(new InternalBrowser(), SetTemplateForUnknownTagTest.TEMPLATE_VALUE,
				TEST_PAGE);
		assertVisualEditorContainsNodeWithValue(new InternalBrowser(), SetTemplateForUnknownTagTest.TAG_NAME,
				TEST_PAGE);
	}

	private String setStyles() {
		new DefaultShell(CSSStyleDialogVariables.CSS_STYLE_DIALOG_TITLE);
		new DefaultTabItem(CSSStyleDialogVariables.TEXT_FONT_TAB).activate();
		new DefaultText(1).setText("Arial");
		Text txColor = new DefaultText(2);
		String colorText = txColor.getText();
		assertEquals("red", colorText); //$NON-NLS-1$
		txColor.setText("black");
		new LabeledCombo(CSSStyleDialogVariables.TEXT_DECORATION_FIELD).setText("underline");
		new OkButton().click();
		new DefaultShell(UnknownTagDialogVariables.DIALOG_TITLE);
		String returnValue = new LabeledText(UnknownTagDialogVariables.TAG_STYLE_FIELD).getText();
		return returnValue;
	}

	private void checkTable(Table table) {
		TableItem firstTableItem = table.getItem(0);
		assertEquals(TAG_NAME, firstTableItem.getText(0));
		assertEquals(DISPALY_TAG, firstTableItem.getText(1));
		assertEquals(TAG_URI, firstTableItem.getText(2));
		assertEquals(CHILDREN_ALLOWS, firstTableItem.getText(3));
	}
}
