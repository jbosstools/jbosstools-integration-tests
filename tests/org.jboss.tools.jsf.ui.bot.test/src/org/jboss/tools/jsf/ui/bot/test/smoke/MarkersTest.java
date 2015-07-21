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

import java.util.LinkedList;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.Keyboard;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.MarkerHelper;
import org.jboss.tools.ui.bot.ext.helper.QuickFixHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * Test Markers position and attributes regarding to JSF components within jsp
 * page
 * 
 * @author Vladimir Pakan
 *
 */
public class MarkersTest extends JSFAutoTestCase {
	private TextEditor editor;
	private String originalEditorText;

	/**
	 * Test open on functionality of JSF components within jsp page
	 */
	@Test
	public void testMarkers() {
		MarkerHelper markerHelper = new MarkerHelper(TEST_PAGE, VPEAutoTestCase.JBT_TEST_PROJECT_NAME, "WebContent",
				"pages");
		String textToSelect = "<h:outputText value=\"#{Message.header";
		String insertText = "yyaddedxx";
		int[] expectedMarkerLines = new int[3];
		String[] expectedMarkerDesc = new String[3];
		editor.setCursorPosition(editor.getPositionOfText(textToSelect) + textToSelect.length());
		Keyboard keyboard = KeyboardFactory.getKeyboard();
		keyboard.type(insertText);
		expectedMarkerLines[0] = editor.getCursorPosition().x;
		expectedMarkerDesc[0] = "^\"header" + insertText + "\" cannot be resolved";
		textToSelect = "<h:outputText value=\"#{Message.prompt_message";
		editor.setCursorPosition(editor.getPositionOfText(textToSelect) + textToSelect.length());
		keyboard.type(insertText);
		expectedMarkerLines[1] = editor.getCursorPosition().x;
		expectedMarkerDesc[1] = "^\"prompt_message" + insertText + "\" cannot be resolved";
		textToSelect = "<h:inputText value=\"#{user.name";
		editor.setCursorPosition(editor.getPositionOfText(textToSelect) + textToSelect.length());
		keyboard.type(insertText);
		expectedMarkerLines[2] = editor.getCursorPosition().x;
		expectedMarkerDesc[2] = "^\"name" + insertText + "\" cannot be resolved";
		editor.save();
		new WaitWhile(new JobIsRunning());
		// Check markers
		for (int index = 0; index < expectedMarkerLines.length; index++) {
			markerHelper.checkForMarker(String.valueOf(expectedMarkerLines[index] + 1), expectedMarkerDesc[index]);
		}
		// check quick fix
		LinkedList<String> expectedQuickFixes = new LinkedList<String>();
		expectedQuickFixes.add("Configure Problem Severity for preference 'Property cannot be resolved'");
		QuickFixHelper.checkQuickFixContent(SWTTestExt.bot, TEST_PAGE, textToSelect, textToSelect.length(),
				insertText.length(), expectedQuickFixes);
		ContentAssistant quickFixes = editor.openQuickFixContentAssistant();
		quickFixes.chooseProposal(quickFixes.getProposals().get(0));
		new WaitUntil(new ShellWithTextIsAvailable("Preferences (Filtered)"), TimePeriod.NORMAL);
		new DefaultShell("Preferences (Filtered)");
		// check if preferences tree is properly filtered
		assertTrue("JBoss Tools > Web > Expression Language > Validation Preferences Item has to be selected",
				new DefaultTreeItem("JBoss Tools", "Web", "Expression Language", "Validation").isSelected());
		boolean isFilterValueOK = false;
		final String expectedFilterValue = "Property cannot be resolved:";
		try {
			new DefaultText(expectedFilterValue);
			isFilterValueOK = true;
		} catch (CoreLayerException cle) {
			// do nothing
		}
		boolean isValidationOptionPresent = false;
		final String expectedValidationOption = "Property cannot be resolved:";
		try {
			new DefaultLabel(expectedValidationOption);
			isValidationOptionPresent = true;
		} catch (CoreLayerException cle) {
			// do nothing
		}
		bot.button(IDELabel.Button.CANCEL).click();
		final String expectedSelectedTreeItem = "Validation";
		assertTrue("Validation Filter has to have value " + expectedFilterValue + " but it does not", isFilterValueOK);
		assertTrue("Validation Option has to have label " + expectedValidationOption + " but it does not",
				isValidationOptionPresent);
	}   
  @Override
  public void setUp() throws Exception {
    super.setUp();
    openPage(TEST_PAGE);
    editor = new TextEditor(TEST_PAGE);
    originalEditorText = editor.getText();
  }

  @Override
  public void tearDown() throws Exception {
    if (editor != null) {
      editor.setText(originalEditorText);
      editor.save();
      editor.close();
    }
    super.tearDown();
  }
}