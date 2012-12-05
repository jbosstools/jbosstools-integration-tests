/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
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
import static org.junit.Assert.fail;

import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ActiveEditorHasTitleCondition;
import org.jboss.tools.ui.bot.ext.condition.ActiveShellContainsWidget;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Helper for Open On functionality testing
 * 
 * @author Vladimir Pakan
 * @author jjankovi
 * 
 */
public class OpenOnHelper {
	protected static final Logger log = Logger.getLogger(OpenOnHelper.class);

	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, String expectedOpenedFileName) {
		
		int offset = textToSelect.contains("@")?1:0;
		return checkOpenOnFileIsOpened(bot, editorTitle, textToSelect, 
				offset, textToSelect.length() - offset, 0, expectedOpenedFileName);
		
	}
	
	/**
	 * Applies Open On (F3) on textToSelect within editor with editorTitle and
	 * checks if expectedOpenedFileName was opened
	 * 
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param expectedOpenedFileName
	 */
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex,
			String expectedOpenedFileName) {

		SWTBotEclipseEditor sourceEditor = SWTJBTExt.selectTextInSourcePane(
				bot, editorTitle, textToSelect, selectionOffset,
				selectionLength, textToSelectIndex);

		bot.sleep(Timing.time3S());

		sourceEditor.setFocus();
		// process UI Events
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
			}
		});
		bot.sleep(Timing.time3S());
		new SWTUtilExt(bot).waitForNonIgnoredJobs();

		KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_F3);
		// process UI Events
		UIThreadRunnable.syncExec(new VoidResult() {
			@Override
			public void run() {
			}
		});
		bot.sleep(Timing.time3S());
		new SWTUtilExt(bot).waitForNonIgnoredJobs();

		return checkActiveEditorTitle(bot, expectedOpenedFileName);

	}
	
	public static void showOpenOnOptions(SWTBotExt bot, String editorTitle, 
			String textToSelect, String openOnOption) {
		int offset = textToSelect.contains("@")?1:0;
		showOpenOnOptions(bot, editorTitle, textToSelect, offset, 
				textToSelect.length() - offset, 0, openOnOption);
	}
	
	/**
	 * Selects specified text in specified opened editor and shows open on options
	 * via selecting Navigate -> Open Hyperlink
	 * 
	 * @param bot
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param openOnOption
	 */
	public static void showOpenOnOptions(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex, String openOnOption) {
		
		SWTJBTExt.selectTextInSourcePane(
				bot, editorTitle, textToSelect, selectionOffset,
				selectionLength, textToSelectIndex);
		bot.menu(IDELabel.Menu.NAVIGATE).menu(IDELabel.Menu.OPEN_HYPERLINK)
				.click();

		bot.waitUntil(new ActiveShellContainsWidget(bot, Table.class));
		
	}
	
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, String openOnOption,
			String expectedOpenedFileName) {
		
		int offset = textToSelect.contains("@")?1:0;
		return checkOpenOnFileIsOpened(bot, editorTitle, textToSelect, 
				offset, textToSelect.length() - offset, 0, 
				openOnOption, expectedOpenedFileName);
	}

	/**
	 * Shows all open on options on textToSelect within editor with editorTitle, 
	 * select the one containing text openOnOption and finally checks if 
	 * expectedOpenedFileName was opened
	 * 
	 * @param bot
	 * @param editorTitle
	 * @param textToSelect
	 * @param selectionOffset
	 * @param selectionLength
	 * @param textToSelectIndex
	 * @param openOnOption
	 * @param expectedOpenedFileName
	 * @return
	 */
	public static SWTBotEditor checkOpenOnFileIsOpened(SWTBotExt bot,
			String editorTitle, String textToSelect, int selectionOffset,
			int selectionLength, int textToSelectIndex, String openOnOption,
			String expectedOpenedFileName) {
		
		showOpenOnOptions(bot, editorTitle, textToSelect, selectionOffset, 
				selectionLength, textToSelectIndex, openOnOption);
		
		SWTBotTable table = bot.activeShell().bot().table(0);

		boolean optionFound = false;
		String foundOptions = "";

		for (int i = 0; i < table.rowCount(); i++) {
			String foundOption = table.getTableItem(i).getText();
			foundOptions = foundOptions + foundOption + ", ";
			if (foundOption.contains(openOnOption)) {
				optionFound = true;
				table.click(i, 0);
				break;
			}
		}
		foundOptions = foundOptions.substring(0, foundOptions.length() - 3);
		assertTrue(openOnOption + " was not found in open on options of "
				+ textToSelect + " Found: " + foundOptions, optionFound);
		
		return checkActiveEditorTitle(bot, expectedOpenedFileName);
		
	}
	
	private static SWTBotEditor checkActiveEditorTitle(SWTBotExt bot, String expectedOpenedFileName) {
		
		SWTBotEditor activeEditor = null;
		try {
			bot.waitUntil(new ActiveEditorHasTitleCondition(bot,
					expectedOpenedFileName), Timing.time10S());
			activeEditor = bot.activeEditor();
		} catch (TimeoutException toe) {
			activeEditor = bot.activeEditor();
			fail("Opened file has to have title " + expectedOpenedFileName
					+ " but has " + activeEditor.getTitle());
		}
		
		return activeEditor;
		
	}

}
