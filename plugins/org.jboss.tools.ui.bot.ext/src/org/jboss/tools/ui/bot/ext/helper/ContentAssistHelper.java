 /*******************************************************************************
  * Copyright (c) 2007-2012 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.ui.bot.ext.helper;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.FormatUtils;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.parts.ContentAssistBot;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;

/**
 * Helper for Content Assist functionality testing
 * @author Vladimir Pakan
 *
 */
public class ContentAssistHelper {
  protected static final Logger log = Logger.getLogger(ContentAssistHelper.class);
  /**
   * Checks Content Assist content on specified position within editor with editorTitle
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   * @param expectedProposalList
   */
  public static SWTBotEditor checkContentAssistContent(SWTBotExt bot,
      String editorTitle, String textToSelect, int selectionOffset,
      int selectionLength, int textToSelectIndex, List<String> expectedProposalList) {

    return checkContentAssistContent(bot, editorTitle, textToSelect, selectionOffset, 
    		selectionLength, textToSelectIndex, expectedProposalList, true);

  }
  /**
   * Checks Content Assist content on specified position within editor with editorTitle
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editor
   * @param line
   * @param column
   * @param expectedProposalList
   * @param mustEquals
   */
	public static void checkContentAssistContent(TextEditor editor, int line, int column,
			List<String> expectedCodeProposals, boolean mustEquals) {
		editor.setCursorPosition(line, column);
		ContentAssistant contentAssistant = editor.openContentAssistant();
		List<String> currentCodeProposals = contentAssistant.getProposals();
		contentAssistant.close();
		assertContentAssistantContent(expectedCodeProposals, currentCodeProposals, mustEquals);
	}
  
  /**
   * Checks Content Assist content on specified position within editor with editorTitle
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   * @param expectedProposalList
   * @param mustEquals
   */
	public static SWTBotEditor checkContentAssistContent(SWTBotExt bot, String editorTitle,
			String textToSelect, int selectionOffset, int selectionLength, int textToSelectIndex,
			List<String> expectedProposalList, boolean mustEquals) {
		
		SWTJBTExt.selectTextInSourcePane(bot, editorTitle, textToSelect, selectionOffset,
			selectionLength, textToSelectIndex);
		
		bot.sleep(Timing.time1S());

		SWTBotEditorExt editor = SWTTestExt.bot.swtBotEditorExtByTitle(editorTitle);
		ContentAssistBot contentAssist = editor.contentAssist();
		List<String> currentProposalList = contentAssist.getProposalList();
		assertTrue(
				"Code Assist menu has incorrect menu items.\n"
						+ "Expected Proposal Menu Labels vs. Current Proposal Menu Labels :\n"
						+ FormatUtils.getListsDiffFormatted(expectedProposalList,
								currentProposalList),
				mustEquals ? expectedProposalList.equals(currentProposalList) : currentProposalList
						.containsAll(expectedProposalList));

		return editor;
  }
  
  /**
   * Checks Content Assist content on specified position within editor with editorTitle
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   * @param expectedProposalList
   */
  public static SWTBotEditor checkContentAssistContent(SWTBotExt bot,
      String editorTitle, String textToSelect, int selectionOffset,
      int selectionLength, List<String> expectedProposalList) {

    return checkContentAssistContent(bot, editorTitle, textToSelect, 
    		selectionOffset, selectionLength, expectedProposalList, true);

  }
  
  /**
   * Checks Content Assist content on specified position within editor with editorTitle
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   * @param expectedProposalList
   * @param mustEquals
   */
  public static SWTBotEditor checkContentAssistContent(SWTBotExt bot,
      String editorTitle, String textToSelect, int selectionOffset,
      int selectionLength, List<String> expectedProposalList, boolean mustEquals) {

    return ContentAssistHelper.checkContentAssistContent(bot, 
        editorTitle, 
        textToSelect, 
        selectionOffset, 
        selectionLength,
        0,
        expectedProposalList,
        mustEquals);

  }
  
  /**
   * Checks Content Assist auto proposal. It's case when there is only one
   * content assist item and that item is automatically inserted into editor
   * and checks if expectedProposalList is equal to current Proposal List 
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   * @param expectedInsertedText
   */
  public static SWTBotEditor checkContentAssistAutoProposal(SWTBotExt bot,
      String editorTitle, String textToSelect, int selectionOffset,
      int selectionLength, int textToSelectIndex, String expectedInsertedText) {

    SWTJBTExt.selectTextInSourcePane(bot,
        editorTitle, textToSelect, selectionOffset, selectionLength,
        textToSelectIndex);

    bot.sleep(Timing.time1S());

    SWTBotEditorExt editor = SWTTestExt.bot.swtBotEditorExtByTitle(editorTitle);
    String editorLineBeforeInsert = editor.getTextOnCurrentLine();
    int xPos = editor.cursorPosition().column;
    String expectedEditorLineAfterInsert = editorLineBeforeInsert.substring(0,xPos) +
        expectedInsertedText +
        editorLineBeforeInsert.substring(xPos);
    ContentAssistBot contentAssist = editor.contentAssist();
    contentAssist.invokeContentAssist();
    String editorLineAfterInsert = editor.getTextOnCurrentLine();
    assertTrue("Text on current line should be:\n" + 
        expectedEditorLineAfterInsert +
        "\nbut is:\n" + editorLineAfterInsert
        , editorLineAfterInsert.equals(expectedEditorLineAfterInsert));

    return editor;

  }
  /**
   * Applies Content Assist auto proposal. It's case when there is only one
   * content assist item and that item is automatically inserted into editor
   * @param editorTitle
   * @param textToSelect
   * @param selectionOffset
   * @param selectionLength
   * @param textToSelectIndex
   */
  public static SWTBotEditor applyContentAssistAutoProposal(SWTBotExt bot,
      String editorTitle, String textToSelect, int selectionOffset,
      int selectionLength, int textToSelectIndex) {

    SWTJBTExt.selectTextInSourcePane(bot,
        editorTitle, textToSelect, selectionOffset, selectionLength,
        textToSelectIndex);

    bot.sleep(Timing.time1S());

    SWTBotEditorExt editor = SWTTestExt.bot.swtBotEditorExtByTitle(editorTitle);
    ContentAssistBot contentAssist = editor.contentAssist();
    contentAssist.invokeContentAssist();

    return editor;

  }
	/**
	 * Asserts if codeAssistant contains item and apply this item in case
	 * applyCodeAssist is true
	 * 
	 * @param item
	 * @param applyCodeAssist
	 */
	public static void assertContentAssistantContains(ContentAssistant contentAssistant, String item,
			boolean applyCodeAssist) {
		List<String> caProposals = contentAssistant.getProposals();
		boolean contains = caProposals.contains(item);
		if (!contains){
			contentAssistant.close();
		}
		assertTrue("Editor Content Assist doesn't containt item with label: " + item, contains);
		if (applyCodeAssist){
			contentAssistant.chooseProposal(item);
		}
	}
	
	/**
	 * Asserts if codeAssistant content is as expected
	 * applyCodeAssist is true
	 * 
	 * @param expectedCodeProposals
	 * @param currentCodeProposals
	 * @param mustEquals
	 */
	public static void assertContentAssistantContent(List<String> expectedCodeProposals, List<String> currentCodeProposals,
			boolean mustEquals) {
		assertTrue("Code Assist menu has incorrect menu items.\n"
			+ "Expected Proposal Menu Labels vs. Current Proposal Menu Labels :\n"
			+ FormatUtils.getListsDiffFormatted(expectedCodeProposals, currentCodeProposals),
				mustEquals ? expectedCodeProposals.equals(currentCodeProposals)
						: currentCodeProposals.containsAll(expectedCodeProposals));
	}
} 
